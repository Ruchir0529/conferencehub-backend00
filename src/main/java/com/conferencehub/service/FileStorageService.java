package com.conferencehub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * FileStorageService — stores uploaded PDF/JPEG files on disk.
 * Files are saved to the configured upload directory with UUID names to avoid collisions.
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directory: " + uploadDir, ex);
        }
    }

    /**
     * Save a multipart file and return the generated filename.
     * Only PDF and JPEG/JPG files are accepted.
     */
    public String storeFile(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("application/pdf") &&
                 !contentType.equals("image/jpeg") &&
                 !contentType.equals("image/jpg"))) {
            throw new RuntimeException("Only PDF and JPEG files are allowed.");
        }

        // Generate unique filename
        String extension = originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String storedFilename = UUID.randomUUID() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file: " + originalFilename, ex);
        }
    }

    /**
     * Load a file as a Path for download.
     */
    public Path loadFile(String filename) {
        return fileStorageLocation.resolve(filename).normalize();
    }

    /**
     * Delete a stored file by its stored filename.
     */
    public void deleteFile(String filename) {
        try {
            Path filePath = fileStorageLocation.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file: " + filename, ex);
        }
    }
}
