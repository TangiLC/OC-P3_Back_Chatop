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

    public Rental createRental(Rental rental) {
        if (rental.getName() == null || rental.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Rental name is required");
        }
        if (rental.getOwner() == null) {
            throw new IllegalArgumentException("Owner is required");
        }
        if (rental.getPrice() == null || rental.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (rental.getSurface() == null || rental.getSurface() <= 0) {
            throw new IllegalArgumentException("Surface must be greater than zero");
        }

        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    public Rental readRentalById(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found with id: " + id));
    }

    public List<Rental> readAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> readRentalsByOwnerId(Integer ownerId) {
        return rentalRepository.findByOwnerId(ownerId);
    }

    public List<Rental> searchByName(String keyword) {
        return rentalRepository.findByNameContaining(keyword);
    }

    public List<Rental> filterBySurface(Integer minSurface) {
        return rentalRepository.findBySurfaceGreaterThanEqual(minSurface);
    }

    public List<Rental> filterByPriceRange(Integer minPrice, Integer maxPrice) {
        return rentalRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public Rental updateRental(Integer id, Rental updatedRental) {
        Rental existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found with id: " + id));

        if (updatedRental.getName() != null && !updatedRental.getName().trim().isEmpty()) {
            existingRental.setName(updatedRental.getName());
        }
        if (updatedRental.getSurface() != null) {
            existingRental.setSurface(updatedRental.getSurface());
        }
        if (updatedRental.getPrice() != null && updatedRental.getPrice() > 0) {
            existingRental.setPrice(updatedRental.getPrice());
        }
        if (updatedRental.getPicture() != null) {
            existingRental.setPicture(updatedRental.getPicture());
        }
        if (updatedRental.getDescription() != null) {
            existingRental.setDescription(updatedRental.getDescription());
        }

        existingRental.setUpdatedAt(LocalDateTime.now());
        return rentalRepository.save(existingRental);
    }

    public void deleteRentalById(Integer id) {
        if (!rentalRepository.existsById(id)) {
            throw new RuntimeException("Rental not found with id: " + id);
        }
        rentalRepository.deleteById(id);
    }
}
