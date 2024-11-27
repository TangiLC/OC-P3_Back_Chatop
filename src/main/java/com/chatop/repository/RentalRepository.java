package com.chatop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatop.model.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
	
	//findById(ID id) is JPA native method
    //findAll() is JPA native method
    //save() is JPA native method create/update
	//deleteById(ID id) is JPA native method
	
    List<Rental> findByOwnerId(Integer ownerId);

    List<Rental> findByName(String name);
    
    List<Rental> findByNameContaining(String keyword);

    List<Rental> findBySurfaceGreaterThanEqual(Integer surface);

    List<Rental> findByPriceBetween(Integer minPrice, Integer maxPrice);
    
    //TO DO Find Rental by...
}
