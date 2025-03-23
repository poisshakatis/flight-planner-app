package com.example.flightplannerapi.controller;

import com.example.flightplannerapi.model.Airport;
import com.example.flightplannerapi.service.AirportService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {
    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public List<Airport> searchAirports(@RequestParam String query, @RequestParam boolean isDestination) {
        return airportService.searchAirports(query, isDestination);
    }
}
