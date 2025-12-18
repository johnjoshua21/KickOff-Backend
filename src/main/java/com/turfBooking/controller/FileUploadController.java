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
@CrossOrigin(origins = "*", allowCredentials = "false")  // Changed for testing
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.upload-dir:uploads/turfs}")
    private String uploadDir;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Received file: " + file.getOriginalFilename() + ", Size: " + file.getSize());

            String filename = fileStorageService.storeFile(file);
            String fileUrl = "/api/files/" + filename;

            Map<String, String> response = new HashMap<>();
            response.put("fileName", filename);
            response.put("fileUrl", fileUrl);
            response.put("message", "File uploaded successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/upload-multiple")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        try {
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
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload files: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // CRITICAL FIX: This endpoint serves the actual image files
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            System.out.println("Attempting to serve file: " + filename);
            System.out.println("Upload directory: " + uploadDir);

            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            System.out.println("Full file path: " + filePath.toAbsolutePath());

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                System.err.println("File not found: " + filePath.toAbsolutePath());
                return ResponseEntity.notFound().build();
            }

            if (!resource.isReadable()) {
                System.err.println("File not readable: " + filePath.toAbsolutePath());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String contentType = "application/octet-stream";

            // Try to determine file content type
            try {
                contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    // Default to image types based on extension
                    String lowerFilename = filename.toLowerCase();
                    if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
                        contentType = "image/jpeg";
                    } else if (lowerFilename.endsWith(".png")) {
                        contentType = "image/png";
                    } else if (lowerFilename.endsWith(".gif")) {
                        contentType = "image/gif";
                    } else if (lowerFilename.endsWith(".webp")) {
                        contentType = "image/webp";
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not determine content type: " + e.getMessage());
                contentType = "image/jpeg"; // Default fallback
            }

            System.out.println("Serving file with content type: " + contentType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .body(resource);
        } catch (Exception e) {
            System.err.println("Error serving file: " + e.getMessage());
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