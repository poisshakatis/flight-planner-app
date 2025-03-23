package com.example.flightplannerapi.dto;

import lombok.Data;

@Data
public class SeatPreferences {
    private boolean window;
    private boolean extraLegroom;
    private boolean exitRow;
    private int groupSize;
    private String seatClass;
}
