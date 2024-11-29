package com.chatop.controller;

import com.chatop.dto.MessageResponseDTO;
import com.chatop.model.Message;
import com.chatop.service.MessageService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api")
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
   * Creates a new message. Authentification via Bearer JWT
   *
   * @param payload A map containing the message content, user ID, and rental ID.
   * @param authorizationHeader The Authorization header with Bearer token.
   * @return A ResponseEntity containing a MessageResponseDTO with the created message content.
   */
  @PostMapping("/messages")
  public ResponseEntity<?> createMessage(
    @RequestBody Map<String, Object> payload
  ) {
    System.out.println("Create Message" + payload);
    try {
      // Récupérer le payload
      System.out.println("Payload received: " + payload);

      // Extraire les paramètres
      String messageContent = (String) payload.get("message");
      Integer userId = (Integer) payload.get("user_id");
      Integer rentalId = (Integer) payload.get("rental_id");

      messageService.createMessage(messageContent, userId, rentalId);

      return ResponseEntity.ok(
        new MessageResponseDTO("Message sent successfully")
      );
    } catch (IllegalArgumentException e) {
      return ResponseEntity
        .badRequest()
        .body(Map.of("error 1st catch", e.getMessage()));
    } catch (RuntimeException e) {
      return ResponseEntity
        .status(404)
        .body(Map.of("error 2nd catch", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity
        .status(500)
        .body(Map.of("error", "An unexpected error occurred"));
    }
  }

  /**
   * Retrieves all messages sent by a specific user.
   *
   * @param userId The ID of the user.
   * @return A ResponseEntity containing a list of messages sent by the user.
   */
  @GetMapping("/messages/user/{userId}")
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
   */
  @GetMapping("/messages/rental/{rentalId}")
  public ResponseEntity<List<Message>> readMessagesByRentalId(
    @PathVariable Integer rentalId
  ) {
    try {
      List<Message> messages = messageService.readMessagesByRentalId(rentalId);
      return ResponseEntity.ok(messages);
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(null);
    }
  }
}
