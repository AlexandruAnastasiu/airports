package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.repository.AirportRepository;
import ro.siit.airports.service.AirportService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class AdministrationAirportController {

    @Autowired
    private AirportService airportService;

    @Autowired
    private AirportRepository airportRepository;

    @RequestMapping("/admin/settings/airports")
    public String displayAirportsSettingsPage(Model model) {
        String keyword = null;
        return listByPage(model, 1, "name", "asc", keyword);
    }

    @GetMapping("/admin/settings/airports/page/{pageNumber}")
    public String listByPage(Model model, @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") final String sortField,
                             @Param("sortDir") final String sortDir,
                             @Param("keyword") final String keyword) {

        Page<Airport> page = airportService.listAllAirports(currentPage, sortField, sortDir, keyword);

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Airport> airports = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("airports", airports);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        return "airports-settings";
    }

    @GetMapping("/admin/settings/airports/add")
    public String displayAddPage(final Model model) {
        model.addAttribute("airport", new Airport());
        return "add-airport";
    }

    @PostMapping("/admin/settings/airports/post-add")
    public String displayPostAdd(final Model model, @ModelAttribute final Airport airport) {
        airportService.insertIntoDatabase(airport);
        model.addAttribute("addedAirport", airport);
        return "add-airport-successful";
    }

    @GetMapping("/admin/settings/airports/edit/{airportId}")
    public String displayEditAirportPage(final Model model, @PathVariable("airportId") final Long airportId) {
        final Optional<Airport> optAirport = airportRepository.findById(airportId);
        Airport airport = optAirport.get();
        model.addAttribute("myAirport", airport);
        return "edit-airport";
    }

    @PostMapping("/admin/settings/airports/edit/result")
    public String displayEditAirportResult(final Model model, @ModelAttribute final Airport myAirport) {
        Airport editedAirport = airportService.insertIntoDatabase(myAirport);
        Optional<Airport> optAirport = Optional.of(editedAirport);
        model.addAttribute("airportName", optAirport.map(a -> a.getName()).orElse("No Name"));
        model.addAttribute("airportCountry", optAirport.map(a -> a.getCountry()).orElse("No Country"));
        model.addAttribute("airportCity", optAirport.map(a -> a.getCity()).orElse("No City"));
        model.addAttribute("airportIata", optAirport.map(a -> a.getIata()).orElse("No IATA Code"));
        model.addAttribute("airportIcao", optAirport.map(a -> a.getIata()).orElse("No ICAO Code"));
        model.addAttribute("airportAltitude", optAirport.map(a -> a.getAltitude()).orElse(0));
        model.addAttribute("airportLatitude", optAirport.map(a -> a.getLatitude()).orElse(BigDecimal.ZERO));
        model.addAttribute("airportLongitude", optAirport.map(a -> a.getLongitude()).orElse(BigDecimal.ZERO));
        model.addAttribute("airportTimezone", optAirport.map(a -> a.getTimezone()).orElse(Double.MIN_VALUE));
        model.addAttribute("airportDst", optAirport.map(a -> a.getDst()).orElse(Character.MIN_VALUE));
        model.addAttribute("airportTz", optAirport.map(a -> a.getTz()).orElse("No TZ"));
        model.addAttribute("airportType", optAirport.map(a -> a.getType()).orElse("No type"));
        model.addAttribute("airportSource", optAirport.map(a -> a.getSource()).orElse("No Source"));
        return "edit-airport-successful";
    }

    @GetMapping("/admin/settings/airports/delete/{airportId}")
    public String deleteAirport(final Model model, @PathVariable("airportId") final Long airportId) {
        final Airport airportToDelete = airportRepository.findById(airportId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airport ID:" + airportId));
        model.addAttribute("airportName", airportToDelete.getName());
        airportRepository.delete(airportToDelete);
        return "delete-airport-successful";
    }
}
