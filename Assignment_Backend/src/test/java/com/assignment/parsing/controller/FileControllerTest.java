package com.assignment.parsing.controller;

import com.assignment.parsing.dto.EventDTO;
import com.assignment.parsing.entity.FileDetails;
import com.assignment.parsing.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {
    @Mock
    private FileStorageService fileStorageService;
    @InjectMocks
    private FileController fileController;

    @Test
    public void testUploadFileSuccess() throws IOException {
        doNothing().when(fileStorageService).storeFile(any());

        ResponseEntity<String> response = fileController.uploadFile(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File uploaded successfully", response.getBody());

        verify(fileStorageService, times(1)).storeFile(any());
    }

    @Test
    public void testUploadFileFailure() throws IOException {
        doThrow(IOException.class).when(fileStorageService).storeFile(any());

        ResponseEntity<String> response = fileController.uploadFile(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to upload file", response.getBody());

        verify(fileStorageService, times(1)).storeFile(any());
    }

    @Test
    public void testGetFileDetails() {
        List<FileDetails> mockDetails = Collections.singletonList(new FileDetails());
        when(fileStorageService.getFileDetails()).thenReturn(mockDetails);

        List<FileDetails> retrievedDetails = fileController.getFileDetails();

        assertEquals(mockDetails, retrievedDetails);

        verify(fileStorageService, times(1)).getFileDetails();
    }

    @Test
    public void testGetFileData() {
        EventDTO mockEvent = new EventDTO();
        mockEvent.setId(1L);
        when(fileStorageService.getFileData(anyLong())).thenReturn(mockEvent);

        EventDTO retrievedEvent = fileController.getFileData(1L);

        assertNotNull(retrievedEvent);
        assertEquals(mockEvent, retrievedEvent);
        assertEquals(mockEvent.getId(), retrievedEvent.getId());

        verify(fileStorageService, times(1)).getFileData(anyLong());
    }
}