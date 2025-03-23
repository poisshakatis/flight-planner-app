package com.example.flightplannerapi.service;

import com.example.flightplannerapi.config.FlightProperties;
import com.example.flightplannerapi.model.Airport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {
    private final FlightProperties flightProperties;

    public AirportService(FlightProperties flightProperties) {
        this.flightProperties = flightProperties;
    }

    public List<Airport> searchAirports(String query, boolean isDestination) {
        return flightProperties.getFlights().stream()
                .map(flight -> (isDestination ? flight.getArrival() : flight.getDeparture()).getAirport())
                .filter(airport -> airport.getName().toLowerCase().contains(query.toLowerCase()) ||
                        airport.getIata().toLowerCase().contains(query.toLowerCase()))
                .map(airport -> new Airport(airport.getName(), airport.getIata()))
                .toList();
    }
}
