package com.chatop.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.chatop.model.User;
import com.chatop.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user.
     *
     * @param user The user object to create.
     * @return The created user.
     */
    public User createUser(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format: " + user.getEmail());
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    
        user.setName(user.getName().trim());
        user.setPassword(user.getPassword().trim());  // TO DO : Encrypt password
    
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return The user object with sensitive data excluded.
     */
    public User readUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        user.setPassword(null); // Exclude password from the response
        return user;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user object.
     */
    public User readUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    /**
     * Updates a user's details.
     *
     * @param email The email of the user to update.
     * @param updatedData The updated user data.
     * @return The updated user object.
     */
    public User updateUser(String email, User updatedData) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        // Update only non-null fields
        if (updatedData.getName() != null) {
            user.setName(updatedData.getName().trim());
        }
        if (updatedData.getPassword() != null) {
            user.setPassword(updatedData.getPassword().trim()); // TO DO : Encrypt password
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their email.
     *
     * @param email The email of the user to delete.
     */
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        userRepository.delete(user);
    }

    /**
     * Validates an email format.
     *
     * @param email The email to validate.
     * @return True if the email format is valid, otherwise false.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
