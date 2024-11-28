package com.chatop.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatop.dto.LoginRequestDTO;
import com.chatop.dto.UserDTO;
import com.chatop.dto.UserRequestDTO;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import com.chatop.util.JwtUtil;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 
    private final JwtUtil jwtUtil;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Creates a new user and hashes the password before saving.
     *
     * @param user The user object to create.
     * @return The created user.
     */
    public User createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + userRequestDTO.getEmail());
        }
    
        User user = new User();
        user.setName(userRequestDTO.getName().trim());
        user.setEmail(userRequestDTO.getEmail().trim());
        String hashedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        user.setPassword(hashedPassword);
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
 * Retrieves a user by their email and converts it into a UserDTO.
 *
 * @param email The email of the user to retrieve.
 * @return A UserDTO containing the user's details, excluding sensitive information.
 * @throws IllegalArgumentException if no user is found with the given email.
 */
public UserDTO readUserByEmailAsDTO(String email) {
    // Retrieve the user by email
    User user = userRepository.findByEmail(email);

    // Check if the user exists
    if (user == null) {
        throw new IllegalArgumentException("User not found with email: " + email);
    }

    // Convert the User entity to UserDTO and return
    return UserDTO.fromEntity(user);
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
     * Authenticates a user and generates a JWT token upon success.
     *
     * @param loginRequestDTO The login credentials.
     * @return The generated JWT token if authentication succeeds.
     * @throws IllegalArgumentException if email or password is invalid.
     */
    public String authenticateUser(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("No user found with email: " + loginRequestDTO.getEmail());
        }
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password for email: " + loginRequestDTO.getEmail());
        }
        return jwtUtil.generateToken(user.getEmail());
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

}
