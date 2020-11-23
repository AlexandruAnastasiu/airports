package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.airports.domain.Airline;
import ro.siit.airports.repository.AirlineRepository;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.repository.FlightRepository;
import ro.siit.airports.service.AirportService;
import ro.siit.airports.service.FlightService;

import java.util.List;

@Controller
public class AdministrationController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirportService airportService;

    @GetMapping("/admin")
    public ModelAndView displayAdministrationPage() {
        final ModelAndView modelAndView = new ModelAndView("admin-page");
        return modelAndView;
    }

}
