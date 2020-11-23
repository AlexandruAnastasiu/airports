package ro.siit.airports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.siit.airports.domain.Airport;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    Optional<Airport> findByIcaoIgnoreCase(String icao);
    Optional<Airport> findByIataIgnoreCase(String iata);
    Optional<Airport> findByNameIgnoreCase(String name);

    @Query("SELECT a FROM Airport a WHERE CONCAT(a.name, ' ', a.country, ' ', a.city, ' ') LIKE %?1%")
    Page<Airport> findAll(String keyword, Pageable pageable);
}
