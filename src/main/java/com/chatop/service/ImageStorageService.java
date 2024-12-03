package com.chatop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service for handling the storage of image files.
 */
@Service
public class ImageStorageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageStorageService.class);

    // Directory where images will be stored, configured in application.properties
    @Value("${image.storage.directory:src/main/resources/static/pictures/}")
    private String storageDirectory;

    // Base URL for accessing images, configured in application.properties
    @Value("${image.base.url:http://localhost:8080/images/}")
    private String baseUrl;

    /**
     * Saves an image file to the configured storage directory and generates its URL.
     *
     * @param file The image file to save.
     * @return The public URL of the saved image.
     * @throws IOException If an error occurs while saving the file.
     */
    public String saveImage(MultipartFile file) throws IOException {
        logger.info("Starting image save process...");

        // Validate that the file is not empty
        if (file.isEmpty()) {
            logger.error("Upload failed: File is empty.");
            throw new IllegalArgumentException("File is empty");
        }

        // Validate that the file is an image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            logger.error("Upload failed: File is not an image. Content type: {}", contentType);
            throw new IllegalArgumentException("The file must be an image");
        }

        // Generate a unique file name using UUID
        String extension = getFileExtension(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID() + "." + extension;
        logger.info("Generated unique file name: {}", uniqueFileName);

        // Ensure the storage directory exists
        Path directoryPath = Paths.get(storageDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
            logger.info("Storage directory created at: {}", directoryPath.toAbsolutePath());
        } else {
            logger.info("Using existing storage directory: {}", directoryPath.toAbsolutePath());
        }

        // Save the file to the storage directory
        Path filePath = directoryPath.resolve(uniqueFileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File saved successfully: {}", filePath.toAbsolutePath());
        } catch (IOException e) {
            logger.error("Error during file transfer: {}", e.getMessage(), e);
            throw new IOException("Failed to save the file: " + filePath.toAbsolutePath(), e);
        }

        // Generate and return the file's public URL
        String fileUrl = baseUrl + uniqueFileName;
        logger.info("File URL generated: {}", fileUrl);
        return fileUrl;
    }

    /**
     * Extracts the file extension from a file name.
     *
     * @param fileName The file name.
     * @return The file extension (e.g., "jpg").
     */
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
