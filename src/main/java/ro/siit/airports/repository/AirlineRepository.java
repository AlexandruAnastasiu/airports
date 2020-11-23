package ro.siit.airports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.siit.airports.domain.Airline;
import ro.siit.airports.domain.Airport;

import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Optional<Airline> findByNameIgnoreCase(String name);

    @Query("SELECT a FROM Airline a WHERE CONCAT(a.name, ' ', a.alias, ' ', a.country, ' ') LIKE %?1%")
    Page<Airline> findAll(String keyword, Pageable pageable);

}
