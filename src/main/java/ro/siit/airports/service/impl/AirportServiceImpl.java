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
import ro.siit.airports.model.AirportSearch;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.repository.FlightRepository;
import ro.siit.airports.service.AirportService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class AirportServiceImpl implements AirportService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;

    @Override
    public Optional<Airport> findFilteredAirports(final AirportSearch search) {
        Optional<Airport> airport;

        if (search.hasName()) {
            airport = airportRepository.findByNameIgnoreCase(search.getName());
        } else if (search.hasIataOrIcao()) {
            if (search.getIataOrIcao().length() == 3) {
                airport = airportRepository.findByIataIgnoreCase(search.getIataOrIcao());
            } else {
                airport = airportRepository.findByIcaoIgnoreCase(search.getIataOrIcao());
            }
        } else {
            airport = Optional.of(airportRepository.findAll().get(0));
        }
        return airport;
    }

    @Override
    public Page<Airport> listAllAirports(final int pageNumber, final String sortField, final String sortDir, final String keyword) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, sortDir.equals("asc") ? Sort.by(sortField).ascending()
                                                                                                        : Sort.by(sortField).descending());

        if(keyword != null) {
            return airportRepository.findAll(keyword, pageable);
        }
        return airportRepository.findAll(pageable);
    }

    @Override
    public Airport insertIntoDatabase(Airport myAirport) {
        return airportRepository.save(myAirport);
    }

    @Override
    public Map<Airport, Long> getBusiestAirports() {
        List<Flight> allFlights = flightRepository.findAll();

        List<Airport> departureAirportsList = allFlights.stream().map(e -> e.getDepartureAirport()).collect(Collectors.toList());
        List<Airport> arrivalAirportsList = allFlights.stream().map(e -> e.getArrivalAirport()).collect(Collectors.toList());
        List<Airport> airportsList = departureAirportsList;
        airportsList.addAll(arrivalAirportsList);
        /*
        Airport busiestAirport = airportsList.stream().reduce(BinaryOperator.maxBy((o1, o2) -> Collections.frequency(airportsList, o1) -
                Collections.frequency(airportsList, o2))).orElse(null);
        return busiestAirport;
         */

        Map<Airport, Long> elementCountMap = airportsList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<Airport, Long> result = elementCountMap.values().stream()
                .max(Long::compareTo).map(maxValue -> elementCountMap.entrySet().stream()
                        .filter(entry -> maxValue.equals(entry.getValue())).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())))
                .orElse(Collections.emptyMap());

        return result;
    }
}
