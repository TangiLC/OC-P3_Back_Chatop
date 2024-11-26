package repository;

import model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
	
	//findById(ID id) is JPA native method
	//deleteById(ID id) is JPA native method
	
    List<Rental> findByOwnerId(Integer ownerId);

    List<Rental> findByName(String name);

    //TODO Find Rental by...
    //List<Rental> findByNameContaining(String keyword);
    //List<Rental> findBySurfaceGreaterThanEqual(Integer surface);
    //List<Rental> findByPriceBetween(Integer minPrice, Integer maxPrice);
}
