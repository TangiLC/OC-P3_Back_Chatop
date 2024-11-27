package com.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatop.model.Rental;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
	
	//findById(ID id) is JPA native method
    //save() is JPA native method create/update
	//deleteById(ID id) is JPA native method
	
    List<Rental> findByOwnerId(Integer ownerId);

    List<Rental> findByName(String name);

    //TO DO Find Rental by...
    //List<Rental> findByNameContaining(String keyword);
    //List<Rental> findBySurfaceGreaterThanEqual(Integer surface);
    //List<Rental> findByPriceBetween(Integer minPrice, Integer maxPrice);
}
