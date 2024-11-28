package com.chatop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.model.Message;
import com.chatop.service.MessageService;

/**
 * Controller for managing message-related operations.
 */
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
     * @param message The message object containing the details of the message to be created.
     * @return The created message object.
     */
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);
    }

    /**
     * Retrieves a message by its ID.
     *
     * @param id The ID of the message to retrieve.
     * @return The message object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer id) {
        Message message = messageService.readMessageById(id);
        return ResponseEntity.ok(message);
    }

    /**
     * Retrieves all messages sent by a specific user.
     *
     * @param userId The ID of the user whose messages to retrieve.
     * @return A list of messages sent by the user.
     */
    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<Message>> getMessagesByUserId(@PathVariable("user_id") Integer userId) {
        List<Message> messages = messageService.readMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Retrieves all messages related to a specific rental.
     *
     * @param rentalId The ID of the rental whose messages to retrieve.
     * @return A list of messages related to the rental.
     */
    @GetMapping("/rental/{rental_id}")
    public ResponseEntity<List<Message>> getMessagesByRentalId(@PathVariable("rental_id") Integer rentalId) {
        List<Message> messages = messageService.readMessagesByRentalId(rentalId);
        return ResponseEntity.ok(messages);
    }
}
