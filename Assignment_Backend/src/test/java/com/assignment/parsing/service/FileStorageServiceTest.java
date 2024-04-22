package com.assignment.parsing.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.assignment.parsing.entity.FileDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {
    @Mock
    private AmazonS3 s3Client;
    @InjectMocks
    private FileStorageService fileStorageService;

    @Test
    public void testGetFileDetails_emptyList() throws IOException {
        ObjectListing objectListing = new ObjectListing();
        objectListing.setBucketName("filestoreforassignment");

        when(s3Client.listObjects((String) isNull())).thenReturn(objectListing);

        List<FileDetails> files = fileStorageService.getFileDetails();
        verify(s3Client, times(1)).listObjects((String) isNull());
    }

    @Test
    void getFileDetails() {
    }

    @Test
    void downloadAndMapEntities() {
    }

    @Test
    void getFileData() {
    }
}
