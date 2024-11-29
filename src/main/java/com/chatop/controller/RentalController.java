package com.chatop.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.dto.RentalsResponseDTO;
import com.chatop.model.Rental;
import com.chatop.service.RentalService;
import com.chatop.util.JwtUtil;

import jakarta.validation.Valid;

/**
 * Controller for managing rental-related operations such as retrieval, creation, and updates.
 */
@RestController
@RequestMapping("/api")
public class RentalController {

  private final RentalService rentalService;
  private final JwtUtil jwtUtil;

  /**
   * Constructs the RentalController.
   *
   * @param rentalService The service for managing rentals.
   */
  public RentalController(
    RentalService rentalService,
    JwtUtil jwtUtil
  ) {
    this.rentalService = rentalService;
    this.jwtUtil = jwtUtil;
  }

  /**
   * Retrieves a list of all rentals as DTOs.
   *
   * @return A ResponseEntity containing a list of RentalDTOs.
   */
  @GetMapping("/rentals")
  public ResponseEntity<RentalsResponseDTO> getAllRentals() {
    List<RentalDTO> rentalDTOs = rentalService.readAllRentalsAsDTO();
    RentalsResponseDTO rentalsObject = new RentalsResponseDTO(rentalDTOs);
    return ResponseEntity.ok(rentalsObject);
  }

  /**
   * Retrieves the details of a specific rental by its ID.
   *
   * @param id The ID of the rental to retrieve.
   * @return A ResponseEntity containing the RentalDTO of the specified rental.
   */
  @GetMapping("/rentals/{id}")
  public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
    Rental rental = rentalService.readRental(id);
    RentalDTO rentalDTO = RentalDTO.fromEntity(rental);
    return ResponseEntity.ok(rentalDTO);
  }

  /**
     * Handles the creation of a new rental.
     *
     * @param rentalRequestDTO The rental data from the request.
     * @param headers          The HTTP headers containing the Authorization token.
     * @return A response indicating success or failure.
     */
    @PostMapping(value = "/rentals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createRental(
            @ModelAttribute RentalRequestDTO rentalRequestDTO,
            @RequestHeader HttpHeaders headers) {
        try {
            // Extract and validate the Authorization header
            String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body(Collections.singletonMap("error", "Missing or invalid authorization token"));
            }

            // Extract the token and validate it
            String token = authorizationHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401)
                        .body(Collections.singletonMap("error", "Invalid or expired token"));
            }

            // Extract owner email from the token
            String ownerEmail = jwtUtil.extractEmail(token);

            // Call the service to create the rental
            rentalService.createRental(rentalRequestDTO, ownerEmail);

            // Success response
            return new ResponseEntity<>(Collections.singletonMap("message", "Rental created!"), HttpStatus.CREATED);

        } catch (Exception e) {
            // Handle errors and return a proper response
            return new ResponseEntity<>(
                    Collections.singletonMap("error", "Error while creating rental: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  /**
   * Updates an existing rental by its ID.
   *
   * @param id The ID of the rental to update.
   * @param rentalRequestDTO The DTO containing the updated details.
   * @return A ResponseEntity containing the updated RentalDTO.
   */
  @PutMapping("/rentals/{id}")
  public ResponseEntity<RentalDTO> updateRental(
    @PathVariable Integer id,
    @Valid @RequestBody RentalRequestDTO rentalRequestDTO
  ) {
    Rental updatedRental = rentalService.updateRental(id, rentalRequestDTO);
    RentalDTO rentalDTO = RentalDTO.fromEntity(updatedRental);
    return ResponseEntity.ok(rentalDTO);
  }
}
