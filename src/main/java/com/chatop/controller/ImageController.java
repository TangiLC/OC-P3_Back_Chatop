package com.chatop.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * Controller for serving image files with authentication.
 */
@RestController
/*@Tag(
  name = "4. Image Controller",
  description = "Serve image requests"
)*/
@Hidden
@RequestMapping("/images")
public class ImageController {

  private static final String IMAGE_DIRECTORY =
    "src/main/resources/static/pictures/";

  /**
   * Serves an image file based on the authenticated user's request.
   *
   * @param filename       The name of the image file to retrieve.
   * @param authentication The current authenticated user.
   * @return A ResponseEntity containing the requested image or an appropriate error response.
   */
  /*@Operation(
    security={},
    summary = "Get an image file",
    description = """
            📸Retrieve an image file based on its filename.
            
            """
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "📷 Image served successfully",
        content = @Content(mediaType = "application/octet-stream")
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌Bad Request",
        content = @Content(mediaType = "application/json")
      ),
      @ApiResponse(
        responseCode = "404",
        description = "🔎Image not found",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )*/
  @GetMapping("/{filename}")
  public ResponseEntity<Resource> getImage(
    @PathVariable String filename
    //Authentication authentication
  ) {
    try {
      //String userEmail = authentication.getName();

      Path filePath = Paths.get(IMAGE_DIRECTORY).resolve(filename);
      Resource resource = new UrlResource(filePath.toUri());

      if (!resource.exists() || !resource.isReadable()) {
        throw new ResourceNotFoundException("Image not found: " + filename);
      }

      return ResponseEntity
        .ok()
        .header(
          HttpHeaders.CONTENT_DISPOSITION,
          "inline; filename=\"" + filename + "\""
        )
        .body(resource);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid filename: " + filename, e);
    } catch (Exception e) {
      throw new RuntimeException("Error retrieving image: " + filename, e);
    }
  }
}
