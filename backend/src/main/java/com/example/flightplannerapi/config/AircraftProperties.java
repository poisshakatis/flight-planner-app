package com.example.flightplannerapi.config;

import com.example.flightplannerapi.model.SeatClass;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties
@Data
public class AircraftProperties {
    private List<Aircraft> aircraft;

    @Data
    public static class Aircraft {
        private String seatMap;
        private String seatClass;
        private int rowLength;
        private List<Row> rows;
    }

    @Data
    public static class Row {
        private int row;
        private boolean extraLegroom;
        private int length;
        private List<Seat> seats;
    }

    @Data
    public static class Seat {
        private char seat;
        private SeatClass seatClass;
        private boolean extraLegroom;
        private int position;

        @JsonProperty("isOccupied")
        private boolean isOccupied;
    }
}
