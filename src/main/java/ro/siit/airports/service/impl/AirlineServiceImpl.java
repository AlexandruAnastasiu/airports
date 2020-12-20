package ro.siit.airports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.siit.airports.domain.Airline;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.domain.Flight;
import ro.siit.airports.repository.AirlineRepository;
import ro.siit.airports.repository.FlightRepository;
import ro.siit.airports.service.AirlineService;

import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class AirlineServiceImpl implements AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public Page<Airline> listAllAirlines(final int pageNumber, final String sortField, final String sortDir, final String keyword) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, sortDir.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());

        if(keyword != null) {
            return airlineRepository.findAll(keyword, pageable);
        }
        return airlineRepository.findAll(pageable);
    }

    @Override
    public Airline insertIntoDatabase(Airline myAirline) {
        return airlineRepository.save(myAirline);
    }

    @Override
    public Airline getMostFlownAirline() {
        List<Flight> allFlights = flightRepository.findAll();

        List<Airline> airlinesList = allFlights.stream().map(e -> e.getAirline()).collect(Collectors.toList());
        Airline mostFlownAirline = airlinesList.stream().reduce(BinaryOperator.maxBy((o1, o2) -> Collections.frequency(airlinesList, o1) -
                Collections.frequency(airlinesList, o2))).orElse(null);
        return mostFlownAirline;
    }
}
