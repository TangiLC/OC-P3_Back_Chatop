package com.chatop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chatop.model.Message;
import com.chatop.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Creates a new message.
     *
     * @param message The message object to create.
     * @return The created message.
     */
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    /**
     * Retrieves a message by its ID.
     *
     * @param id The ID of the message to retrieve.
     * @return The message object.
     */
    public Message readMessageById(Integer id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + id));
    }

    /**
     * Retrieves all messages sent by a specific user.
     *
     * @param userId The ID of the user whose messages to retrieve.
     * @return A list of messages sent by the user.
     */
    public List<Message> readMessagesByUserId(Integer userId) {
        return messageRepository.findByUserId(userId);
    }

    /**
     * Retrieves all messages related to a specific rental.
     *
     * @param rentalId The ID of the rental whose messages to retrieve.
     * @return A list of messages related to the rental.
     */
    public List<Message> readMessagesByRentalId(Integer rentalId) {
        return messageRepository.findByRentalId(rentalId);
    }
}
