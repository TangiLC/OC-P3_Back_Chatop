package com.chatop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.model.Rental;
import com.chatop.service.RentalService;

import jakarta.validation.Valid;

/**
 * Controller for managing rental-related operations such as retrieval, creation, and updates.
 */
@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;

    /**
     * Constructs the RentalController.
     *
     * @param rentalService The service for managing rentals.
     */
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Retrieves a list of all rentals as DTOs.
     *
     * @return A ResponseEntity containing a list of RentalDTOs.
     */
    @GetMapping("/rentals")
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        List<RentalDTO> rentalDTOs = rentalService.readAllRentalsAsDTO();
        return ResponseEntity.ok(rentalDTOs);
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
    @PostMapping("/rentals/")
    public ResponseEntity<RentalDTO> createRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO) {
        Rental createdRental = rentalService.createRental(rentalRequestDTO);
        RentalDTO rentalDTO = RentalDTO.fromEntity(createdRental);
        return ResponseEntity.ok(rentalDTO);
    }

    /**
     * Updates an existing rental by its ID.
     *
     * @param id The ID of the rental to update.
     * @param rentalRequestDTO The DTO containing the updated details.
     * @return A ResponseEntity containing the updated RentalDTO.
     */
    @PutMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> updateRental(@PathVariable Integer id, @Valid @RequestBody RentalRequestDTO rentalRequestDTO) {
        Rental updatedRental = rentalService.updateRental(id, rentalRequestDTO);
        RentalDTO rentalDTO = RentalDTO.fromEntity(updatedRental);
        return ResponseEntity.ok(rentalDTO);
    }
}
