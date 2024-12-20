package com.chatop.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatop.dto.LoginRequestDTO;
import com.chatop.dto.UserDTO;
import com.chatop.dto.UserRequestDTO;
import com.chatop.exception.InvalidInputException;
import com.chatop.exception.ResourceNotFoundException;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import com.chatop.util.JwtUtil;

/**
 * Service for managing user-related operations.
 */
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public UserService(
    UserRepository userRepository,
    PasswordEncoder passwordEncoder,
    JwtUtil jwtUtil
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  /**
   * Creates a new user and hashes the password before saving.
   *
   * @param userRequestDTO The DTO containing user details for registration.
   * @return The created user entity.
   * @throws InvalidInputException If the email already exists.
   */
  public User createUser(UserRequestDTO userRequestDTO) {
    if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
      throw new InvalidInputException(
        "Email already exists: " + userRequestDTO.getEmail()
      );
    }

    User user = new User();
    user.setName(userRequestDTO.getName().trim());
    user.setEmail(userRequestDTO.getEmail().trim());
    user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
    user.setRole("ROLE_USER");
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    return userRepository.save(user);
  }

  /**
   * Retrieves a user by email and converts it into a UserDTO.
   *
   * @param email The email of the user to retrieve.
   * @return A UserDTO containing the user's details.
   * @throws ResourceNotFoundException If no user is found with the given email.
   */
  public UserDTO readUserByEmailAsDTO(String email) {
    return userRepository
      .findByEmail(email)
      .map(UserDTO::fromEntity)
      .orElseThrow(() ->
        new ResourceNotFoundException("User not found with email: " + email)
      );
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The user entity.
   * @throws ResourceNotFoundException If no user is found with the given ID.
   */
  public User readUserById(Integer id) {
    return userRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("User not found with ID: " + id)
      );
  }

  /**
   * Authenticates a user and generates a JWT token upon success.
   *
   * @param loginRequestDTO The login credentials.
   * @return The generated JWT token if authentication succeeds.
   * @throws ResourceNotFoundException If the email is not found.
   * @throws InvalidInputException If the password is invalid.
   */
  public String authenticateUser(LoginRequestDTO loginRequestDTO) {
    User user = userRepository
      .findByEmail(loginRequestDTO.getEmail())
      .orElseThrow(() ->
        new ResourceNotFoundException(
          "No user found with email: " + loginRequestDTO.getEmail()
        )
      );

    if (
      !passwordEncoder.matches(
        loginRequestDTO.getPassword(),
        user.getPassword()
      )
    ) {
      throw new InvalidInputException(
        "Invalid password for email: " + loginRequestDTO.getEmail()
      );
    }

    return jwtUtil.generateToken(user.getEmail(), user.getRole());
  }

  // TO DO Update User's details Method ?
  // TO DO Delete User Method ?

}
