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
import ro.siit.airports.model.DateRangeSearch;
import ro.siit.airports.model.FlightCreator;
import ro.siit.airports.repository.AirlineRepository;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.repository.FlightRepository;
import ro.siit.airports.service.FlightService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Override
    public List<Flight> findNextDepartures(final Optional<Airport> airport) {
        List<Flight> flights;

        flights = flightRepository.findByDepartureAirport(airport);
        return flights.stream()
                .filter(f -> f.getDeparture().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> findNextArrivals(final Optional<Airport> airport) {
        List<Flight> flights;

        flights = flightRepository.findByArrivalAirport(airport);
        return flights.stream()
                .filter(f -> f.getArrival().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> findDeparturesByDateRange(final Optional<Airport> airport, final DateRangeSearch dateRangeSearch) {
        LocalDate startDate = dateRangeSearch.getStartDate();
        LocalDate endDate = dateRangeSearch.getEndDate();
        List<Flight> flights = flightRepository.findByDepartureAirport(airport);

        return flights.stream()
                .filter(f -> f.getDeparture().toLocalDate().isAfter(startDate) && f.getDeparture().toLocalDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> findArrivalsByDateRange(final Optional<Airport> airport, final DateRangeSearch dateRangeSearch) {
        LocalDate startDate = dateRangeSearch.getStartDate();
        LocalDate endDate = dateRangeSearch.getEndDate();
        List<Flight> flights = flightRepository.findByArrivalAirport(airport);

        return flights.stream()
                .filter(f -> f.getDeparture().toLocalDate().isAfter(startDate) && f.getDeparture().toLocalDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Flight> listAllFlights(final int pageNumber, final String sortField, final String sortDir, final String keyword) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, sortDir.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());

        if (keyword != null) {
            return flightRepository.findAll(keyword, pageable);
        }
        return flightRepository.findAll(pageable);
    }

    @Override
    public Flight insertIntoDatabase(final Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight createFlight(final FlightCreator flightCreator) {
        final Airport departureAirport = airportRepository.findByNameIgnoreCase(flightCreator.getDepartureAirportName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airport Name:" + flightCreator.getDepartureAirportName()));

        final Airport arrivalAirport = airportRepository.findByNameIgnoreCase(flightCreator.getArrivalAirportName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airport Name:" + flightCreator.getArrivalAirportName()));

        final Airline airline = airlineRepository.findByNameIgnoreCase(flightCreator.getAirlineName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airline Name:" + flightCreator.getAirlineName()));

        final Flight flight = new Flight();
        flight.setFlightNo(flightCreator.getFlightNo());
        flight.setDeparture(flightCreator.getDeparture());
        flight.setArrival(flightCreator.getArrival());
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setAirline(airline);

        return flight;
    }

    @Override
    public Flight editFlight(Flight initialFlight, FlightCreator flightWithData) {
        final Airport departureAirport = airportRepository.findByNameIgnoreCase(flightWithData.getDepartureAirportName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airport Name:" + flightWithData.getDepartureAirportName()));

        final Airport arrivalAirport = airportRepository.findByNameIgnoreCase(flightWithData.getArrivalAirportName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airport Name:" + flightWithData.getArrivalAirportName()));

        final Airline airline = airlineRepository.findByNameIgnoreCase(flightWithData.getAirlineName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airline Name:" + flightWithData.getAirlineName()));

        initialFlight.setFlightNo(flightWithData.getFlightNo());
        initialFlight.setDeparture(flightWithData.getDeparture());
        initialFlight.setArrival(flightWithData.getArrival());
        initialFlight.setDepartureAirport(departureAirport);
        initialFlight.setArrivalAirport(arrivalAirport);
        initialFlight.setAirline(airline);

        return initialFlight;
    }

    @Override
    public Integer getCurrentTraffic() {
        Integer currentFlights = flightRepository.findAll().stream()
                .filter(f -> f.getArrival().isAfter(LocalDateTime.now()) && f.getDeparture().isBefore(LocalDateTime.now()))
                .reduce(0, (a,b) -> a + 1, (a,b) -> a + b);

        return currentFlights;
    }
}
