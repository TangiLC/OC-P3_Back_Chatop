package com.chatop.service;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

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
   * @param userRepository   The repository for managing users.
   */
  public RentalService(
    RentalRepository rentalRepository,
    UserRepository userRepository
  ) {
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
    return rentals.stream().map(RentalDTO::fromEntity).toList();
  }

  /**
   * Reads a rental by its ID and converts it to a DTO.
   *
   * @param id The ID of the rental.
   * @return The RentalDTO of the specified rental.
   * @throws RuntimeException if the rental is not found.
   */
  public RentalDTO readRentalAsDTO(Integer id) {
    Rental rental = rentalRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + id)
      );
    return RentalDTO.fromEntity(rental);
  }

  /**
   * Creates a new rental from a RentalRequestDTO.
   *
   * @param rentalRequestDTO The DTO containing rental data.
   * @param ownerEmail       The email of the rental's owner.
   * @return The created Rental object.
   * @throws IllegalArgumentException if the owner is not found.
   */
  public Rental createRental(
    RentalRequestDTO rentalRequestDTO,
    String ownerEmail
  ) {
    // Fetch the owner from the database
    User owner = userRepository
      .findByEmail(ownerEmail)
      .orElseThrow(() ->
        new IllegalArgumentException(
          "Owner not found with email: " + ownerEmail
        )
      );

    // Map DTO to Entity
    Rental rental = new Rental();
    rental.setName(rentalRequestDTO.getName());
    rental.setSurface(rentalRequestDTO.getSurface());
    rental.setPrice(rentalRequestDTO.getPrice());
    rental.setPicture(
      rentalRequestDTO.getPicture() != null
        ? rentalRequestDTO.getPicture().getOriginalFilename()
        : null
    );
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
   * @param id               The ID of the rental to update.
   * @param rentalRequestDTO The DTO containing updated rental data.
   * @param ownerId          The id of the authenticated owner.
   * @return The updated Rental object.
   * @throws IllegalArgumentException owner not found, or user is not the owner.
   */
  public Rental updateRental(
    Integer id,
    RentalRequestDTO rentalRequestDTO,
    Integer ownerId
  ) {
    List<Rental> rentals = rentalRepository.findByOwnerId(ownerId);
    boolean rentalExists = rentals.stream().anyMatch(r -> r.getId().equals(id));

    if (!rentalExists) {
      throw new IllegalArgumentException(
        "Current user is not the owner of the rental " + id
      );
    }

    Rental rental = rentalRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + id)
      );

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
      rental.setPicture(rentalRequestDTO.getPicture().getOriginalFilename());
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
   * @throws RuntimeException if the rental is not found.
   */
  public RentalDTO updateRentalAsDTO(
    Integer id,
    RentalRequestDTO rentalRequestDTO,
    Integer ownerId
  ) {
    Rental updatedRental = updateRental(id, rentalRequestDTO, ownerId);
    return RentalDTO.fromEntity(updatedRental);
  }
}
