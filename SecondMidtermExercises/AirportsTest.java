

import javax.sound.midi.SysexMessage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class Flight {
    String from;
    String to;
    int time;
    int duration;

    String arrivingOrDeparture;

    public Flight(String from, String to, int time, int duration, String arrivingOrDeparture){
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
        this.arrivingOrDeparture = arrivingOrDeparture;
    }

    public String getTo(){
        return to;
    }

    public String getFrom(){
        return from;
    }

    public int getTime(){
        return time;
    }

    @Override
    public String toString() {

        LocalTime departureTime = LocalTime.of(time / 60, time % 60);

        LocalTime arrivalTime = departureTime.plusMinutes(duration);

        String nextDay = arrivalTime.isBefore(departureTime) ? " +1d" : "";


        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        int hours = duration / 60;
        int minutes = duration % 60;

        return String.format("%s-%s %s-%s%s %dh%02dm",
                from, to,
                departureTime.format(timeFormatter),
                arrivalTime.format(timeFormatter),
                nextDay,
                hours, minutes);
    }
}

class Airport {
    String name;
    String country;
    String code;
    int passengers;

    List<Flight> flights;

    public Airport(){
        this.flights = new ArrayList<>();
    }

    public Airport(String name, String country, String code, int passengers){
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flights = new ArrayList<>();
    }
}


class Airports {

    Set<Airport> airports;

    public Airports(){
        this.airports =  new HashSet<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        Airport newAirport = new Airport(name, country, code, passengers);
        this.airports.add(newAirport);
    }

    public void addFlights(String from, String to, int time, int duration){
        Flight departureFlight = new Flight(from, to, time, duration, "departure");
        Flight arrivalFlight = new Flight(from, to, time, duration, "arrival");

        airports.stream()
                .filter(airport -> airport.code.equals(from))
                .findFirst()
                .get()
                .flights.add(departureFlight);

        airports.stream()
                .filter(airport -> airport.code.equals(to))
                .findFirst()
                .get()
                .flights.add(arrivalFlight);
    }

    public void showFlightsFromAirport(String code){

        Airport airport = airports.stream().filter(a -> a.code.equals(code)).findFirst().get();

        List<Flight> flights = airport.flights.stream()
                .filter(flight -> flight.arrivingOrDeparture.equals("departure"))
                .sorted(Comparator.comparing(Flight::getTo).thenComparing(Flight::getFrom).thenComparing(Flight::getTime))
                .collect(Collectors.toList());

        System.out.println(airport.name + " (" + airport.code + ")");
        System.out.println(airport.country);
        System.out.println(airport.passengers);

        for(int i=0; i<flights.size(); i++){
            System.out.println(i+1 + ". " + flights.get(i).toString());
        }


    }

    public void showDirectFlightsFromTo(String from, String to) {
        airports.stream()
                .flatMap(airport -> airport.flights.stream())
                .filter(flight -> flight.getFrom().equals(from) && flight.getTo().equals(to))
                .findAny()
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No flights from " + from + " to " + to)
                );
    }


    public void showDirectFlightsTo(String to){
        airports.stream()
                .flatMap(airport -> airport.flights.stream())
                .filter(flight -> flight.to.equals(to) && flight.arrivingOrDeparture.equals("arrival"))
                .sorted(Comparator.comparing(Flight::getTime).thenComparing(Flight::getFrom))
                .forEach(System.out::println);
    }



}



public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

