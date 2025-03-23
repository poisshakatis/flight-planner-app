export default interface Flight {
    number: string;
    departure: {
      airport: {
        iata: string;
        name: string;
      };
      time: string; // ISO timestamp
    };
    arrival: {
      airport: {
        iata: string;
        name: string;
      };
      time: string; // ISO timestamp
    };
    price: number;
    seatClasses: string[];
    aircraft: {
      model: string;
    };
  }
  