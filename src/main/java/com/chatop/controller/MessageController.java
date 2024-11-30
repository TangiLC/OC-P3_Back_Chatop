package com.chatop.controller;

import com.chatop.dto.MessageResponseDTO;
import com.chatop.dto.RentalsResponseDTO;
import com.chatop.model.Message;
import com.chatop.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing messages.
 */
@RestController
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
   * @param payload A map containing the message content, user ID, and rental ID.
   * @param authentication The current authenticated user.
   * @return A ResponseEntity containing a MessageResponseDTO with the created message content.
   */
  @Operation(
    summary = "Create a Message",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponse(
    responseCode = "200",
    description = "Message successfully sent",
    content = @Content(
      mediaType = "application/json",
      examples = @ExampleObject(value = "{\"message\": \"success\"}")
    )
  )
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @PostMapping
  public ResponseEntity<?> createMessage(
    @RequestBody Map<String, Object> payload,
    Authentication authentication
  ) {
    try {
      // Retrieve the authenticated user's email (or username)
      String currentUserEmail = authentication.getName();

      // Debugging purpose: Print the authenticated user and payload
      System.out.println("Authenticated user: " + currentUserEmail);
      System.out.println("Payload received: " + payload);

      // Extract the parameters
      String messageContent = (String) payload.get("message");
      Integer userId = (Integer) payload.get("user_id");
      Integer rentalId = (Integer) payload.get("rental_id");

      messageService.createMessage(messageContent, userId, rentalId);

      return ResponseEntity.ok(
        new MessageResponseDTO("Message sent successfully")
      );
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
  /*  TO DO : upgrade messages routes
  /**
   * Retrieves all messages sent by a specific user.
   *
   * @param userId The ID of the user.
   * @return A ResponseEntity containing a list of messages sent by the user.
  
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Message>> readMessagesByUserId(
    @PathVariable Integer userId
  ) {
    try {
      List<Message> messages = messageService.readMessagesByUserId(userId);
      return ResponseEntity.ok(messages);
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(null);
    }
  }

  /**
   * Retrieves all messages related to a specific rental.
   *
   * @param rentalId The ID of the rental.
   * @return A ResponseEntity containing a list of messages related to the rental.
   
  @GetMapping("/rental/{rentalId}")
  public ResponseEntity<List<Message>> readMessagesByRentalId(
    @PathVariable Integer rentalId
  ) {
    try {
      List<Message> messages = messageService.readMessagesByRentalId(rentalId);
      return ResponseEntity.ok(messages);
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(null);
    }
  }     */
}
