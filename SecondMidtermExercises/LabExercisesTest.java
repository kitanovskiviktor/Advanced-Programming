package SecondMidtermExercises;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    String index;
    List<Integer> labPoints;

    public Student(){
        this.labPoints = new ArrayList<>();
    }

    public Student(String index, List<Integer> labPoints){
        this.index = index;
        this.labPoints = labPoints;
    }

    public int sumPoints(){
        return labPoints.stream().mapToInt(Integer::intValue).sum();
    }

    public String getIndex(){
        return index;
    }

    public boolean isPassed(){
        return labPoints.size() >= 8;
    }

    public int getYear(){
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    public double averagePoints(){
        return sumPoints() / 10.0;
    }

    @Override
    public String toString(){
        return index + " " + (isPassed() ? "YES " : "NO ") + String.format("%.2f", averagePoints());
    }

}

class LabExercises {

    Set<Student> students;

    public LabExercises(){
        this.students = new HashSet<>();
    }

    public void addStudent(Student student){
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n){
        if(ascending){
            students.stream()
                    .sorted(Comparator.comparing(Student::averagePoints).
                            thenComparing(Student::getIndex))
                    .limit(n)
                    .forEach(System.out::println);
        }
        else {
            students.stream()
                    .sorted(Comparator.comparing(Student::averagePoints).
                            thenComparing(Student::getIndex).
                            reversed())
                    .limit(n)
                    .forEach(System.out::println);
        }
    }

    public List<Student> failedStudents(){
        return students.stream()
                .filter(s -> !s.isPassed())
                .sorted(Comparator.comparing(Student::getIndex).thenComparing(
                        Student::sumPoints
                ))
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear(){
        return students.stream()
                .filter(Student::isPassed)
                .collect(Collectors.groupingBy(
                        Student::getYear,
                        Collectors.averagingDouble(Student::averagePoints)
                ));
    }
}



public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
