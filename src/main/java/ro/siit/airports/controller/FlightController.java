package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.domain.Flight;
import ro.siit.airports.model.DateRangeSearch;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.service.FlightService;

import java.util.List;
import java.util.Optional;

@Controller
public class FlightController {

    @Autowired
    private FlightService flightService;
    @Autowired
    private AirportRepository airportRepository;

    @RequestMapping(value = "/search/flights/advanced", method = RequestMethod.GET)
    public ModelAndView displayAdvancedFlitghtSearchResults(@RequestParam(value = "airportName", required = false, defaultValue = "NoName") final String airportName) {
        final ModelAndView modelAndView = new ModelAndView("advanced-search");
        modelAndView.addObject("dateRangeSearch", new DateRangeSearch());
        modelAndView.addObject("airportName", airportName);
        return modelAndView;
    }

    @PostMapping("/search/flights/advanced/result")
    public ModelAndView displayAirportSearchResults(@RequestParam(value = "airportName", required = false, defaultValue = "NoName") final String airportName, final DateRangeSearch dateRangeSearch) {
        final ModelAndView modelAndView = new ModelAndView("advanced-results");
        modelAndView.addObject("daterangeSearch", dateRangeSearch);
        final Optional<Airport> airport = airportRepository.findByNameIgnoreCase(airportName);
        final List<Flight> departures = flightService.findDeparturesByDateRange(airport, dateRangeSearch);
        modelAndView.addObject("departures", departures);
        final List<Flight> arrivals = flightService.findArrivalsByDateRange(airport, dateRangeSearch);
        modelAndView.addObject("arrivals", arrivals);
        return modelAndView;
    }
}
