package SecondMidtermExercises;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class WrongDateException extends Exception {
    WrongDateException(Date date) {
        super("Wrong date: " + date);
    }
}

class Event implements Comparable<Event>{
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date){
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public int getMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    @Override
    public int compareTo(Event e){
        int value = date.compareTo(e.date);
        return value !=0 ? value : name.compareTo(e.name);
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        return dateFormat.format(date) + " at " + " " + location + " " + name;
    }
}

class EventCalendar {

    int year;
    Set<Event> events;

    public EventCalendar(int year){
        this.year = year;
        this.events = new TreeSet<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException{
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(calendar.get(Calendar.YEAR) != year){
            throw new WrongDateException(date);
        }
        events.add(new Event(name, location, date));
    }

    public void listEvents(Date date) {
        boolean hasEvents = events.stream()
                .anyMatch(event -> dateComparator(event.date, date));

        if (hasEvents) {
            events.stream()
                    .filter(event -> dateComparator(event.date, date))
                    .forEach(System.out::println);
        } else {
            System.out.println("No events on this day!");
        }
    }


    public boolean dateComparator(Date date1, Date date2){
        Calendar first = Calendar.getInstance();
        Calendar second = Calendar.getInstance();
        first.setTime(date1);
        second.setTime(date2);

        return first.get(Calendar.DAY_OF_MONTH) == second.get(Calendar.DAY_OF_MONTH)
                && first.get(Calendar.MONTH) == second.get(Calendar.MONTH)
                && first.get(Calendar.YEAR) == second.get(Calendar.YEAR);
    }

    public void listByMonth(){
        IntStream.range(0, 12).forEach(
                i -> {
                    long counter = events.stream().filter(event -> event.getMonth() == i).count();
                    System.out.printf("%d : %d%n", (i + 1), counter);
                });
    }

}


public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde