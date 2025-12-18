package com.turfBooking.controller;

import com.turfBooking.service.implementation.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.upload-dir:uploads/turfs}")
    private String uploadDir;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // ADDED: Log for debugging
            System.out.println("Received file: " + file.getOriginalFilename() + ", Size: " + file.getSize());

            String filename = fileStorageService.storeFile(file);
            String fileUrl = "/api/files/" + filename;

            Map<String, String> response = new HashMap<>();
            response.put("fileName", filename);
            response.put("fileUrl", fileUrl);
            response.put("message", "File uploaded successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // ADDED: Print stack trace for debugging
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // FIXED: Ensure this endpoint correctly handles the "files" parameter
    @PostMapping("/upload-multiple")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            // ADDED: Log for debugging
            System.out.println("Received " + files.length + " files");

            List<Map<String, String>> uploadedFiles = new ArrayList<>();

            for (MultipartFile file : files) {
                System.out.println("Processing file: " + file.getOriginalFilename() + ", Size: " + file.getSize());

                String filename = fileStorageService.storeFile(file);
                String fileUrl = "/api/files/" + filename;

                Map<String, String> fileInfo = new HashMap<>();
                fileInfo.put("fileName", filename);
                fileInfo.put("fileUrl", fileUrl);
                uploadedFiles.add(fileInfo);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("files", uploadedFiles);
            response.put("message", "Files uploaded successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // ADDED: Print stack trace for debugging
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload files: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType = "application/octet-stream";

                // Try to determine file content type
                try {
                    contentType = Files.probeContentType(filePath);
                    if (contentType == null) {
                        // Default to image types
                        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                            contentType = "image/jpeg";
                        } else if (filename.toLowerCase().endsWith(".png")) {
                            contentType = "image/png";
                        } else if (filename.toLowerCase().endsWith(".gif")) {
                            contentType = "image/gif";
                        } else if (filename.toLowerCase().endsWith(".webp")) {
                            contentType = "image/webp";
                        }
                    }
                } catch (IOException e) {
                    contentType = "image/jpeg"; // Default fallback
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
                        // CORS headers already handled by CorsConfig and @CrossOrigin
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{filename:.+}")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteFile(@PathVariable String filename) {
        try {
            fileStorageService.deleteFile(filename);
            Map<String, String> response = new HashMap<>();
            response.put("message", "File deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}