package SecondMidtermExercises;

//package mk.ukim.finki.midterm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Student {
    String index;
    String name;
    int pointsFirstMidterm;
    int pointsSecondMidterm;
    int pointsLab;

    public Student(String index, String name){
        this.index = index;
        this.name = name;
    }

    public void setPoints(String activity, int points){
        if(activity.equals("midterm1")){
            pointsFirstMidterm = points;
        }
        else if(activity.equals("midterm2")){
            pointsSecondMidterm = points;
        }
        else if(activity.equals("labs")){
            pointsLab = points;
        }
    }

    public double getSummaryPoints(){
        return pointsFirstMidterm * 0.45 + pointsSecondMidterm * 0.45 + pointsLab;
    }

    public int getGrade(){
        int g = (int) (getSummaryPoints() / 10 + 1);
        if(g > 10){
            g = 10;
        }
        else if(g <5){
            g = 5;
        }
        return g;
    }

    @Override
    public String toString(){
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                index, name, pointsFirstMidterm, pointsSecondMidterm, pointsLab, getSummaryPoints(), getGrade());
    }
}

class AdvancedProgrammingCourse {

    Set<Student> students;

    public AdvancedProgrammingCourse() {
        this.students = new HashSet<>();
    }

    public void addStudent(Student s){
        students.add(s);
    }

    public void updateStudent(String idNumber, String activity, int points){
        Student getStudent = students.stream().filter(s -> s.index.equals(idNumber)).findFirst().orElse(null);
        getStudent.setPoints(activity, points);
        students.add(getStudent);
    }

    public List<Student> getFirstNStudents(int n){
        return students.stream()
                .sorted(Comparator.comparing(Student::getSummaryPoints).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution(){
        Map<Integer, Long> grouped = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.counting()
                ));

        return IntStream.rangeClosed(5, 10)
                .boxed()
                .collect(Collectors.toMap(
                        grade -> grade,
                        grade -> grouped.getOrDefault(grade, 0L).intValue()
                ));
    }
    public void printStatistics() {
        int count = (int) students.stream().filter(s -> s.getGrade() >5).count();
        double min = students.stream()
                .filter(s -> s.getGrade() > 5)
                .mapToDouble(Student::getSummaryPoints)
                .min()
                .orElse(0);

        double average = students.stream()
                .filter(s -> s.getGrade() > 5)
                .mapToDouble(Student::getSummaryPoints)
                .average()
                .orElse(0);

        double max = students.stream()
                .filter(s -> s.getGrade() > 5)
                .mapToDouble(Student::getSummaryPoints)
                .max()
                .orElse(0);

        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f", count, min, average, max));
    }
}



public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}

