package com.chatop.service;

import com.chatop.exception.FileStorageException;
import com.chatop.exception.FileValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for handling the storage of image files.
 */
@Service
public class ImageStorageService {

  @Value("${image.storage.directory:src/main/resources/static/pictures/}")
  private String storageDirectory;

  @Value("${image.base.url:http://localhost:8080/images/}")
  private String baseUrl;

  /**
   * Saves an image file to the configured storage directory and generates its URL.
   *
   * @param file The image file to save.
   * @return The public URL of the saved image.
   * @throws FileValidationException If the file is invalid.
   * @throws FileStorageException    If an error occurs while saving the file.
   */
  public String saveImage(MultipartFile file) {
    if (file.isEmpty()) {
      throw new FileValidationException("File is empty");
    }
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new FileValidationException(
        "The file must be an image. Detected type: " + contentType
      );
    }

    String extension = getFileExtension(file.getOriginalFilename());
    String uniqueFileName = UUID.randomUUID() + "." + extension;

    Path directoryPath = Paths.get(storageDirectory);
    try {
      if (!Files.exists(directoryPath)) {
        Files.createDirectories(directoryPath);
      }
    } catch (IOException e) {
      throw new FileStorageException(
        "Failed to create storage directory: " + directoryPath,
        e
      );
    }

    Path filePath = directoryPath.resolve(uniqueFileName);
    try (InputStream inputStream = file.getInputStream()) {
      Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new FileStorageException(
        "Failed to save the file: " + filePath.toAbsolutePath(),
        e
      );
    }
    return baseUrl + uniqueFileName;
  }

  /**
   * Extracts the file extension from a file name.
   *
   * @param fileName The file name.
   * @return The file extension (e.g., "jpg").
   * @throws FileValidationException If the file name is invalid.
   */
  private String getFileExtension(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      throw new FileValidationException("Invalid file name: " + fileName);
    }
    return fileName.substring(fileName.lastIndexOf(".") + 1);
  }
}
