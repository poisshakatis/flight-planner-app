package com.example.flightplannerapi.dto;

import com.example.flightplannerapi.config.FlightProperties;
import com.example.flightplannerapi.model.SeatClass;
import lombok.Getter;

import java.util.List;

@Getter
public class FlightResponse {
    private final String number;
    private final FlightProperties.Location departure;
    private final FlightProperties.Location arrival;
    private final List<SeatClass> seatClasses;
    private final FlightProperties.Aircraft aircraft;
    private final Double price;

    public FlightResponse(String number, FlightProperties.Location departure, FlightProperties.Location arrival, List<SeatClass> seatClasses, FlightProperties.Aircraft aircraft, Double price) {
        this.number = number;
        this.departure = departure;
        this.arrival = arrival;
        this.seatClasses = seatClasses;
        this.aircraft = aircraft;
        this.price = price;
    }
}
