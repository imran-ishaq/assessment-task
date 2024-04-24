package com.assignment.parsing.service;

import com.amazonaws.services.s3.AmazonS3;
import com.assignment.parsing.dto.EventDTO;
import com.assignment.parsing.entity.Event;
import com.assignment.parsing.entity.FileDetails;
import com.assignment.parsing.repository.*;
import com.assignment.parsing.utils.EventDataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {
    @Mock
    private AmazonS3 s3Client;
    @Mock
    private FileDetailsRepository fileDetailsRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventDataRepository eventDataRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private DeviceTypeRepository deviceTypeRepository;
    @Mock
    private GatewayRepository gatewayRepository;
    @Mock
    private SensorReadingRepository sensorReadingRepository;
    @Mock
    private EventDataMapper eventDataMapper;

    private FileStorageService fileStorageService;

    @BeforeEach
    public void setUp() {
        fileStorageService = new FileStorageService(s3Client, eventRepository,
                eventDataRepository, deviceRepository, deviceTypeRepository,
                companyRepository, locationRepository, sensorReadingRepository,
                gatewayRepository, fileDetailsRepository, eventDataMapper);
    }


    @Test
    public void testStoreFile_success() throws IOException {
        byte[] fileContent = "Mock file content".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.json", "application/json", fileContent);
        File convFile = new File("test.json");

        fileStorageService.storeFile(multipartFile);

        verify(s3Client,times(1)).putObject(null, "test.json", convFile);
    }

    @Test
    public void testGetFileDetails_emptyList() {
        when(s3Client.listObjects((String) isNull())).thenReturn(null);

        List<FileDetails> files = fileStorageService.getFileDetails();

        assertEquals(files, new ArrayList<>());

        verify(s3Client, times(1)).listObjects((String) isNull());
        verify(fileDetailsRepository, times(0)).findLatestByName(anyString());
    }

    @Test
    void getFileData_InvalidFormat() {
        FileDetails mockFileDetails = new FileDetails();
        mockFileDetails.setEvent_id(-1L);
        mockFileDetails.setId(1L);

        when(fileDetailsRepository.findById(anyInt())).thenReturn(Optional.of(mockFileDetails));

        EventDTO eventDTO = fileStorageService.getFileData(1L);

        assertNull(eventDTO);
        verify(fileDetailsRepository, times(1)).findById(anyInt());
    }

    @Test
    void getFileData_ValidFormat() {
        FileDetails mockFileDetails = new FileDetails();
        mockFileDetails.setEvent_id(1L);
        mockFileDetails.setId(1L);

        Event mockEvent = new Event();
        mockEvent.setId(1L);

        when(fileDetailsRepository.findById(anyInt())).thenReturn(Optional.of(mockFileDetails));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(mockEvent));

        EventDTO eventDTO = fileStorageService.getFileData(1L);

        assertNotNull(eventDTO);
        assertEquals(mockEvent.getId(), eventDTO.getId());
        assertEquals(mockEvent.getCompany(), eventDTO.getCompany());

        verify(fileDetailsRepository, times(1)).findById(anyInt());
        verify(eventRepository, times(1)).findById(anyLong());
    }
}
