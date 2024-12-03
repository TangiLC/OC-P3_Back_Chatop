package com.chatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for serving image files with authentication.
 */
@RestController
@Tag(
  name = "* Image Controller",
  description = "Serve authenticated image requests"
)
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
  @Operation(
    summary = "Get an image file",
    description = """
            📸Retrieve an image file based on its filename.
            This endpoint requires authentication via Bearer JWT token.
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
      /*@ApiResponse(
        responseCode = "401",
        description = "🧙‍♂️Unauthorized (no token)",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "403",
        description = "🧙‍♂️Forbidden (no role)",
        content = @Content(mediaType = "text/plain")
      ),*/
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
  )
  @GetMapping("/{filename}")
  public ResponseEntity<?> getImage(
    @PathVariable String filename
    //Authentication authentication
  ) {
    try {
      //String userEmail = authentication.getName();

      Path filePath = Paths.get(IMAGE_DIRECTORY).resolve(filename);
      Resource resource = new UrlResource(filePath.toUri());

      if (!resource.exists() || !resource.isReadable()) {
        return ResponseEntity
          .status(404)
          .body(Map.of("error", "Image not found"));
      }

      return ResponseEntity
        .ok()
        .header(
          HttpHeaders.CONTENT_DISPOSITION,
          "inline; filename=\"" + filename + "\""
        )
        .body(resource);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity
        .status(500)
        .body(Map.of("error", "An unexpected error occurred"));
    }
  }
}
