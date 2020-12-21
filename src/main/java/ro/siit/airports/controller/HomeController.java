package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.airports.domain.Airline;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.domain.Flight;
import ro.siit.airports.model.AirportSearch;
import ro.siit.airports.model.FlightSearch;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.repository.FlightRepository;
import ro.siit.airports.service.AirlineService;
import ro.siit.airports.service.AirportService;
import ro.siit.airports.service.FlightService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private AirportService airportService;

    @Autowired
    private AirlineService airlineService;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightService flightService;

    @GetMapping({"/", "/home", "/index"})
    public String displayHomePage(final Model model) {
        model.addAttribute("airportSearch", new AirportSearch());
        model.addAttribute("flightSearch", new FlightSearch());
        List<String> airportNames = airportRepository.findAll().stream().map(a -> a.getName()).collect(Collectors.toList());
        model.addAttribute("airportNames", airportNames);
        Integer currentTraffic = flightService.getCurrentTraffic();
        model.addAttribute("currentTraffic", currentTraffic);
        Map<Airline, Long> mostFlownAirlines = airlineService.getMostFlownAirlines();
        model.addAttribute("mostFlownAirlines", mostFlownAirlines.entrySet());
        Map<Airport, Long> busiestAirports = airportService.getBusiestAirports();
        model.addAttribute("busiestAirports", busiestAirports.entrySet());
        return "home-page";
    }

    @PostMapping("/airportresults")
    public ModelAndView displayAirportSearchResults(final AirportSearch airportSearch) {
        final ModelAndView modelAndView = new ModelAndView("airport-results");
        final Optional<Airport> airport = airportService.findFilteredAirports(airportSearch);
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

    @PostMapping("/flightresults")
    public ModelAndView displayFlightSearchResults(final FlightSearch flightSearch) {
        final ModelAndView modelAndView = new ModelAndView("flight-results");
        final List<Flight> flights = flightRepository.findByFlightNoIgnoreCase(flightSearch.getFlight_no());
        modelAndView.addObject("myFlights", flights);
        return modelAndView;
    }
}
