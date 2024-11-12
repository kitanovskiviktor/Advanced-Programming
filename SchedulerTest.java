import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Comparator;

class Timestamp<T> implements Comparable<Timestamp<?>>{
    private final LocalDateTime time;
    private final T element;

    public Timestamp(LocalDateTime timestamp, T element) {
        this.time = timestamp;
        this.element = element;
    }

    public LocalDateTime getTime(){
        return time;
    }

    public T getElement(){
        return element;
    }

    public int compareTo(Timestamp<?> t){
        return time.compareTo(t.time);
    }

    public boolean equals(Object o){
        return time.equals(((Timestamp<?>)o).time);
    }

    public String toString(){
        return time.toString() + " " + element.toString();
    }
}

class Scheduler<T> {
    private List<Timestamp> timestamps;
    private T typeObject;

    public Scheduler(){
        this.timestamps = new ArrayList<>();
        this.typeObject = null;
    }

    public void add(Timestamp<T> t){
        this.timestamps.add(t);
    }

    public boolean remove(Timestamp<T> t){
       return this.timestamps.remove(t);
    }

    public Timestamp<T> next() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp<T> closestFuture = null;

        for (Timestamp<T> timestamp : timestamps) {
            if (timestamp.getTime().isAfter(now)) {
                if (closestFuture == null || timestamp.getTime().isBefore(closestFuture.getTime())) {
                    closestFuture = timestamp;
                }
            }
        }
        return closestFuture;
    }

    public Timestamp<T> last() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp<T> closestPast = null;

        for (Timestamp<T> timestamp : timestamps) {
            if (timestamp.getTime().isBefore(now)) {
                if (closestPast == null || timestamp.getTime().isAfter(closestPast.getTime())) {
                    closestPast = timestamp;
                }
            }
        }
        return closestPast;
    }

    public List<Timestamp<T>> getAll(LocalDateTime begin, LocalDateTime end) {
        List<Timestamp<T>> result = new ArrayList<>();

        for (Timestamp<T> timestamp : timestamps) {
            if (timestamp.getTime().isAfter(begin) && timestamp.getTime().isBefore(end)) {
                result.add(timestamp);
            }
        }
        return result;
    }


}


public class SchedulerTest {
    static final LocalDateTime TIME = LocalDateTime.of(2016, 10, 25, 10, 15);

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Timestamp with String
            Timestamp<String> t = new Timestamp<>(TIME, jin.next());
            System.out.println(t);
            System.out.println(t.getTime());
            System.out.println(t.getElement());
        }
        if (k == 1) { //test Timestamp with ints
            Timestamp<Integer> t1 = new Timestamp<>(TIME, jin.nextInt());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<Integer> t2 = new Timestamp<>(TIME.plusDays(10), jin.nextInt());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 2) {//test Timestamp with String, complex
            Timestamp<String> t1 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<String> t2 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(5), jin.next()));
            System.out.println(scheduler.next().getElement());
            System.out.println(scheduler.last().getElement());
            List<Timestamp<String>> result = scheduler.getAll(now.minusHours(3), now.plusHours(4).plusMinutes(15));
            String out = result.stream()
                    .sorted()
                    .map(Timestamp::getElement)
                    .collect(Collectors.joining(", "));
            System.out.println(out);
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<>();
            int counter = 0;
            ArrayList<Timestamp<Integer>> forRemoval = new ArrayList<>();
            while (jin.hasNextLong()) {
                Timestamp<Integer> ti = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.nextInt());
                if ((counter & 7) == 0) {
                    forRemoval.add(ti);
                }
                scheduler.add(ti);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                LocalDateTime left = ofEpochMS(jin.nextLong());
                LocalDateTime right = ofEpochMS(jin.nextLong());
                List<Timestamp<Integer>> res = scheduler.getAll(left, right);
                Collections.sort(res);
                System.out.println(left + " <: " + print(res) + " >: " + right);
            }
            System.out.println("test");
            List<Timestamp<Integer>> res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            forRemoval.forEach(scheduler::remove);
            res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static LocalDateTime ofEpochMS(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
    }

    private static <T> String print(List<Timestamp<T>> res) {
        if (res == null || res.size() == 0) return "NONE";
        return res.stream()
                .map(each -> each.getElement().toString())
                .collect(Collectors.joining(", "));
    }
}