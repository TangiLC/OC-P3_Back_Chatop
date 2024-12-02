package com.chatop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.dto.LoginRequestDTO;
import com.chatop.dto.LoginResponseDTO;
import com.chatop.dto.UserDTO;
import com.chatop.dto.UserRequestDTO;
import com.chatop.model.User;
import com.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller for managing user-related operations
 *  such as registration, login, and profile retrieval.
 */
@RestController
@RequestMapping("/api")
@Tag(
  name = "1. User Controller",
  description = "Registration, login, and profile retrieval."
)
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
  @Operation(
    security = {},
    summary = "Register a new user",
    description = """
        🆕Registers a new user (name, email, password). Returns JWT Token on success.
        \nemail syntax must contain *"@domain.suffix"* to be valid. 
        \n⚙️password will be hashed (Spring security Bcrypt) before saving in database""",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Register request with name, email and password",
      required = true,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
                { "name":"newUser Test",
                  "email": "user@example.com",
                  "password": "password123!"
                }
                """
        )
      )
    )
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "👍User created successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = LoginResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌User name, email & password required",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "404",
        description = "🤔User not found",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )
  @PostMapping("/auth/register")
  public ResponseEntity<LoginResponseDTO> registerUser(
    @Valid @RequestBody UserRequestDTO userRequestDTO
  ) {
    User createdUser = userService.createUser(userRequestDTO);
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
    loginRequestDTO.setEmail(createdUser.getEmail());
    loginRequestDTO.setPassword(userRequestDTO.getPassword());
    String token = userService.authenticateUser(loginRequestDTO);
    return ResponseEntity.ok(new LoginResponseDTO(token));
  }

  /**
   * Logs in a user and returns a JWT token upon successful authentication.
   *
   * @param loginRequestDTO The DTO containing the user's login credentials.
   * @return A ResponseEntity with a JWT token if the login is successful.
   */
  @Operation(
    security = {},
    summary = "Login user",
    description = """
        🔐Log in user (email, password). Returns JWT Token on success.
        \nemail syntax must contain *"@domain.suffix"* to be valid. 
        \nCredentials (email,password) must be in database to be valid.""",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Login request with email and password",
      required = true,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
                { "email": "test@test.com",
                  "password": "test31!"
                }
                """
        )
      )
    )
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "👍User Logged successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = LoginResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌password must match database",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "404",
        description = "🕵🏻User not found in database",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )
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
  @Operation(
    //security = {},
    summary = "Get user info",
    description = """
       👤Retrieve user info from database using token as Authentication.
       \nemail is stored in token"""
    /*requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Login request with email and password",
      required = true,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
                { "email": "test@test.com",
                  "password": "test31!"
                }
                """
        )
      )
    )*/
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "👍User infos Retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "🧙‍♂️unauthorized",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌Bad Request",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )
  @GetMapping("/auth/me")
  public ResponseEntity<UserDTO> getAuthenticatedUser(
    Authentication authentication
  ) {
    String email = authentication.getName();

    UserDTO userDTO = userService.readUserByEmailAsDTO(email);
    return ResponseEntity.ok(userDTO);
  }

  /**
   * Retrieves the profile of a specific user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return A ResponseEntity with the user's details as a DTO.
   */
  @Operation(
    //security = {},
    summary = "Get infos about user {id}",
    description = """
       💁Retrieve user info from database using id.
       \nid is integer, auto-incremented when saving in database"""
    /*requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Login request with email and password",
      required = true,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
                { "email": "test@test.com",
                  "password": "test31!"
                }
                """
        )
      )
    )*/
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "👍User infos Retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "🧙‍♂️unauthorized (no token)",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "403",
        description = "🧙‍♂️forbidden (no role)",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌Bad Request",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )
  @GetMapping("/user/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
    User user = userService.readUserById(id);
    UserDTO userDTO = UserDTO.fromEntity(user);
    return ResponseEntity.ok(userDTO);
  }
}
