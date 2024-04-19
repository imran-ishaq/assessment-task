package com.assignment.parsing.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.assignment.parsing.entity.*;
import com.assignment.parsing.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileStorageService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventDataRepository eventDataRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private SensorReadingRepository sensorReadingRepository;
    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private FileDetailsRepository fileDetailsRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    private final AmazonS3 s3Client;
    private ObjectMapper objectMapper = new ObjectMapper();

    public FileStorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void storeFile(MultipartFile file) throws IOException {
        s3Client.putObject(bucketName,file.getOriginalFilename(), this.convertMultiPartToFile(file));
    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    public List<FileDetails> getFileDetails() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        List<FileDetails> files = new ArrayList<>();
        if (objectListing != null) {
            List<S3ObjectSummary> s3ObjectSummariesList = objectListing.getObjectSummaries();
            if (!s3ObjectSummariesList.isEmpty()) {
                for (S3ObjectSummary objectSummary : s3ObjectSummariesList) {
                    String key = objectSummary.getKey();
                    files.add(downloadAndMapEntities(bucketName,key));
                }
            }
        }
        return files;
    }
    public FileDetails downloadAndMapEntities(String bucketName, String key) {
        try {
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));

            Event event = objectMapper.readValue(s3Object.getObjectContent(), Event.class);
            s3Object.close();

            if (event.getEvent_data() != null) {
                eventDataRepository.save(event.getEvent_data());
                if (event.getEvent_data().getPayload() != null) {
                    List<SensorReading> payloads = event.getEvent_data().getPayload();
                    for(SensorReading payload: payloads){
                        payload.setEventData(event.getEvent_data());
                    }
                    sensorReadingRepository.saveAll(payloads);
                }
                if (event.getEvent_data().getGateways() != null) {
                    List<Gateway> gateways = event.getEvent_data().getGateways();
                    for(Gateway gateway: gateways){
                        gateway.setEventData(event.getEvent_data());
                    }
                    gatewayRepository.saveAll(event.getEvent_data().getGateways());
                }
            }
            if (event.getCompany() != null) {
                companyRepository.save(event.getCompany());
            }
            if (event.getLocation() != null) {
                locationRepository.save(event.getLocation());
            }
            if (event.getDevice_type() != null) {
                deviceTypeRepository.save(event.getDevice_type());
            }
            if (event.getDevice() != null) {
                deviceRepository.save(event.getDevice());
            }
            eventRepository.save(event);
            return fileDetailsRepository.save(new FileDetails(key,event.getId(),"https://"+bucketName+".s3.amazonaws.com/"+key));
        } catch (IOException e) {
            return fileDetailsRepository.save(new FileDetails(key, (long) -1,"https://"+bucketName+".s3.amazonaws.com/"+key));
        }
    }
    public Event getFileData(Long id){
        Optional<FileDetails> file = fileDetailsRepository.findById(id.intValue());
        if(file.isPresent()){
            if(file.get().getEvent_id()!=-1){
                Event res = new Event();
                Optional<Event> event = eventRepository.findById(file.get().getEvent_id());
                if(event.isPresent()){
                    res.setId(event.get().getId());
                    res.setCompany(event.get().getCompany());
                    res.setDevice(event.get().getDevice());
                    res.setLocation(event.get().getLocation());
                    res.setDevice_type(event.get().getDevice_type());
                    res.setEvent_type(event.get().getEvent_type());
                    res.setEvent_data(event.get().getEvent_data());
                    res.getEvent_data().setGateways(null);
                    res.getEvent_data().setPayload(null);
                }
                return res;
            }else{
                return null;
            }
        }
        return null;
    }
}
