import { useState } from "react";
import { seatClassMapping } from "./constants";
import SeatMapData from "./types/seatMapData";

interface SeatRecommendationProps {
  selectedModel: string;
  seatMapData: SeatMapData;
  seatClasses: string[];
}

export default function SeatRecommendation({
  selectedModel,
  seatMapData,
  seatClasses,
}: SeatRecommendationProps) {
  const [groupSize, setGroupSize] = useState("1");
  const [windowSeat, setWindowSeat] = useState(false);
  const [extraLegroom, setExtraLegroom] = useState(false);
  const [seatClass, setSeatClass] = useState("");
  const [recommendedSeats, setRecommendedSeats] = useState([] as string[]);

  const recommendSeats = async () => {
    let query = new URLSearchParams();
    query.append("groupSize", groupSize);
    if (windowSeat) query.append("window", "true");
    if (extraLegroom) query.append("extraLegroom", "true");
    if (seatClass) query.append("seatClass", seatClass.toLowerCase());

    const response = await fetch(
      `http://localhost:8080/aircraft/${selectedModel}/seats/recommend?${query.toString()}`
    );
    const data = await response.json();
    setRecommendedSeats(data);
  };

  const SeatMap = () => {
    const { rows } = seatMapData;
    return (
      <div className="p-4">
        {rows.map(({ row, length, seats, extraLegroom }) => {
          return (
            <div key={`row-${row}`} className="mb-2">
              <div
                className="grid gap-2"
                style={{
                  gridTemplateColumns: `repeat(${length}, minmax(0, 1fr))`,
                }}
              >
                {Array.from({ length }, (_, i) => {
                  const seat = seats.find((seat) => seat.position === i + 1);
                  return seat ? (
                    <div
                      key={i}
                      className={`h-8 flex items-center justify-center border rounded
                    ${
                      seat.isOccupied
                        ? "bg-gray-400"
                        : recommendedSeats.includes(row + seat.seat)
                        ? "bg-blue-400"
                        : seat.seatClass === "FIRST"
                        ? "bg-yellow-400"
                        : seat.seatClass === "MAIN_CABIN_EXTRA"
                        ? "bg-purple-400"
                        : "bg-green-400"
                    }
                    ${extraLegroom ? "mt-8" : ""}
                  `}
                    >
                      {row + seat.seat}
                    </div>
                  ) : (
                    <div key={i}></div>
                  );
                })}
              </div>
            </div>
          );
        })}
      </div>
    );
  };

  return (
    <div className="mt-6 p-6 rounded-lg shadow-md">
      <h2 className="text-xl font-bold mb-4">Istekohtade soovitamine</h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
        <label className="flex items-center space-x-2">
          <input
            type="checkbox"
            checked={windowSeat}
            onChange={(e) => setWindowSeat(e.target.checked)}
            className="h-5 w-5"
          />
          <span className="font-semibold">Istekoht akna all</span>
        </label>

        <label className="flex items-center space-x-2">
          <input
            type="checkbox"
            checked={extraLegroom}
            onChange={(e) => setExtraLegroom(e.target.checked)}
            className="h-5 w-5"
          />
          <span className="font-semibold mb-1">Rohkem jalaruumi</span>
        </label>

        <div className="flex flex-col">
          <label className="font-semibold mb-1">Istekohad kõrvuti:</label>
          <input
            className="p-2 border rounded w-full sm:w-32"
            type="number"
            min="1"
            placeholder="1"
            onChange={(e) => setGroupSize(e.target.value)}
          />
        </div>

        {seatClasses && (
          <div className="flex flex-col">
            <label className="font-semibold mb-1">Istekohtade klass:</label>
            <select
              className="p-2 border rounded w-full sm:w-48"
              onChange={(e) => setSeatClass(e.target.value)}
            >
              <option value="">Kõik</option>
              {seatClasses.map((seatClass) => (
                <option value={seatClass} key={seatClass}>
                  {seatClassMapping.get(seatClass)}
                </option>
              ))}
            </select>
          </div>
        )}
      </div>

      <div className="flex justify-center mt-2">
        <button
          className="bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded-lg shadow-md transition duration-200"
          onClick={recommendSeats}
        >
          Otsi
        </button>
      </div>

      <div className="mt-6">
        <SeatMap />
      </div>
    </div>
  );
}
