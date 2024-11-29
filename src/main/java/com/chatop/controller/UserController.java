package com.chatop.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.dto.LoginRequestDTO;
import com.chatop.dto.LoginResponseDTO;
import com.chatop.dto.UserDTO;
import com.chatop.dto.UserRequestDTO;
import com.chatop.model.User;
import com.chatop.service.UserService;
import com.chatop.util.JwtUtil;

import jakarta.validation.Valid;

/**
 * Controller for managing user-related operations such as registration, login, and profile retrieval.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Constructs the UserController.
     *
     * @param userService The service for managing users.
     */
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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
     * @param loginRequestDTO The DTO containing the user's login credentials.
     * @return A ResponseEntity with a jwt token if the login is successful.
     */
   @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDTO> loginUser(
        @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String token = userService.authenticateUser(loginRequestDTO);
        
        //String token = "jwt";
        return ResponseEntity.ok(new LoginResponseDTO(token));
       
    }

    /**
     * Retrieves the currently authenticated user's profile.
     *
     * @param email The email of the authenticated user (simulated for now).
     * @return A ResponseEntity with the user's details as a DTO.
     */
    @GetMapping("/auth/me")
public ResponseEntity<?> getAuthenticatedUser(
    @RequestHeader("Authorization") String authorizationHeader) {
    
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body("Authorization header missing or invalid format. Expected 'Bearer <token>'");
    }

    String token = authorizationHeader.substring(7); // -"Bearer " 
    if (!jwtUtil.validateToken(token)) {
        Date expDate = jwtUtil.extractExpiration(token);
        String errorMessage = String.format("Invalid or expired token: '%s'-'%s'", token,expDate);
        return ResponseEntity.status(401).body(errorMessage);
    }
    String email= jwtUtil.extractEmail(token);
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
