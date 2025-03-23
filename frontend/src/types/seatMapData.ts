interface Seat {
    seat: string;
    seatClass: string;
    extraLegroom: boolean;
    position: number;
    isOccupied: boolean;
}

interface Row {
    row: number;
    extraLegroom: boolean;
    length: number;
    seats: Seat[];
}

export default interface SeatMapData {
    seatMap: string;
    seatClass: string | null;
    rowLength: number;
    rows: Row[];
}
