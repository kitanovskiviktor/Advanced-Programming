package SecondMidtermExercises;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

class VehicleRecord {
    String registration;
    String spot;
    LocalDateTime entryTime;
    LocalDateTime exitTime;

    boolean entry;

    public VehicleRecord(Vehicle vehicle, LocalDateTime exitTime){
        this.registration = vehicle.registration;
        this.spot = vehicle.spot;
        this.entryTime = vehicle.timestamp;
        this.exitTime = exitTime;
    }

    public long getDuration(){
        return DateUtil.durationBetween(entryTime, exitTime);
    }

    public String getRegistration(){
        return registration;
    }

    public LocalDateTime getExitTime(){
        return exitTime;
    }

    public LocalDateTime getEntryTime(){
        return entryTime;
    }

    public String getSpot(){
        return spot;
    }

    @Override
    public String toString(){
        return "Registration number:" + registration + " Spot: " + spot + " Start timestamp: " + entryTime + " End timestamp: " + exitTime + " Duration in minutes: " + Duration.between(entryTime, exitTime).toMinutes();
    }
}

class Vehicle {
    String registration;
    String spot;
    LocalDateTime timestamp;

    boolean entry;

    public Vehicle(String registration, String spot, LocalDateTime timestamp, boolean entry){
        this.registration = registration;
        this.spot = spot;
        this.timestamp = timestamp;
        this.entry = entry;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    @Override
    public String toString(){
        return "Registration number: " + registration + " Spot: " + spot + " Start " + timestamp;
    }
}


class Parking {
    private int capacity;
    Set<Vehicle> vehicles;

    Set<VehicleRecord> leftVehicles;

    public Parking(int capacity){
        this.vehicles = new HashSet<>();
        this.leftVehicles = new HashSet<>();
        this.capacity = capacity;
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entry){
        Vehicle newVehicle = new Vehicle(registration, spot, timestamp, entry);
        if(entry){
            vehicles.add(newVehicle);
        }
        else {
            vehicles.remove(newVehicle);
            leftVehicles.add(new VehicleRecord(newVehicle, timestamp));
        }
    }

    public void currentState(){
        double percentage = ( capacity / (double)vehicles.size()) * 100;
        System.out.println(percentage);
        vehicles.stream()
                .sorted(Comparator.comparing(Vehicle::getTimestamp).reversed())
                .forEach(System.out::println);
    }

    public void history(){
        leftVehicles.stream()
                .sorted(Comparator.comparing(VehicleRecord::getDuration).reversed())
                .forEach(System.out::println);
    }

    public Map<String, Integer> carStatistics(){
        return leftVehicles.stream()
                .map(VehicleRecord::getRegistration)
                .collect(Collectors.groupingBy(
                        registration -> registration,
                        TreeMap::new,
                        Collectors.summingInt(reg -> 1)
                ));
    }

    public Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end){
        long totalMinutes = Duration.between(start, end).toMinutes();

        return leftVehicles.stream()
                .filter(record -> record.getExitTime().isAfter(start) && record.getEntryTime().isBefore(end))
                .collect(Collectors.groupingBy(
                        VehicleRecord::getSpot,
                        Collectors.summingDouble(
                                record -> {
                                    long occupiedMinutes = DateUtil.durationBetween(
                                            record.getEntryTime().isBefore(start) ? start : record.getEntryTime(),
                                            record.getExitTime().isAfter(end) ? end : record.getExitTime()
                                    );
                                    return (double) occupiedMinutes / totalMinutes * 100;
                                })
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1, HashMap::new
                ));
    }



}


public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
