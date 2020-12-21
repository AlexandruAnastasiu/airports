package ro.siit.airports.service;

import org.springframework.data.domain.Page;
import ro.siit.airports.domain.Airline;
import ro.siit.airports.domain.Airport;

import java.util.List;
import java.util.Map;

public interface AirlineService {
    Page<Airline> listAllAirlines(int pageNumber, String sortField, String sortDir, String keyword);
    Airline insertIntoDatabase(Airline myAirline);
    Map<Airline, Long> getMostFlownAirlines();
}
