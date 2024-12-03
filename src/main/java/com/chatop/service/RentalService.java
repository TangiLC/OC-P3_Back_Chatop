package com.chatop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.exception.InvalidInputException;
import com.chatop.exception.ResourceNotFoundException;
import com.chatop.exception.UnauthorizedAccessException;
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
    private final ImageStorageService imageStorageService;

    public RentalService(
        RentalRepository rentalRepository,
        UserRepository userRepository,
        ImageStorageService imageStorageService
    ) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.imageStorageService = imageStorageService;
    }

    /**
     * Reads all rentals and converts them to DTOs.
     *
     * @return A list of RentalDTOs.
     */
    public List<RentalDTO> readAllRentalsAsDTO() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream().map(RentalDTO::fromEntity).toList();
    }

    /**
     * Reads a rental by its ID and converts it to a DTO.
     *
     * @param id The ID of the rental.
     * @return The RentalDTO of the specified rental.
     * @throws ResourceNotFoundException If the rental is not found.
     */
    public RentalDTO readRentalAsDTO(Integer id) {
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Rental not found with ID: " + id));
        return RentalDTO.fromEntity(rental);
    }

    /**
     * Creates a new rental from a RentalRequestDTO.
     *
     * @param rentalRequestDTO The DTO containing rental data.
     * @param ownerEmail       The email of the rental's owner.
     * @return The created Rental object.
     * @throws InvalidInputException If the owner is not found.
     */
    public Rental createRental(RentalRequestDTO rentalRequestDTO, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
            .orElseThrow(() -> new InvalidInputException("Owner not found with email: " + ownerEmail));

        String pictureUrl = null;
        if (rentalRequestDTO.getPicture() != null && !rentalRequestDTO.getPicture().isEmpty()) {
            try {
                pictureUrl = imageStorageService.saveImage(rentalRequestDTO.getPicture());
            } catch (Exception e) {
                throw new InvalidInputException("Error while saving picture: " + e.getMessage(), e);
            }
        }

        Rental rental = new Rental();
        rental.setName(rentalRequestDTO.getName());
        rental.setSurface(rentalRequestDTO.getSurface());
        rental.setPrice(rentalRequestDTO.getPrice());
        rental.setPicture(pictureUrl);
        rental.setDescription(rentalRequestDTO.getDescription());
        rental.setOwner(owner);
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        return rentalRepository.save(rental);
    }

    /**
     * Updates an existing rental with new data from a RentalRequestDTO.
     *
     * @param id               The ID of the rental to update.
     * @param rentalRequestDTO The DTO containing updated rental data.
     * @param ownerId          The id of the authenticated owner.
     * @return The updated Rental object.
     * @throws UnauthorizedAccessException If the current user is not the owner of the rental.
     * @throws ResourceNotFoundException   If the rental is not found.
     */
    public Rental updateRental(Integer id, RentalRequestDTO rentalRequestDTO, Integer ownerId) {
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Rental not found with ID: " + id));

        if (!rental.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedAccessException("Current user is not the owner of the rental with ID: " + id);
        }

        if (rentalRequestDTO.getName() != null) {
            rental.setName(rentalRequestDTO.getName());
        }
        if (rentalRequestDTO.getSurface() != null) {
            rental.setSurface(rentalRequestDTO.getSurface());
        }
        if (rentalRequestDTO.getPrice() != null) {
            rental.setPrice(rentalRequestDTO.getPrice());
        }
        if (rentalRequestDTO.getPicture() != null && !rentalRequestDTO.getPicture().isEmpty()) {
            try {
                String pictureUrl = imageStorageService.saveImage(rentalRequestDTO.getPicture());
                rental.setPicture(pictureUrl);
            } catch (Exception e) {
                throw new InvalidInputException("Error while saving picture: " + e.getMessage(), e);
            }
        }
        if (rentalRequestDTO.getDescription() != null) {
            rental.setDescription(rentalRequestDTO.getDescription());
        }

        rental.setUpdatedAt(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    /**
     * Updates an existing rental by its ID and returns it as a DTO.
     *
     * @param id               The ID of the rental to update.
     * @param rentalRequestDTO The DTO containing updated rental data.
     * @return The updated RentalDTO.
     */
    public RentalDTO updateRentalAsDTO(Integer id, RentalRequestDTO rentalRequestDTO, Integer ownerId) {
        Rental updatedRental = updateRental(id, rentalRequestDTO, ownerId);
        return RentalDTO.fromEntity(updatedRental);
    }
}
