package com.chatop.controller;

import com.chatop.model.User;
import com.chatop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing user-related operations such as registration, login, and user details retrieval.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param user The user object containing registration details.
     * @return The created user.
     */
    @PostMapping("/auth/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Authenticates a user (login functionality).
     *
     * @param user The user object containing login credentials (email and password).
     * @return A placeholder message (JWT to be implemented later).
     */
    @PostMapping("/auth/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        return ResponseEntity.ok("Login functionality to be implemented");
    }

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @param email The email of the user (to be replaced by JWT-based identification).
     * @return The user object with sensitive data (e.g., password) excluded.
     */
    @GetMapping("/auth/me")
    public ResponseEntity<User> getUserDetails(@RequestParam String email) {
        User user = userService.readUser(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves the details of another user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user object.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.readUserById(id);
        return ResponseEntity.ok(user);
    }
}
