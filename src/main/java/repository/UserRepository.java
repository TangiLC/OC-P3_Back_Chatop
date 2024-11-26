package repository;

import model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserRepository, Integer> {
	
	//findById(ID id) is JPA native method
	//deleteById(ID id) is JPA native method

    User findByEmail(String email);

}
