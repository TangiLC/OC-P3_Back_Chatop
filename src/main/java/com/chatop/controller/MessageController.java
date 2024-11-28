package com.chatop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.model.Message;
import com.chatop.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Creates a new message.
     *
     * @param payload The request payload containing the message content, user ID, and rental ID.
     * @return The created message.
     */
    @PostMapping("/")
    public ResponseEntity<Message> createMessage(@RequestBody Map<String, Object> payload) {
        try {
            // Extract parameters from the payload
            String messageContent = (String) payload.get("message");
            Integer userId = (Integer) payload.get("user_id");
            Integer rentalId = (Integer) payload.get("rental_id");

            // Call service to create the message
            Message message = messageService.createMessage(messageContent, userId, rentalId);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            // Bad Request for invalid data
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            // Not Found for invalid user or rental IDs
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            // Internal Server Error for unexpected issues
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Retrieves a message by its ID.
     *
     * @param id The ID of the message.
     * @return The requested message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Message> readMessageById(@PathVariable Integer id) {
        try {
            Message message = messageService.readMessageById(id);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            // Not Found for invalid message ID
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Retrieves all messages sent by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of messages sent by the user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Message>> readMessagesByUserId(@PathVariable Integer userId) {
        try {
            List<Message> messages = messageService.readMessagesByUserId(userId);
            return ResponseEntity.ok(messages);
        } catch (RuntimeException e) {
            // Not Found for invalid user ID
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Retrieves all messages related to a specific rental.
     *
     * @param rentalId The ID of the rental.
     * @return A list of messages related to the rental.
     */
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<List<Message>> readMessagesByRentalId(@PathVariable Integer rentalId) {
        try {
            List<Message> messages = messageService.readMessagesByRentalId(rentalId);
            return ResponseEntity.ok(messages);
        } catch (RuntimeException e) {
            // Not Found for invalid rental ID
            return ResponseEntity.status(404).body(null);
        }
    }
}
