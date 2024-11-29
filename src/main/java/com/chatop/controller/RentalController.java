package com.chatop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
   * Creates a new rental.
   *
   * @param rentalRequestDTO The DTO containing the details of the rental to be created.
   * @return A ResponseEntity containing the created RentalDTO.
   */
  @PostMapping(value="/rentals", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createRental(
    @RequestPart("name") String name,
    @RequestPart("surface") Integer surface,
    @RequestPart("price") Integer price,
    @RequestPart("picture") MultipartFile picture,
    @RequestPart(value="description",required=false) String description,
    @RequestHeader HttpHeaders headers
  ) {
    try {
      String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
      if (
        authorizationHeader == null ||
        !authorizationHeader.startsWith("Bearer ")
      ) {
        return ResponseEntity
          .status(401)
          .body("Missing or invalid authorization token");
      }

      String token = authorizationHeader.substring(7);
      if (!jwtUtil.validateToken(token)) {
        return ResponseEntity.status(401).body("Invalid or expired token");
      }
      String email = jwtUtil.extractEmail(token);
      String pictureUrl = null;
        if (picture != null && !picture.isEmpty()) {
            pictureUrl = picture.getOriginalFilename(); 
        }
      
      RentalRequestDTO rentalRequestDTO = new RentalRequestDTO();
      rentalRequestDTO.setName(name);
      rentalRequestDTO.setSurface(surface);
      rentalRequestDTO.setPrice(price);
      rentalRequestDTO.setDescription(description);
      rentalRequestDTO.setPicture(pictureUrl);

      //Rental rental = 
      rentalService.createRental(rentalRequestDTO, email);
      
      //RentalDTO rentalDTO = RentalDTO.fromEntity(rental);

      return ResponseEntity.ok(Map.of("message", "Rental created!"));

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("An unexpected error occurred");
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
