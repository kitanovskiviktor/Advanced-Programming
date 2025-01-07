package SecondMidtermExercises;

import java.util.*;


class Participant {

    String code;
    String name;
    int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return code + " " + name + " " + age;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return code.equals(((Participant) object).code);
    }

    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

}


class Audition {

    private Map<String, Set<Participant>> participants;


    public Audition() {
        this.participants = new HashMap<>();
    }

    public void addParticipant(String city, String code, String name, int age){
        participants.computeIfAbsent(city, p -> new HashSet<>());
        participants.get(city).add(new Participant(code, name, age));
    }

    public void listByCity(String city){
        participants.get(city).stream()
                .sorted(Comparator.comparing(Participant::getName)
                        .thenComparing(Participant::getAge))
                .forEach(System.out::println);
    }

}



public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}