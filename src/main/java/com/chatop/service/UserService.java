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
        user.setPassword(user.getPassword().trim());  // TO DO : Security
    
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    
        return userRepository.save(user);
    }
    

    public User readUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        user.setPassword(null);
        return user;
    }

    public User updateUser(String email, User updatedData) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
    
        if (updatedData.getName() != null) {
            user.setName(updatedData.getName());
        }
        if (updatedData.getPassword() != null) {
            user.setPassword(updatedData.getPassword()); // TO DO : security
        }
    
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        userRepository.delete(user);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
