package com.example.flightplannerapi.service;

import com.example.flightplannerapi.config.FlightProperties;
import com.example.flightplannerapi.dto.FlightResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightProperties flightProperties;

    public FlightService(FlightProperties flightProperties) {
        this.flightProperties = flightProperties;
    }

    public List<FlightResponse> getFlights(String departure, String destination, String date, Double maxPrice) {
        return flightProperties.getFlights().stream()
                .map(flight -> new FlightResponse(
                        flight.getNumber(),
                        flight.getDeparture(),
                        flight.getArrival(),
                        flight.getSeatClasses(),
                        flight.getAircraft(),
                        flight.getPrice()))
                .filter(flight -> departure == null ||
                        flight.getDeparture().getAirport().getIata().toUpperCase().contains(departure.toUpperCase()) ||
                        flight.getDeparture().getAirport().getName().toUpperCase().contains(departure.toUpperCase()))
                .filter(flight -> destination == null ||
                        flight.getArrival().getAirport().getIata().toUpperCase().contains(destination.toUpperCase()) ||
                        flight.getArrival().getAirport().getName().toUpperCase().contains(destination.toUpperCase()))
                .filter(flight -> date == null || flight.getDeparture().getTime().startsWith(date))
                .filter(flight -> maxPrice == null || flight.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
}
