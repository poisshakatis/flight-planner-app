package com.example.flightplannerapi.model;

import lombok.Data;

@Data
public class Airport {
    private final String municipality;
    private final String iataCode;
}
