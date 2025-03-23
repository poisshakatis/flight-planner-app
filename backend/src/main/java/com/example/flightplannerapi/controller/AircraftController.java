package com.example.flightplannerapi.controller;

import com.example.flightplannerapi.config.AircraftProperties;
import com.example.flightplannerapi.dto.SeatPreferences;
import com.example.flightplannerapi.service.AircraftService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {
    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @GetMapping("{model}/seats")
    public AircraftProperties.Aircraft getSeats(@PathVariable String model) {
        String decoded = StringUtils.uriDecode(model, StandardCharsets.UTF_8);
        return aircraftService.getSeats(decoded);
    }

    @GetMapping("/{model}/seats/recommend")
    public List<String> recommendSeats(@PathVariable String model,
                                       @RequestParam(required = false) boolean window,
                                       @RequestParam(required = false) boolean extraLegroom,
                                       @RequestParam(defaultValue = "1") int groupSize,
                                       @RequestParam(required = false) String seatClass) {
        SeatPreferences preferences = new SeatPreferences();
        preferences.setWindow(window);
        preferences.setExtraLegroom(extraLegroom);
        preferences.setGroupSize(groupSize);
        preferences.setSeatClass(seatClass);

        String decoded = StringUtils.uriDecode(model, StandardCharsets.UTF_8);
        return aircraftService.recommendSeats(preferences, decoded);
    }

}
