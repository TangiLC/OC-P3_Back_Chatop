package repository;

import model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	
	//findById(ID id) is JPA native method
	//save() is JPA native method create/update
	//deleteById(ID id) is JPA native method

    List<Message> findByRentalId(Integer rentalId);

    List<Message> findByUserId(Integer userId);
}
