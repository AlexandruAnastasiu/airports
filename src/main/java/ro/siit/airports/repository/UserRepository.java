package ro.siit.airports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE CONCAT(u.email, ' ', u.firstName, ' ', u.lastName, ' ') LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);
}