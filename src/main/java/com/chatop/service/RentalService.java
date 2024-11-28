package com.chatop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chatop.model.Rental;
import com.chatop.repository.RentalRepository;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    /**
     * Creates a new rental.
     *
     * @param rental The rental object to create.
     * @return The created rental.
     */
    public Rental createRental(Rental rental) {
        if (rental.getName() == null || rental.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Rental name is required");
        }
        if (rental.getSurface() == null || rental.getSurface() <= 0) {
            throw new IllegalArgumentException("Rental surface must be greater than 0");
        }
        if (rental.getPrice() == null || rental.getPrice() <= 0) {
            throw new IllegalArgumentException("Rental price must be greater than 0");
        }
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    /**
     * Retrieves a rental by its ID.
     *
     * @param id The ID of the rental to retrieve.
     * @return The rental object.
     */
    public Rental readRental(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + id));
    }

    /**
     * Retrieves all rentals.
     *
     * @return A list of all rentals.
     */
    public List<Rental> readAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Updates a rental by its ID.
     *
     * @param id The ID of the rental to update.
     * @param updatedData The updated rental data.
     * @return The updated rental object.
     */
    public Rental updateRental(Integer id, Rental updatedData) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + id));

        // Update only non-null fields
        if (updatedData.getName() != null) {
            rental.setName(updatedData.getName().trim());
        }
        if (updatedData.getSurface() != null) {
            rental.setSurface(updatedData.getSurface());
        }
        if (updatedData.getPrice() != null) {
            rental.setPrice(updatedData.getPrice());
        }
        if (updatedData.getDescription() != null) {
            rental.setDescription(updatedData.getDescription().trim());
        }
        if (updatedData.getPicture() != null) {
            rental.setPicture(updatedData.getPicture());
        }

        rental.setUpdatedAt(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    /**
     * Deletes a rental by its ID.
     *
     * @param id The ID of the rental to delete.
     */
    public void deleteRental(Integer id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + id));
        rentalRepository.delete(rental);
    }
}
