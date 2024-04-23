package com.assignment.parsing.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.assignment.parsing.dto.EventDTO;
import com.assignment.parsing.entity.Event;
import com.assignment.parsing.entity.FileDetails;
import com.assignment.parsing.entity.Gateway;
import com.assignment.parsing.entity.SensorReading;
import com.assignment.parsing.repository.*;
import com.assignment.parsing.utils.EventDataMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

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
    @Autowired
    private EventDataMapper eventDataMapper;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    private final AmazonS3 s3Client;
    private ObjectMapper objectMapper = new ObjectMapper();

    public FileStorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    /*
        storeFile() method takes a Multipart file sent via front end
        and convert it to a temp File before uploading it to s3bucket
        and finally after uploading it successfully deletes temp file.
    */

    public void storeFile(MultipartFile file) throws IOException {
        File tempFile = this.convertMultiPartFileToFile(file);  //create temp file in required format for s3 bucket to accept
        try {
            s3Client.putObject(bucketName, file.getOriginalFilename(), tempFile);  //Store temp file to s3 bucket
        } finally {
            tempFile.delete();          //delete temp file
        }
    }
    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /*
        getFileDetails() method fetch list of all objects that are present in s3bucket
        and then for each file fetched, it downloads,parse and save the file in db via downloadAndMapEntities()
        and finally returns a list of details about all files fetched and parsed.
    */

    public List<FileDetails> getFileDetails() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);     //list details about files stored in s# bucket
        List<FileDetails> files = new ArrayList<>();
        if (objectListing != null) {
            List<S3ObjectSummary> s3ObjectSummariesList = objectListing.getObjectSummaries();
            if (!s3ObjectSummariesList.isEmpty()) {
                for (S3ObjectSummary objectSummary : s3ObjectSummariesList) {
                    String key = objectSummary.getKey();                    // get filename for each file
                    Date objectSummaryLastModified = objectSummary.getLastModified();   // get last modified time for each file
                    Optional<FileDetails> fileAlreadyExist = fileDetailsRepository.findLatestByName(key);   //get latest file details from DB based on fetched file name from s3 bucket
                    if(fileAlreadyExist.isPresent() && objectSummaryLastModified.equals(fileAlreadyExist.get().getLast_modified())){
                        //if last modified time of details from s3 bucket is same as of db's last modified time mean, no change in file and no need to download it again
                        files.add(fileAlreadyExist.get());
                    }else{
                        //else download the file if it's changed or do not exist in db
                        files.add(downloadAndMapEntities(bucketName,key,objectSummaryLastModified));
                    }
                }
            }
        }
        return files;
    }

    /*
        downloadAndMapEntites() method downloads file mentioned via key parameter form s3 bucket
        and then parse it via object mapper and if file has correct format and is parsed correctly,
        it saves the data for each entity in their respective tables and at the end store the details
        of file being parsed in file_details table.
    */

    public FileDetails downloadAndMapEntities(String bucketName, String key,Date lastModified) {
        try {
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key)); //download file from s3 bucket with given key name

            Event event = objectMapper.readValue(s3Object.getObjectContent(), Event.class); // map the json file to event entity
            s3Object.close();
            // save the data for each entity in a file in their respective table.
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
            return fileDetailsRepository.save(new FileDetails(key,event.getId(),"https://"+bucketName+".s3.amazonaws.com/"+key,lastModified));
        } catch (IOException e) {
            return fileDetailsRepository.save(new FileDetails(key, (long) -1,"https://"+bucketName+".s3.amazonaws.com/"+key,lastModified));
        }
    }

    /*
        getFileData() method retrieve file details from the repository
        and then determine whether that file has valid format or not
        and if file has valid format it displays the data of that file on front-end
    */

    public EventDTO getFileData(Long id){
        Optional<FileDetails> file = fileDetailsRepository.findById(id.intValue()); //check if the details for required file exist in db
        if(file.isPresent()){
            if(file.get().getEvent_id()!=-1){   // if for the required file event id is not -1 means it has been parsed correctly and its data exist in db
                EventDTO eventDTO = new EventDTO();
                Optional<Event> event = eventRepository.findById(file.get().getEvent_id());     // retrieve data related to required file
                if(event.isPresent()){
                    eventDTO.setId(event.get().getId());
                    eventDTO.setCompany(event.get().getCompany());
                    eventDTO.setDevice(event.get().getDevice());
                    eventDTO.setLocation(event.get().getLocation());
                    eventDTO.setDevice_type(event.get().getDevice_type());
                    eventDTO.setEvent_type(event.get().getEvent_type());
                    eventDTO.setEvent_data(eventDataMapper.convertEventDataToDTO(event.get().getEvent_data()));
                }
                return eventDTO;    // return data of required file
            }
        }
        return null;        // no data exist or file not parsed correctly
    }
}
