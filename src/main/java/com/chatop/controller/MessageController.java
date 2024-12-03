package com.chatop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.dto.MessageRequestDTO;
import com.chatop.dto.MessageResponseDTO;
import com.chatop.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller for managing messages.
 */
@RestController
@Tag(name = "3. Message Controller", description = "Create message")
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  /**
   * Constructs a MessageController.
   *
   * @param messageService The service for managing messages.
   */
  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  /**
   * Creates a new message. Authentication via Bearer JWT is handled by Spring Security.
   *
   * @param messageRequestDTO The DTO containing the message content, user ID, and rental ID.
   * @param authentication The current authenticated user.
   * @return A ResponseEntity containing a MessageResponseDTO with the created message content.
   */
  @Operation(
    summary = "Create a Message",
    description = """
            📬 Create a message from a user to a rental owner.
            """,
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Message creation payload",
      required = true,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
                        {
                            "message": "New message !",
                            "userId": 1,
                            "rentalId": 2
                        }
                        """
        )
      )
    )
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "📨 Message sent successfully",
        content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(value = "{\"message\": \"success\"}")
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌ Bad Request (key missing in body)",
        content = @Content(mediaType = "application/json")
      ),
      @ApiResponse(
        responseCode = "401",
        description = "🧙‍♂️ Unauthorized (no token)",
        content = @Content(mediaType = "application/json")
      ),
      @ApiResponse(
        responseCode = "403",
        description = "🧙‍♂️ Forbidden (no role)",
        content = @Content(mediaType = "application/json")
      ),
      @ApiResponse(
        responseCode = "500",
        description = "🔧 Internal server error",
        content = @Content(mediaType = "application/json")
      ),
    }
  )
  @PostMapping
  public ResponseEntity<MessageResponseDTO> createMessage(
    @Valid @RequestBody MessageRequestDTO messageRequestDTO,
    Authentication authentication
  ) {
    // Get the email of the authenticated user
    String userEmail = authentication.getName();

    // Delegate the creation to the service
    messageService.createMessage(
      messageRequestDTO.getMessage(),
      messageRequestDTO.getUserId(),
      messageRequestDTO.getRentalId()
    );

    // Return a success response
    return ResponseEntity.ok(
      new MessageResponseDTO("Message sent successfully")
    );
  }
}
