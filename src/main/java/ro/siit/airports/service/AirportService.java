package ro.siit.airports.service;

import org.springframework.data.domain.Page;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.model.AirportSearch;

import java.util.List;
import java.util.Optional;

public interface AirportService {

    Optional<Airport> findFilteredAirports(AirportSearch search);
    Page<Airport> listAllAirports(int pageNumber, String sortField, String sortDir, String keyword);
    Airport insertIntoDatabase(Airport myAirport);
    Airport getBusiestAirport();
}
