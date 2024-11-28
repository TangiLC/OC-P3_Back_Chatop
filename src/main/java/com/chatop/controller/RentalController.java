package com.chatop.controller;

import com.chatop.model.Rental;
import com.chatop.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing rental-related operations such as retrieval, creation, and updates.
 */
@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Retrieves a list of all rentals.
     *
     * @return A list of rental objects.
     */
    @GetMapping("/rentals")
    public ResponseEntity<List<Rental>> getAllRentals() {
        List<Rental> rentals = rentalService.readAllRentals();
        return ResponseEntity.ok(rentals);
    }

    /**
     * Retrieves the details of a specific rental by its ID.
     *
     * @param id The ID of the rental to retrieve.
     * @return The rental object.
     */
    @GetMapping("/rentals/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Integer id) {
        Rental rental = rentalService.readRental(id);
        return ResponseEntity.ok(rental);
    }

    /**
     * Creates a new rental.
     *
     * @param rental The rental object containing the details of the rental to be created.
     * @return The created rental object.
     */
    @PostMapping("/rentals")
    public ResponseEntity<Rental> createRental(@RequestBody Rental rental) {
        Rental createdRental = rentalService.createRental(rental);
        return ResponseEntity.ok(createdRental);
    }

    /**
     * Updates an existing rental by its ID.
     *
     * @param id The ID of the rental to update.
     * @param rental The rental object containing the updated details.
     * @return The updated rental object.
     */
    @PutMapping("/rentals/{id}")
    public ResponseEntity<Rental> updateRental(@PathVariable Integer id, @RequestBody Rental rental) {
        Rental updatedRental = rentalService.updateRental(id, rental);
        return ResponseEntity.ok(updatedRental);
    }
}
