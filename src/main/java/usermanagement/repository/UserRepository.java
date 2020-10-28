package usermanagement.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import usermanagement.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	 User findByEmail(String email);
	 @Query("SELECT u FROM User u")
	 Collection<User> findAllUsers();
	 User findByToken(String token);
}
