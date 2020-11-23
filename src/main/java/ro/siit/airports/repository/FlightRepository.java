package ro.siit.airports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.domain.Flight;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByFlightNoIgnoreCase(String flightNo);
    List<Flight> findByDepartureAirport(Optional<Airport> departureAirport);
    List<Flight> findByArrivalAirport(Optional<Airport> arrivalAirport);

    @Query("SELECT f FROM Flight f WHERE CONCAT(f.flightNo, ' ', f.airline.name, ' ') LIKE %?1%")
    Page<Flight> findAll(String keyword, Pageable pageable);
}
