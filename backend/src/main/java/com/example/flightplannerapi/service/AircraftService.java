package com.example.flightplannerapi.service;

import com.example.flightplannerapi.config.AircraftProperties;
import com.example.flightplannerapi.dto.SeatPreferences;
import com.example.flightplannerapi.model.Row;
import com.example.flightplannerapi.model.SeatClass;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class AircraftService {
    private final AircraftProperties aircraftProperties;
    private final List<AircraftProperties.Aircraft> aircraftList = new ArrayList<>();

    private final Map<String, String> aircraftSeatMaps = Map.of(
            "Airbus A321 (Sharklets)", "A321-200_V3",
            "ATR 72", "ATR-72-500_V2"
    );

    public AircraftService(AircraftProperties aircraftProperties) {
        this.aircraftProperties = aircraftProperties;
        initializeAircraft();
    }

    private void initializeAircraft() {
        Random random = new Random();
        aircraftProperties.getAircraft().forEach(aircraft -> {
            aircraft.getRows().forEach(row ->
                    row.getSeats().forEach(seat ->
                            seat.setOccupied(random.nextBoolean()))
            );
            aircraftList.add(aircraft);
        });
    }

    public AircraftProperties.Aircraft getSeats(String model) {
        String seatMap;
        try {
            seatMap = aircraftSeatMaps.get(model);
        } catch (RuntimeException e) {
            throw new RuntimeException("aircraft model " + model + " not found");
        }

        for (AircraftProperties.Aircraft aircraft : aircraftList) {
            if (aircraft.getSeatMap().equals(seatMap)) {
                return aircraft;
            }
        }

        throw new RuntimeException("seat map not found for model " + model);
    }


    public List<String> recommendSeats(SeatPreferences preferences, String model) {
        AircraftProperties.Aircraft aircraft = getSeats(model);

        List<Row> rows = getAvailableSeats(aircraft);

        String seatClass = preferences.getSeatClass();
        if (seatClass != null) {
            SeatClass seatClassEnum = SeatClass.valueOf(seatClass.toUpperCase());
            rows = getRowsForSeatClass(rows, seatClassEnum);
        }

        if (preferences.getGroupSize() == 2) {
            rows = getPairSeats(rows);
        } else if (preferences.getGroupSize() == 3) {
            rows = getTripletSeats(rows);
        }

        if (preferences.isWindow()) {
            rows = getWindowSeats(rows);
        }

        if (preferences.isExtraLegroom()) {
            rows = getExtraLegroomSeats(rows);
        }

        return getSeatIdentifiers(rows);
    }

    private static List<Row> getRowsForSeatClass(List<Row> rows, SeatClass seatClass) {
        return rows.stream()
                .map(row -> new Row(
                        row.getRow(),
                        row.getLength(),
                        row.getSeats().stream()
                                .filter(seat -> seat.getSeatClass().equals(seatClass.toString()))
                                .toList(),
                        row.isExtraLegroom()
                ))
                .filter(row -> !row.getSeats().isEmpty())
                .toList();
    }

    private static List<String> getSeatIdentifiers(List<Row> rows) {
        return rows.stream()
                .flatMap(row -> row.getSeats().stream()
                        .map(seat -> row.getRow() + seat.getSeat().toString())
                )
                .toList();
    }

    private static List<Row> getExtraLegroomSeats(List<Row> rows) {
        return rows.stream()
                .map(row -> new Row(
                        row.getRow(),
                        row.getLength(),
                        row.getSeats().stream()
                                .filter(seat -> row.isExtraLegroom() || seat.isExtraLegroom())
                                .toList(),
                        row.isExtraLegroom()
                ))
                .filter(row -> !row.getSeats().isEmpty())
                .toList();
    }

    private static List<Row> getWindowSeats(List<Row> rows) {
        return rows.stream()
                .map(row -> new Row(
                        row.getRow(),
                        row.getLength(),
                        row.getSeats().stream()
                                .filter(seat -> seat.getPosition() == 1 || seat.getPosition() == row.getLength())
                                .toList(),
                        row.isExtraLegroom()
                ))
                .filter(row -> !row.getSeats().isEmpty())
                .toList();
    }

    private static List<Row> getPairSeats(List<Row> rows) {
        List<Row> list1 = new ArrayList<>();
        for (Row row : rows) {
            List<Row.Seat> list2 = new ArrayList<>();
            for (int i = 0; i < row.getSeats().size() - 1; i++) {
                Row.Seat current = row.getSeats().get(i);
                Row.Seat next = row.getSeats().get(i + 1);

                if (current.getPosition() + 1 == next.getPosition()) {
                    list2.add(current);
                    list2.add(next);
                    i++;
                }
            }

            if (!list2.isEmpty()) {
                list1.add(new Row(row.getRow(), row.getLength(), list2, row.isExtraLegroom()));
            }
        }
        rows = list1;
        return rows;
    }

    private static List<Row> getTripletSeats(List<Row> rows) {
        List<Row> list1 = new ArrayList<>();
        for (Row row : rows) {
            List<Row.Seat> list2 = new ArrayList<>();
            for (int i = 0; i < row.getSeats().size() - 2; i++) {
                Row.Seat current = row.getSeats().get(i);
                Row.Seat next = row.getSeats().get(i + 1);
                Row.Seat afterNext = row.getSeats().get(i + 2);

                if (current.getPosition() + 1 == next.getPosition() && next.getPosition() + 1 == afterNext.getPosition()) {
                    list2.add(current);
                    list2.add(next);
                    list2.add(afterNext);
                    i += 2;
                }
            }

            if (!list2.isEmpty()) {
                list1.add(new Row(row.getRow(), row.getLength(), list2, row.isExtraLegroom()));
            }
        }
        rows = list1;
        return rows;
    }

    private static List<Row> getAvailableSeats(AircraftProperties.Aircraft aircraft) {
        return aircraft.getRows().stream()
                .map(row -> new Row(
                        row.getRow(),
                        row.getLength(),
                        row.getSeats().stream()
                                .filter(seat -> !seat.isOccupied())
                                .map(seat -> new Row.Seat(seat.getSeat(), seat.getPosition(), seat.isExtraLegroom(), seat.getSeatClass()))
                                .toList(),
                        row.isExtraLegroom()
                ))
                .filter(row -> !row.getSeats().isEmpty())
                .toList();
    }
}
