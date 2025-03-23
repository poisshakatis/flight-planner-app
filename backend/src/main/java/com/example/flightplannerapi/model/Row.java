package com.example.flightplannerapi.model;

import lombok.Data;

import java.util.List;

@Data
public class Row {
    private final int row;
    private final int length;
    private final List<Seat> seats;
    private final boolean extraLegroom;

    @Data
    public static class Seat {
        private final Character seat;
        private final int position;
        private final boolean extraLegroom;
        private final SeatClass seatClass;
    }
}
