package com.assignment.parsing.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.assignment.parsing.dto.EventDTO;
import com.assignment.parsing.dto.EventDataDTO;
import com.assignment.parsing.dto.GatewayDTO;
import com.assignment.parsing.dto.SensorReadingDTO;
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
import java.util.*;
import java.util.stream.Collectors;

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
        File tempFile = this.convertMultiPartToFile(file);
        try {
            s3Client.putObject(bucketName, file.getOriginalFilename(), tempFile);
        } finally {
            tempFile.delete();
        }
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
                    Date objectSummaryLastModified = objectSummary.getLastModified();
                    Optional<FileDetails> fileAlreadyExist = fileDetailsRepository.findLatestByName(key);
                    if(fileAlreadyExist.isPresent() && objectSummaryLastModified.equals(fileAlreadyExist.get().getLast_modified())){
                        files.add(fileAlreadyExist.get());
                    }else{
                        files.add(downloadAndMapEntities(bucketName,key,objectSummaryLastModified));
                    }
                }
            }
        }
        return files;
    }
    public FileDetails downloadAndMapEntities(String bucketName, String key,Date lastModified) {
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
            return fileDetailsRepository.save(new FileDetails(key,event.getId(),"https://"+bucketName+".s3.amazonaws.com/"+key,lastModified));
        } catch (IOException e) {
            return fileDetailsRepository.save(new FileDetails(key, (long) -1,"https://"+bucketName+".s3.amazonaws.com/"+key,lastModified));
        }
    }
    public EventDTO getFileData(Long id){
        Optional<FileDetails> file = fileDetailsRepository.findById(id.intValue());
        if(file.isPresent()){
            if(file.get().getEvent_id()!=-1){
                EventDTO eventDTO = new EventDTO();
                Optional<Event> event = eventRepository.findById(file.get().getEvent_id());
                if(event.isPresent()){
                    eventDTO.setId(event.get().getId());
                    eventDTO.setCompany(event.get().getCompany());
                    eventDTO.setDevice(event.get().getDevice());
                    eventDTO.setLocation(event.get().getLocation());
                    eventDTO.setDevice_type(event.get().getDevice_type());
                    eventDTO.setEvent_type(event.get().getEvent_type());
                    eventDTO.setEvent_data(convertEventDataToDTO(event.get().getEvent_data()));
                }
                return eventDTO;
            }
        }
        return null;
    }
    public EventDataDTO convertEventDataToDTO(EventData eventData) {
        EventDataDTO eventDataDTO = new EventDataDTO();
        eventDataDTO.setId(eventData.getId());
        eventDataDTO.setCorrelation_id(eventData.getCorrelation_id());
        eventDataDTO.setDevice_id(eventData.getDevice_id());
        eventDataDTO.setUser_id(eventData.getUser_id());
        List<SensorReadingDTO> sensorReadingDTOs = eventData.getPayload().stream()
                .filter(sensorReading -> sensorReading.getTimestamp() <= eventData.getTimestamp())
                .map(this::convertSensorReadingToDTO)
                .collect(Collectors.toList());
        eventDataDTO.setPayload(sensorReadingDTOs);

        List<GatewayDTO> gatewayDTOs = eventData.getGateways().stream()
                .map(this::convertGatewayToDTO)
                .collect(Collectors.toList());
        eventDataDTO.setGateways(gatewayDTOs);
        eventDataDTO.setFcnt(eventData.getFcnt());
        eventDataDTO.setFport(eventData.getFport());
        eventDataDTO.setRaw_format(eventData.getRaw_format());
        eventDataDTO.setRaw_payload(eventData.getRaw_payload());
        eventDataDTO.setClient_id(eventData.getClient_id());
        eventDataDTO.setHardware_id(eventData.getHardware_id());
        eventDataDTO.setTimestamp(eventData.getTimestamp());
        eventDataDTO.setApplication_id(eventData.getApplication_id());
        eventDataDTO.setDevice_type_id(eventData.getDevice_type_id());
        eventDataDTO.setLora_datarate(eventData.getLora_datarate());
        eventDataDTO.setFreq(eventData.getFreq());
        return eventDataDTO;
    }
    private SensorReadingDTO convertSensorReadingToDTO(SensorReading sensorReading) {
        SensorReadingDTO sensorReadingDTO = new SensorReadingDTO();
        sensorReadingDTO.setId(sensorReading.getId());
        sensorReadingDTO.setName(sensorReading.getName());
        sensorReadingDTO.setSensor_id(sensorReading.getSensor_id());
        sensorReadingDTO.setType(sensorReading.getType());
        sensorReadingDTO.setUnit(sensorReading.getUnit());
        sensorReadingDTO.setValue(sensorReading.getValue());
        sensorReadingDTO.setChannel(sensorReading.getChannel());
        sensorReadingDTO.setTimestamp(sensorReading.getTimestamp());
        return sensorReadingDTO;
    }
    private GatewayDTO convertGatewayToDTO(Gateway gateway) {
        GatewayDTO gatewayDTO = new GatewayDTO();
        gatewayDTO.setId(gateway.getId());
        gatewayDTO.setGweui(gateway.getGweui());
        gatewayDTO.setTime(gateway.getTime());
        gatewayDTO.setRssi(gateway.getRssi());
        gatewayDTO.setSnr(gateway.getSnr());
        return gatewayDTO;
    }
}
