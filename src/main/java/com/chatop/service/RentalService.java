package com.chatop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;

/**
 * Service for managing rentals, including creation, update, and retrieval.
 */
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    /**
     * Constructs the RentalService.
     *
     * @param rentalRepository The repository for managing rentals.
     * @param userRepository The repository for managing users.
     */
    public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Reads all rentals and converts them to DTOs.
     *
     * @return A list of RentalDTOs.
     */
    public List<RentalDTO> readAllRentalsAsDTO() {
        List<Rental> rentals = rentalRepository.findAll();
        //System.out.println("Number of rentals found: " + rentals.size());
        return rentals.stream()
        .map(rental -> {
            //System.out.println("Processing rental ID: " + rental.getId());
            return RentalDTO.fromEntity(rental);
        })
                      .toList();
    }

    /**
     * Reads a rental by its ID.
     *
     * @param id The ID of the rental.
     * @return The rental object.
     */
    public Rental readRental(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + id));
    }

    /**
     * Creates a new rental from a RentalRequestDTO.
     *
     * @param rentalRequestDTO The DTO containing rental data.
     * @return The created Rental object.
     */
    public Rental createRental(RentalRequestDTO rentalRequestDTO) {
        // Fetch the owner from the database
        User owner = userRepository.findById(rentalRequestDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + rentalRequestDTO.getOwnerId()));

        // Map DTO to Entity
        Rental rental = new Rental();
        rental.setName(rentalRequestDTO.getName());
        rental.setSurface(rentalRequestDTO.getSurface());
        rental.setPrice(rentalRequestDTO.getPrice());
        rental.setPicture(rentalRequestDTO.getPicture());
        rental.setDescription(rentalRequestDTO.getDescription());
        rental.setOwner(owner);
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        // Save to database
        return rentalRepository.save(rental);
    }

    /**
     * Updates an existing rental with new data from a RentalRequestDTO.
     *
     * @param id The ID of the rental to update.
     * @param rentalRequestDTO The DTO containing updated rental data.
     * @return The updated Rental object.
     */
    public Rental updateRental(Integer id, RentalRequestDTO rentalRequestDTO) {
        // Fetch the rental from the database
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + id));

        // Update only non-null fields
        if (rentalRequestDTO.getName() != null) {
            rental.setName(rentalRequestDTO.getName());
        }
        if (rentalRequestDTO.getSurface() != null) {
            rental.setSurface(rentalRequestDTO.getSurface());
        }
        if (rentalRequestDTO.getPrice() != null) {
            rental.setPrice(rentalRequestDTO.getPrice());
        }
        if (rentalRequestDTO.getPicture() != null) {
            rental.setPicture(rentalRequestDTO.getPicture());
        }
        if (rentalRequestDTO.getDescription() != null) {
            rental.setDescription(rentalRequestDTO.getDescription());
        }

        rental.setUpdatedAt(LocalDateTime.now());

        // Save to database
        return rentalRepository.save(rental);
    }
}
