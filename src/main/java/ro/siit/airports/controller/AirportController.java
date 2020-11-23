package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.domain.Flight;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.service.FlightService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class AirportController {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightService flightService;

    @GetMapping("/airport/{airportId}")
    public ModelAndView displayAirportInfo(@PathVariable("airportId") final Long airportId) {
        final ModelAndView modelAndView = new ModelAndView("airport-results");
        final Optional<Airport> airport = airportRepository.findById(airportId);
        modelAndView.addObject("airportName", airport.map(a -> a.getName()).orElse("No Name"));
        modelAndView.addObject("airportCountry", airport.map(a -> a.getCountry()).orElse("No Country"));
        modelAndView.addObject("airportCity", airport.map(a -> a.getCity()).orElse("No City"));
        modelAndView.addObject("airportIata", airport.map(a -> a.getIata()).orElse("No IATA Code"));
        modelAndView.addObject("airportAltitude", airport.map(a -> a.getAltitude()).orElse(0));
        modelAndView.addObject("airportLatitude", airport.map(a -> a.getLatitude()).orElse(BigDecimal.ZERO));
        modelAndView.addObject("airportLongitude", airport.map(a -> a.getLongitude()).orElse(BigDecimal.ZERO));

        final List<Flight> nextDepartures = flightService.findNextDepartures(airport);
        modelAndView.addObject("nextDepartures", nextDepartures);
        final List<Flight> nextArrivals = flightService.findNextArrivals(airport);
        modelAndView.addObject("nextArrivals", nextArrivals);
        return modelAndView;
    }
}
