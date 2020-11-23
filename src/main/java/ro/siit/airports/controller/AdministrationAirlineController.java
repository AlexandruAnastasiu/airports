package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.siit.airports.domain.Airline;
import ro.siit.airports.repository.AirlineRepository;
import ro.siit.airports.service.AirlineService;

import java.util.List;
import java.util.Optional;

@Controller
public class AdministrationAirlineController {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirlineService airlineService;

    @RequestMapping("/admin/settings/airlines")
    public String displayAirportsSettingsPage(Model model) {
        String keyword = null;
        return listByPage(model, 1, "name", "asc", keyword);
    }

    @GetMapping("/admin/settings/airlines/page/{pageNumber}")
    public String listByPage(Model model, @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") final String sortField,
                             @Param("sortDir") final String sortDir,
                             @Param("keyword") final String keyword) {

        Page<Airline> page = airlineService.listAllAirlines(currentPage, sortField, sortDir, keyword);

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Airline> airlines = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("airlines", airlines);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        return "airlines-settings";
    }

    @GetMapping("/admin/settings/airlines/add")
    public String displayAddPage(final Model model) {
        model.addAttribute("airline", new Airline());
        return "add-airline";
    }

    @PostMapping("/admin/settings/airlines/post-add")
    public String displayPostAdd(final Model model, @ModelAttribute final Airline airline) {
        airlineService.insertIntoDatabase(airline);
        model.addAttribute("addedAirline", airline);
        return "add-airline-successful";
    }

    @GetMapping("/admin/settings/airlines/edit/{airlineId}")
    public String displayEditAirlinePage(final Model model, @PathVariable("airlineId") final Long airlineId) {
        final Optional<Airline> optAirline = airlineRepository.findById(airlineId);
        Airline airline = optAirline.get();
        model.addAttribute("myAirline", airline);
        return "edit-airline";
    }

    @PostMapping("/admin/settings/airlines/edit/result")
    public String displayEditAirlineResult(final Model model, @ModelAttribute final Airline myAirline) {
        Airline editedAirline = airlineService.insertIntoDatabase(myAirline);
        Optional<Airline> optAirline = Optional.of(editedAirline);
        model.addAttribute("airlineName", optAirline.map(a -> a.getName()).orElse("No Name"));
        model.addAttribute("airlineAlias", optAirline.map(a -> a.getAlias()).orElse("No Alias"));
        model.addAttribute("airlineIata", optAirline.map(a -> a.getIata()).orElse("No Iata Code"));
        model.addAttribute("airlineIcao", optAirline.map(a -> a.getIcao()).orElse("No Icao Code"));
        model.addAttribute("airlineCallsign", optAirline.map(a -> a.getCallsign()).orElse("No Callsign"));
        model.addAttribute("airlineCountry", optAirline.map(a -> a.getCountry()).orElse("No Country"));
        model.addAttribute("airlineActive", optAirline.map(a -> a.getActive()).orElse(false));
        return "edit-airline-successful";
    }

    @GetMapping("/admin/settings/airlines/delete/{airlineId}")
    public String deleteAirport(final Model model, @PathVariable("airlineId") final Long airlineId) {
        final Airline airlineToDelete = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Airline ID:" + airlineId));
        model.addAttribute("airlineName", airlineToDelete.getName());
        airlineRepository.delete(airlineToDelete);
        return "delete-airline-successful";
    }
}