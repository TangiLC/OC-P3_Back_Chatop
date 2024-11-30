package com.chatop.controller;

import com.chatop.dto.LoginRequestDTO;
import com.chatop.dto.LoginResponseDTO;
import com.chatop.dto.UserDTO;
import com.chatop.dto.UserRequestDTO;
import com.chatop.model.User;
import com.chatop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing user-related operations
 *  such as registration, login, and profile retrieval.
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
  public ResponseEntity<UserDTO> registerUser(
    @Valid @RequestBody UserRequestDTO userRequestDTO
  ) {
    User createdUser = userService.createUser(userRequestDTO);
    UserDTO userDTO = UserDTO.fromEntity(createdUser);
    return ResponseEntity.ok(userDTO);
  }

  /**
   * Logs in a user and returns a JWT token upon successful authentication.
   *
   * @param loginRequestDTO The DTO containing the user's login credentials.
   * @return A ResponseEntity with a JWT token if the login is successful.
   */
  @PostMapping("/auth/login")
  public ResponseEntity<LoginResponseDTO> loginUser(
    @Valid @RequestBody LoginRequestDTO loginRequestDTO
  ) {
    String token = userService.authenticateUser(loginRequestDTO);
    return ResponseEntity.ok(new LoginResponseDTO(token));
  }

  /**
   * Retrieves the currently authenticated user's profile using Spring Security's Authentication.
   *
   * @param authentication The Authentication object injected by Spring Security.
   * @return A ResponseEntity with the user's details as a DTO.
   */
  @GetMapping("/auth/me")
  public ResponseEntity<UserDTO> getAuthenticatedUser(
    Authentication authentication
  ) {
    // Extract the email (or principal) of the authenticated user
    String email = authentication.getName();

    // Retrieve user details and convert them to a DTO
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
