package com.example.np_lab2_zad1;

import java.util.*;
import java.util.stream.Collectors;

class Student {

    private String id = "";
    private List<Integer> grades = new ArrayList<>();

    public Student(String id, List<Integer> grades){
        this.id = id;
        this.grades = grades;
    }

    public double getAverageGrade(){
        return grades.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public int getGradesSize(){
        return grades.size();
    }

    public String getId(){
        return id;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Student{id=" + "'" + this.id + "'");
        sb.append(", grades=[");
        for (int i = 0; i < grades.size(); i++) {
            sb.append(this.grades.get(i));
            if (i != grades.size() - 1) sb.append(", ");
        }
        sb.append("]}\n");
        return sb.toString();
    }
}


class Faculty {

    private Map<String, List<Integer>> students;

    public Faculty(){
        this.students = new HashMap<>();
    }

    public void addStudent(String id, List<Integer> grades){
        if(this.students.containsKey(id)){
            System.out.println("Student with ID " + id + " already exists!");
        }
        else {
            this.students.put(id, grades);
        }
    }

    public void addGrade(String id, int grade){
        List<Integer> newGrades = this.students.get(id);
        newGrades.add(grade);
        this.students.put(id, newGrades);
    }

    public Set<Student> getStudentsSortedByAverageGrade(){
        Comparator<Student> studentComparator = Comparator
                .comparingDouble(Student::getAverageGrade)
                .thenComparingInt(Student::getGradesSize)
                .thenComparing(Student::getId).reversed();

        return students.entrySet().stream()
                .map(entry -> new Student(entry.getKey(), entry.getValue()))
                .sorted(studentComparator)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Student> getStudentsSortedByCoursesPassed(){
        Comparator<Student> studentComparator = Comparator
                .comparingDouble(Student::getGradesSize)
                .thenComparingDouble(Student::getAverageGrade)
                .thenComparing(Student::getId).reversed();

        return students.entrySet().stream()
                .map(entry -> new Student(entry.getKey(), entry.getValue()))
                .sorted(studentComparator)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}



public class StudentFacultyTester {

    public static String formatSet(Set<Student> set) {
        StringBuilder sb = new StringBuilder();
        set.forEach(sb::append);
        return sb.toString().trim();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty1 = new Faculty();

        while (true) {
            String command = scanner.nextLine();

            if (command.startsWith("addStudent")) {
                String[] parts = command.split(" ");
                String id = parts[1];
                List<Integer> grades = new ArrayList<>();
                for (int i = 2; i < parts.length; i++) {
                    grades.add(Integer.parseInt(parts[i]));
                }
                faculty1.addStudent(id, grades);
            } else if (command.equals("getStudentsSortedByAverageGrade")) {
                System.out.println("Sorting students by average grade");
                System.out.println(formatSet(faculty1.getStudentsSortedByAverageGrade()));
            } else if (command.equals("getStudentsSortedByCoursesPassed")) {
                System.out.println("Sorting students by courses passed");
                System.out.println(formatSet(faculty1.getStudentsSortedByCoursesPassed()));
            }
            else if(command.startsWith("addGrade")){
                String[] parts = command.split(" ");
                String id = parts[1];
                Integer grade = Integer.valueOf(parts[2]);
                faculty1.addGrade(id, grade);
            }else if (command.equals("exit")) {
                break;
            } else {
                //System.out.println("Unknown command.");
            }
        }

        scanner.close();
    }


}
