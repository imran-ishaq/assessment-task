package com.assignment.parsing.controller;

import com.assignment.parsing.dto.EventDTO;
import com.assignment.parsing.entity.FileDetails;
import com.assignment.parsing.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/file")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileStorageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
    @GetMapping("/fetch")
    public List<FileDetails> getFileDetails() {
        return fileStorageService.getFileDetails();
    }
    @GetMapping("/data/{id}")
    public EventDTO getFileData(@PathVariable("id") Long id) {
        return fileStorageService.getFileData(id);
    }
}
