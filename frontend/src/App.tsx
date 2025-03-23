import { useEffect, useState } from "react";
import FlightSearch from "./FlightSearch";
import SeatRecommendation from "./SeatRecommendation";
import Flight from "./types/flight";
import SeatMapData from "./types/seatMapData";

const defaultSeatMapData: SeatMapData = {
  seatMap: '',
  seatClass: null,
  rowLength: 0,
  rows: []
};

// Enamus kasutajaliidesest ja -kogemusest AI poolt
export default function App() {
  const [flights, setFlights] = useState([]);
  const [seatMapData, setSeatMapData] = useState(defaultSeatMapData as SeatMapData);
  const [selectedModel, setSelectedModel] = useState("");
  const [seatClasses, setSeatClasses] = useState([] as string[]);
  const [departure, setDeparture] = useState("");
  const [destination, setDestination] = useState("");
  const [date, setDate] = useState("");
  const [maxPrice, setMaxPrice] = useState("");

  useEffect(() => {
    fetchFlights();
  }, []);

  const fetchFlights = async () => {
    let query = new URLSearchParams();
    if (departure) query.append("departure", departure);
    if (destination) query.append("destination", destination);
    if (date) query.append("date", date);
    if (maxPrice) query.append("maxPrice", maxPrice);

    const response = await fetch(
      `http://localhost:8080/flights?${query.toString()}`
    );
    const data = await response.json();
    setFlights(data);
  };

  const handleFlightClick = async (flight: Flight) => {
    const { aircraft: { model }, seatClasses } = flight;
    const response = await fetch(
      `http://localhost:8080/aircraft/${encodeURIComponent(model)}/seats`
    );
    const data = await response.json();
    setSeatMapData(data);
    setSelectedModel(model);
    setSeatClasses(seatClasses)
  };

  return (
    <div className="p-6 max-w-2xl mx-auto rounded-lg shadow-md">
      <h1 className="text-2xl font-bold mb-6 text-center">Lennuplaan</h1>

      <div className="grid grid-cols-2 gap-4 mb-6">
        <FlightSearch
          airport={departure}
          setAirport={setDeparture}
          placeholder={"Lähtepunkt"}
        />
        <FlightSearch
          airport={destination}
          setAirport={setDestination}
          placeholder={"Sihtpunkt"}
        />
        <input
          className="p-3 border rounded-lg w-full"
          type="date"
          name="date"
          onChange={e => setDate(e.target.value)}
        />
        <input
          className="p-3 border rounded-lg w-full"
          type="number"
          name="destination"
          placeholder="Maksimaalne hind"
          onChange={e => setMaxPrice(e.target.value)}
        />
      </div>

      <div className="flex justify-center">
        <button
          className="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-lg shadow-md transition duration-200"
          onClick={fetchFlights}
        >
          Otsi
        </button>
      </div>

      <ul className="mt-6 space-y-4">
        {flights.map((flight: Flight) => (
          <li
            key={flight.number}
            className="cursor-pointer transition duration-200 transform hover:scale-105"
            onClick={() => handleFlightClick(flight)}
          >
            <div className="p-5 border rounded-lg shadow-md bg-gray-50 hover:bg-gray-100">
              <h2 className="text-xl font-bold text-gray-900">
                Lend {flight.number}
              </h2>
              <p className="text-gray-700">
                <span className="font-semibold">Kuupäev:</span>{" "}
                {new Date(flight.departure.time).toLocaleDateString()}
              </p>
              <p className="text-gray-700">
                {flight.departure.airport.name} ({flight.departure.airport.iata}
                ) → {flight.arrival.airport.name} ({flight.arrival.airport.iata})
              </p>
              <p className="text-gray-600">
                <span className="font-semibold">Lähtepunkt:</span>{" "}
                {new Date(flight.departure.time).toLocaleTimeString()}
              </p>
              <p className="text-gray-600">
                <span className="font-semibold">Sihtkoht:</span>{" "}
                {new Date(flight.arrival.time).toLocaleTimeString()}
              </p>
              <p className="text-gray-600">
                <span className="font-semibold">Hind:</span> {flight.price}€
              </p>
            </div>
          </li>
        ))}
      </ul>

      {selectedModel && (
        <div className="mt-8 border-t pt-6">
          <SeatRecommendation
            selectedModel={selectedModel}
            seatMapData={seatMapData}
            seatClasses={seatClasses}
          />
        </div>
      )}
    </div>
  );
}
