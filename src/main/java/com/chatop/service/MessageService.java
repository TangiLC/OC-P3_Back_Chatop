package com.chatop.service;

import java.time.LocalDateTime;
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

    public Message createMessage(Message message) {
        if (message.getMessage() == null || message.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content is required");
        }
        if (message.getRental() == null) {
            throw new IllegalArgumentException("Rental is required");
        }
        if (message.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }

        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public Message readMessageById(Integer id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    public List<Message> readMessagesByRentalId(Integer rentalId) {
        return messageRepository.findByRentalId(rentalId);
    }

    public List<Message> readMessagesByUserId(Integer userId) {
        return messageRepository.findByUserId(userId);
    }

    public Message updateMessage(Integer id, Message updatedMessage) {
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));

        if (updatedMessage.getMessage() != null && !updatedMessage.getMessage().trim().isEmpty()) {
            existingMessage.setMessage(updatedMessage.getMessage());
        }

        existingMessage.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(existingMessage);
    }

    public void deleteMessageById(Integer id) {
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
    }
}
