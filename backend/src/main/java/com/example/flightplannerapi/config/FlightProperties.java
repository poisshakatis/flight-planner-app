package com.example.flightplannerapi.config;

import com.example.flightplannerapi.model.SeatClass;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ConfigurationProperties
@Data
public class FlightProperties {
    private List<Flight> flights;

    @Data
    public static class Flight {
        private String number;
        private Location departure;
        private Location arrival;
        private List<SeatClass> seatClasses;
        private Aircraft aircraft;
        private Double price;
    }

    @Data
    public static class Aircraft {
        private String model;
    }

    @Data
    public static class Location {
        private Airport airport;
        private String time;
    }

    @Data
    public static class Airport {
        private String iata;
        private String name;
    }
}
