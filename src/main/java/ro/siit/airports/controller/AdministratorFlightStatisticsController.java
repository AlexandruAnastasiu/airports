package ro.siit.airports.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorFlightStatisticsController {

    @GetMapping("admin/flights/statistics")
    public String displayFlightsStatistics(final Model model) {
        return "flights-statistics";
    }
}
