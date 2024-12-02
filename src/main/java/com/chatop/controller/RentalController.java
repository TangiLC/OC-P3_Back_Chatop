package com.chatop.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.dto.RentalsResponseDTO;
import com.chatop.dto.UserDTO;
import com.chatop.service.RentalService;
import com.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for managing rental-related operations such as retrieval, creation, and updates.
 */
@RestController
@Tag(
  name = "2. Rental Controller",
  description = "Create or modify a rental item, get one particular item or whole list"
)
@RequestMapping("/api")
public class RentalController {

  private final RentalService rentalService;
  private final UserService userService;

  /**
   * Constructs the RentalController.
   *
   * @param rentalService The service for managing rentals.
   * @param userService The service for managing user.
   */
  public RentalController(
    RentalService rentalService,
    UserService userService
  ) {
    this.rentalService = rentalService;
    this.userService = userService;
  }

  /**
   * Retrieves a list of all rentals as DTOs.
   *
   * @param authentication The Authentication object containing the user's details.
   * @return A ResponseEntity containing a list of RentalDTOs.
   */
  @Operation(
    //security = {},
    summary = "Get list of rentals",
    description = "📚Retrieve list of rentals from database."
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "👍List retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RentalsResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌Bad Request",
        content = @Content(mediaType = "text/plain")
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
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )
  @GetMapping("/rentals")
  public ResponseEntity<RentalsResponseDTO> getAllRentals(
    Authentication authentication
  ) {
    String userEmail = authentication.getName();

    List<RentalDTO> rentalDTOs = rentalService.readAllRentalsAsDTO();
    RentalsResponseDTO rentalsObject = new RentalsResponseDTO(rentalDTOs);

    return ResponseEntity.ok(rentalsObject);
  }

  /**
   * Retrieves the details of a specific rental by its ID.
   *
   * @param id             The ID of the rental to retrieve.
   * @param authentication The Authentication object containing the user's details.
   * @return A ResponseEntity containing the RentalDTO of the specified rental.
   */
  @Operation(
    //security = {},
    summary = "Get the rental with id {id}",
    description = "🏠Retrieve data about the rental #id."
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "👍Rental data retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RentalsResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌Bad Request",
        content = @Content(mediaType = "text/plain")
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
        responseCode = "404",
        description = "🤔Data not found",
        content = @Content(mediaType = "text/plain")
      ),
      @ApiResponse(
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )
  @GetMapping("/rentals/{id}")
  public ResponseEntity<RentalDTO> getRentalById(
    @PathVariable Integer id,
    Authentication authentication
  ) {
    String userEmail = authentication.getName();

    RentalDTO rentalDTO = rentalService.readRentalAsDTO(id);

    return ResponseEntity.ok(rentalDTO);
  }

  /**
   * Handles the creation of a new rental.
   *
   * @param rentalRequestDTO The rental data from the request.
   * @param authentication   The Authentication object containing the user's details.
   * @return A ResponseEntity indicating success or failure.
   */
  @Operation(
    //security = {},
    summary = "Register a new rental",
    description = """
        🆕Registers a new rental (name, email, password).
        \nid will be auto incremented before saving data in database. 
        \nform-data required fields must be empty.""",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Register request with name, surface, price, picture url and description",
      required = true,
      content = @Content(
        mediaType = "multipart/form-data",
        schema = @Schema(implementation = RentalRequestDTO.class)
      )
    )
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "👍Rental created successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RentalsResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌Field missing",
        content = @Content(mediaType = "text/plain")
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
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )
  @PostMapping(
    value = "/rentals",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<Object> createRental(
    @ModelAttribute RentalRequestDTO rentalRequestDTO,
    Authentication authentication
  ) {
    try {
      String ownerEmail = authentication.getName();
      rentalService.createRental(rentalRequestDTO, ownerEmail);

      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Collections.singletonMap("message", "Rental created!"));
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
          Collections.singletonMap(
            "error",
            "Error while creating rental: " + e.getMessage()
          )
        );
    }
  }

  /**
   * Updates an existing rental by its ID.
   *
   * @param id               The ID of the rental to update.
   * @param rentalRequestDTO The DTO containing the updated details.
   * @param authentication   The Authentication object containing the user's details.
   * @return A ResponseEntity indicating success or failure.
   */
  @Operation(
    //security = {},
    summary = "Updating rental {id}",
    description = "📝Updates rental in database using id.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Update request with name, surface, price, picture url and description",
      required = true,
      content = @Content(
        mediaType = "multipart/form-data",
        schema = @Schema(implementation = RentalRequestDTO.class)
      )
    )
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "👍Rental updated successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RentalsResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "❌Field missing",
        content = @Content(mediaType = "text/plain")
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
        responseCode = "500",
        description = "🔧Internal server error",
        content = @Content(mediaType = "text/plain")
      ),
    }
  )
  @PutMapping(
    value = "/rentals/{id}",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<Object> updateRental(
    @PathVariable Integer id,
    @ModelAttribute RentalRequestDTO rentalRequestDTO,
    Authentication authentication
  ) {
    try {
      String ownerEmail = authentication.getName();
      UserDTO userDTO = userService.readUserByEmailAsDTO(ownerEmail);

      rentalService.updateRental(id, rentalRequestDTO, userDTO.getId());

      return ResponseEntity
        .status(HttpStatus.OK)
        .body(Collections.singletonMap("message", "Rental updated!"));
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
          Collections.singletonMap(
            "error",
            "Error while updating rental: " + e.getMessage()
          )
        );
    }
  }
}
