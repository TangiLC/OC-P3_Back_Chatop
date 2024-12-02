package com.chatop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chatop.model.Message;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.repository.MessageRepository;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;

@Service
public class MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final RentalRepository rentalRepository;

  public MessageService(
    MessageRepository messageRepository,
    UserRepository userRepository,
    RentalRepository rentalRepository
  ) {
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
    this.rentalRepository = rentalRepository;
  }

  /**
   * Creates a new message.
   *
   * @param messageContent The content of the message.
   * @param userId The ID of the user sending the message.
   * @param rentalId The ID of the rental associated with the message.
   * @return The created message.
   * @throws RuntimeException         If the user_id or rental_id cannot be found.
   * @throws IllegalArgumentException If the message content is null or empty.
   */
  public Message createMessage(
    String messageContent,
    Integer userId,
    Integer rentalId
  ) {
    User user = userRepository
      .findById(userId)
      .orElseThrow(() ->
        new RuntimeException("User not found with ID: " + userId)
      );
    Rental rental = rentalRepository
      .findById(rentalId)
      .orElseThrow(() ->
        new RuntimeException("Rental not found with ID: " + rentalId)
      );
    if (messageContent == null || messageContent.trim().isEmpty()) {
      throw new IllegalArgumentException(
        "Message content cannot be null or empty."
      );
    }

    Message message = new Message();
    message.setMessage(messageContent);
    message.setUser(user);
    message.setRental(rental);
    message.setCreatedAt(LocalDateTime.now());
    message.setUpdatedAt(LocalDateTime.now());
    
    return messageRepository.save(message);
  }


// TO DO : Display message... 

  /**
   * Retrieves a message by its ID.
   *
   * @param id The ID of the message to retrieve.
   * @return The message object.
   */
  public Message readMessageById(Integer id) {
    return messageRepository
      .findById(id)
      .orElseThrow(() ->
        new RuntimeException("Message not found with ID: " + id)
      );
  }

  /**
   * Retrieves all messages sent by a specific user.
   *
   * @param userId The ID of the user whose messages to retrieve.
   * @return A list of messages sent by the user.
   */
  public List<Message> readMessagesByUserId(Integer userId) {
    if (!userRepository.existsById(userId)) {
      throw new RuntimeException("User not found with ID: " + userId);
    }
    return messageRepository.findByUserId(userId);
  }

  /**
   * Retrieves all messages related to a specific rental.
   *
   * @param rentalId The ID of the rental whose messages to retrieve.
   * @return A list of messages related to the rental.
   */
  public List<Message> readMessagesByRentalId(Integer rentalId) {
    if (!rentalRepository.existsById(rentalId)) {
      throw new RuntimeException("Rental not found with ID: " + rentalId);
    }
    return messageRepository.findByRentalId(rentalId);
  }
}
