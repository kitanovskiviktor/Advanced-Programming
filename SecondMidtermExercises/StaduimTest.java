

import java.util.*;

class SeatTakenException extends Exception {
    private String message;
    public SeatTakenException(String message){
        super(message);
    }
}

class SeatNotAllowedException extends Exception {
    private String message;
    public SeatNotAllowedException(String message){
        super(message);
    }
}

class Sector {
    private String code;
    private int capacity;
    private HashMap<Integer, Integer> occupancy;

    public Sector(String code, int capacity) {
        this.code = code;
        this.capacity = capacity;
        this.occupancy = new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public int getCapacity() {
        return capacity;
    }

    public HashMap<Integer, Integer> getOccupancy() {
        return occupancy;
    }

    public int getFreeSeats(){
        return capacity - occupancy.size();
    }

    private double getPercentage(){
       return occupancy.size() / (double)capacity * 100.0;
    }

    @Override
    public String toString() {
        return code + "\t" + getFreeSeats() + "/" + capacity + "\t" + String.format("%.1f", getPercentage()) + "%";
    }

}

class Stadium {

    private String name;
    private List<Sector> sectors;

    public Stadium(String name){
        this.sectors = new ArrayList<>();
        this.name = name;
    }

    public void createSectors(String[] sectorNames, int[] sizes){
        for(int i=0; i<sectorNames.length; i++){
            this.sectors.add(new Sector(sectorNames[i], sizes[i]));
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatNotAllowedException, SeatTakenException{
        Sector sector = sectors.stream().filter(sector1 -> sector1.getCode().equals(sectorName)).findFirst().get();

        if(seat < 1 || seat > sector.getCapacity()){
            throw new SeatNotAllowedException("Seat " + seat + " is out of bounds");
        }

        if(sector.getOccupancy().containsKey(seat)){
            throw new SeatTakenException("Mestoto e zafateno");
        }

        if ((type == 1 && sector.getOccupancy().containsValue(2)) ||
                (type == 2 && sector.getOccupancy().containsValue(1))){
            throw new SeatNotAllowedException("Ne e ist tip");
        }
        sector.getOccupancy().put(seat, type);
    }

    public void showSectors(){
        sectors.stream()
                .sorted(Comparator.comparingInt(Sector::getFreeSeats).reversed()
                .thenComparing(Sector::getCode))
                .forEach(System.out::println);

    }




}



public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
