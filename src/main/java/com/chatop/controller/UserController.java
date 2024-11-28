package com.chatop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.dto.UserDTO;
import com.chatop.dto.UserRequestDTO;
import com.chatop.model.User;
import com.chatop.service.UserService;

import jakarta.validation.Valid;

/**
 * Controller for managing user-related operations such as registration, login, and profile retrieval.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * Constructs the UserController.
     *
     * @param userService The service for managing users.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param userRequestDTO The DTO containing the user's registration details.
     * @return A ResponseEntity with the created user's details as a DTO.
     */
    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User createdUser = userService.createUser(userRequestDTO);
        UserDTO userDTO = UserDTO.fromEntity(createdUser);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Logs in a user.
     * (For simplicity, this method just validates user credentials without issuing a token.)
     *
     * @param userRequestDTO The DTO containing the user's login credentials.
     * @return A ResponseEntity with a success message if the login is successful.
     */
    @PostMapping("/auth/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            boolean isAuthenticated = userService.authenticateUser(userRequestDTO);
            if (isAuthenticated) {
                return ResponseEntity.ok("Login successful");
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(ex.getMessage());
        }
        return ResponseEntity.status(401).body("Invalid email or password");
    }

    /**
     * Retrieves the currently authenticated user's profile.
     *
     * @param email The email of the authenticated user (simulated for now).
     * @return A ResponseEntity with the user's details as a DTO.
     */
    @GetMapping("/auth/me")
    public ResponseEntity<UserDTO> getAuthenticatedUser(@RequestParam String email) {
        UserDTO userDTO = userService.readUserByEmailAsDTO(email);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Retrieves the profile of a specific user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A ResponseEntity with the user's details as a DTO.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        User user = userService.readUserById(id);
        UserDTO userDTO = UserDTO.fromEntity(user);
        return ResponseEntity.ok(userDTO);
    }
}
