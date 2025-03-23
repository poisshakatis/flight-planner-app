package com.example.flightplannerapi.controller;

import com.example.flightplannerapi.dto.FlightResponse;
import com.example.flightplannerapi.service.FlightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<FlightResponse> getFlights(
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Double maxPrice
    ) {
        return flightService.getFlights(departure, destination, date, maxPrice);
    }
}
