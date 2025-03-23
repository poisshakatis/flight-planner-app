import { useEffect, useRef, useState } from "react";
import debounce from "lodash/debounce";
import Suggestions from "./types/suggestions";

interface FlightSearchProps {
  airport: string;
  setAirport: React.Dispatch<React.SetStateAction<string>>;
  placeholder: string;
}

export default function FlightSearch({ airport, setAirport, placeholder }: FlightSearchProps) {
  const [suggestions, setSuggestions] = useState([] as Suggestions[]);
  const [activeIndex, setActiveIndex] = useState(-1);
  const inputRef = useRef<HTMLInputElement>(null);
  const dropdownRef = useRef<HTMLUListElement>(null);

  // AI
  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(e.target as Node) &&
        inputRef.current !== e.target
      ) {
        setSuggestions([]);
        setActiveIndex(-1);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [setSuggestions]);

  const fetchSuggestions = debounce(async (query) => {
    if (!query) {
      setSuggestions([]);
      return;
    }

    const response = await fetch(
      `http://localhost:8080/airports?query=${query}&isDestination=${
        placeholder === "Sihtpunkt"
      }`
    );
    const data = await response.json();
    setSuggestions(data);
  }, 300);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setAirport(value);
    fetchSuggestions(value);
  };

  // AI
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "ArrowDown") {
      setActiveIndex(prev => Math.min(prev + 1, suggestions.length - 1));
    } else if (e.key === "ArrowUp") {
      setActiveIndex(prev => Math.max(prev - 1, 0));
    } else if (e.key === "Enter" && activeIndex >= 0) {
      setAirport(suggestions[activeIndex].iataCode);
      setSuggestions([]);
      setActiveIndex(-1);
    }
  };

  return (
    <div className="relative w-full">
      <input
        ref={inputRef}
        type="text"
        placeholder={placeholder}
        value={airport}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
        className="p-2 border rounded w-full"
      />

      {suggestions.length > 0 && (
        <ul
          ref={dropdownRef}
          className="absolute w-full border rounded mt-2 bg-white max-h-60 overflow-y-auto shadow-lg z-10"
        >
          {suggestions.map((airport, index) => (
            <li
              key={airport.iataCode}
              className={`p-2 cursor-pointer hover:bg-gray-200 ${
                index === activeIndex ? "bg-gray-300" : ""
              }`}
              onClick={() => {
                setAirport(airport.iataCode);
                setSuggestions([]);
                setActiveIndex(-1);
              }}
            >
              {airport.municipality} ({airport.iataCode})
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
