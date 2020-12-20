package ro.siit.airports.service;

import org.springframework.data.domain.Page;
import ro.siit.airports.domain.Airline;
import ro.siit.airports.domain.Airport;

public interface AirlineService {
    Page<Airline> listAllAirlines(int pageNumber, String sortField, String sortDir, String keyword);
    Airline insertIntoDatabase(Airline myAirline);
    Airline getMostFlownAirline();
}
