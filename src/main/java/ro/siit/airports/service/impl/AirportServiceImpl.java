package ro.siit.airports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.model.AirportSearch;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.service.AirportService;

import java.util.Optional;


@Service
public class AirportServiceImpl implements AirportService {

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
}
