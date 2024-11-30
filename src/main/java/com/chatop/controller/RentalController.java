package com.chatop.controller;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.dto.RentalsResponseDTO;
import com.chatop.dto.UserDTO;
import com.chatop.service.RentalService;
import com.chatop.service.UserService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing rental-related operations such as retrieval, creation, and updates.
 */
@RestController
@RequestMapping("/api")
public class RentalController {

  private final RentalService rentalService;
    private final UserService userService;
  
    /**
     * Constructs the RentalController.
     *
     * @param rentalService The service for managing rentals.
     */
    public RentalController(RentalService rentalService, UserService userService) {
      this.rentalService = rentalService;
      this.userService = userService;
  }

  /**
   * Retrieves a list of all rentals as DTOs.
   *
   * @param authentication The Authentication object containing the user's details.
   * @return A ResponseEntity containing a list of RentalDTOs.
   */
  @GetMapping("/rentals")
  public ResponseEntity<RentalsResponseDTO> getAllRentals(
    Authentication authentication
  ) {
    // Retrieve the email of the authenticated user (optional, can be logged or used)
    String userEmail = authentication.getName();

    // Fetch all rentals
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
  @GetMapping("/rentals/{id}")
  public ResponseEntity<RentalDTO> getRentalById(
    @PathVariable Integer id,
    Authentication authentication
  ) {
    // Retrieve the email of the authenticated user (optional, can be logged or used)
    String userEmail = authentication.getName();

    // Fetch the rental by ID
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
  @PutMapping(value = "/rentals/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Object> updateRental(
          @PathVariable Integer id,
          @ModelAttribute RentalRequestDTO rentalRequestDTO,
          Authentication authentication) {
      try {
          String ownerEmail = authentication.getName();
          UserDTO userDTO = userService.readUserByEmailAsDTO(ownerEmail);

          rentalService.updateRental(id,rentalRequestDTO, userDTO.getId());
  
          return ResponseEntity
                  .status(HttpStatus.OK)
                  .body(Collections.singletonMap("message", "Rental updated!"));
      } catch (Exception e) {
          return ResponseEntity
                  .status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(Collections.singletonMap("error", "Error while updating rental: " + e.getMessage()));
      }
  }
}
