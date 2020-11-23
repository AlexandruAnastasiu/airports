package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.siit.airports.domain.Flight;
import ro.siit.airports.model.FlightCreator;
import ro.siit.airports.repository.AirlineRepository;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.repository.FlightRepository;
import ro.siit.airports.service.FlightService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdministrationFlightController {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightService flightService;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @RequestMapping("/admin/settings/flights")
    public String displayFlightsSettingsPage(Model model) {
        String keyword = null;
        return listByPage(model, 1, "flightNo", "asc", keyword);
    }

    @GetMapping("/admin/settings/flights/page/{pageNumber}")
    public String listByPage(Model model, @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") final String sortField,
                             @Param("sortDir") final String sortDir,
                             @Param("keyword") final String keyword) {

        Page<Flight> page = flightService.listAllFlights(currentPage, sortField, sortDir, keyword);

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Flight> flights = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("flights", flights);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        return "flights-settings";
    }

    @GetMapping("/admin/settings/flights/add")
    public String displayAddPage(final Model model) {
        model.addAttribute("flightCreator", new FlightCreator());
        List<String> airportNames = airportRepository.findAll().stream().map(a -> a.getName()).collect(Collectors.toList());
        List<String> airlineNames = airlineRepository.findAll().stream().map(a -> a.getName()).collect(Collectors.toList());
        model.addAttribute("airportNames", airportNames);
        model.addAttribute("airlineNames", airlineNames);
        return "add-flight";
    }

    @PostMapping("/admin/settings/flights/post-add")
    public String displayPostAdd(final Model model, @ModelAttribute final FlightCreator flightCreator) {

        final Flight flightToAdd = flightService.createFlight(flightCreator);
        flightService.insertIntoDatabase(flightToAdd);
        model.addAttribute("addedFlight", flightToAdd);
        return "add-flight-successful";
    }

    @GetMapping("/admin/settings/flights/edit/{flightId}")
    public String displayEditFlightPage(final Model model, @PathVariable("flightId") final Long flightId) {
        final Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID: " + flightId));

        FlightCreator flightCreator = new FlightCreator();
        flightCreator.setFlightNo(flight.getFlightNo());
        flightCreator.setDeparture(flight.getDeparture());
        flightCreator.setArrival(flight.getArrival());
        flightCreator.setDepartureAirportName(flight.getDepartureAirport().getName());
        flightCreator.setArrivalAirportName(flight.getArrivalAirport().getName());
        flightCreator.setAirlineName(flight.getAirline().getName());

        List<String> airportNames = airportRepository.findAll().stream().map(a -> a.getName()).collect(Collectors.toList());
        List<String> airlineNames = airlineRepository.findAll().stream().map(a -> a.getName()).collect(Collectors.toList());
        model.addAttribute("airportNames", airportNames);
        model.addAttribute("airlineNames", airlineNames);
        model.addAttribute("flightId", flightId);
        model.addAttribute("initialFlight", flight);
        model.addAttribute("flightCreator", flightCreator);
        return "edit-flight";
    }

    @PostMapping("/admin/settings/flights/edit/result/{flightId}")
    public String displayEditFlightResult(final Model model, @PathVariable("flightId") final Long flightId, @ModelAttribute final FlightCreator flightCreator) {
        final Flight editedFlight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid FLight ID" + flightId));

        flightService.insertIntoDatabase(flightService.editFlight(editedFlight, flightCreator));

        Optional<Flight> optFlight = Optional.of(editedFlight);
        model.addAttribute("flightNo", optFlight.map(a -> a.getFlightNo()).orElse("No Flight Number"));
        model.addAttribute("departure", optFlight.map(a -> a.getDeparture()).orElse(LocalDateTime.MIN));
        model.addAttribute("arrival", optFlight.map(a -> a.getArrival()).orElse(LocalDateTime.MIN));
        model.addAttribute("departureAirportName", optFlight.map(a -> a.getDepartureAirport().getName()).orElse("No Departure Airport Name"));
        model.addAttribute("arrivalAirportName", optFlight.map(a -> a.getArrivalAirport().getName()).orElse("No Arrival Airport Name"));
        model.addAttribute("airlineName", optFlight.map(a -> a.getAirline().getName()).orElse("No Airline Name"));
        return "edit-flight-successful";
    }

    @GetMapping("/admin/settings/flights/delete/{flightId}")
    public String deleteFlight(final Model model, @PathVariable("flightId") final Long flightId) {
        final Flight flightToDelete = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Flight ID:" + flightId));
        model.addAttribute("flightNo", flightToDelete.getFlightNo());
        flightToDelete.setAirline(null);
        flightToDelete.setDepartureAirport(null);
        flightToDelete.setArrivalAirport(null);
        flightToDelete.setAirline(null);
        flightRepository.delete(flightToDelete);
        return "delete-flight-successful";
    }
}
