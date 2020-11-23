package ro.siit.airports.service;

import org.springframework.data.domain.Page;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.domain.Flight;
import ro.siit.airports.model.DateRangeSearch;
import ro.siit.airports.model.FlightCreator;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    List<Flight> findNextDepartures(Optional<Airport> airport);
    List<Flight> findNextArrivals(Optional<Airport> airport);
    List<Flight> findDeparturesByDateRange(Optional<Airport> airport, DateRangeSearch dateRangeSearch);
    List<Flight> findArrivalsByDateRange(Optional<Airport> airport, DateRangeSearch dateRangeSearch);
    Page<Flight> listAllFlights(int pageNumber, String sortField, String sortDir, String keyword);
    Flight insertIntoDatabase(Flight flight);
    Flight createFlight(FlightCreator flightCreator);
    Flight editFlight(Flight initialFlight, FlightCreator flightWithData);
}
