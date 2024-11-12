import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

enum Operator {
    VIP, ONE, TMOBILE
}

class Contact {
    String dateCreated;
    String type;

    public Contact() {

    }

    public Contact(String date){
        this.dateCreated = date;
    }

    public String getType() {
        return type;
    }

    public boolean isNewerThan(Contact c) {
        return this.dateCreated.compareTo(c.dateCreated) > 0;
    }
}

class EmailContact extends Contact {
    String email;

    public EmailContact(String date, String email) {
        dateCreated = date;
        this.email = email;
        type = "Email";
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return type;
    }
}

class PhoneContact extends Contact {
    String phone;
    Operator operator;

    public PhoneContact(String date, String phone){
        dateCreated = date;
        this.phone = phone;
        char number = phone.charAt(2);
        if(number == '0' || number == '1' || number == '2'){
            operator = Operator.TMOBILE;
        }
        else if(number == '5' || number == '6'){
            operator = Operator.ONE;
        }
        else {
            operator = Operator.VIP;
        }
        type = "Phone";
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String getType() {
        return type;
    }
}

class Student {
    ArrayList <Contact> contacts;
    String firstName;
    String lastName;
    String city;
    int age;
    long index;

    public Student(String name) {
        this.firstName = name;
    }

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new ArrayList<Contact>();
    }

    public void addEmailContact(String date, String email){
        EmailContact newContact =  new EmailContact(date, email);
        contacts.add(newContact);
    }

    public void addPhoneContact(String date, String phone){
        PhoneContact newContact =  new PhoneContact(date, phone);
        contacts.add(newContact);
    }

    public Contact[] getEmailContacts(){
        ArrayList<Contact> emailContacts = new ArrayList<>();
        for(int i = 0; i < contacts.size(); i++){
            if(contacts.get(i).getType().equals("Email")){
                emailContacts.add(contacts.get(i));
            }
        }
        return emailContacts.toArray(new Contact[emailContacts.size()]);
    }

    public Contact[] getPhoneContacts(){
        ArrayList<Contact> phoneContacts = new ArrayList<>();
        for(int i = 0; i < contacts.size(); i++){
            if(contacts.get(i).getType().equals("Phone")){
                phoneContacts.add(contacts.get(i));
            }
        }
        return phoneContacts.toArray(new Contact[phoneContacts.size()]);
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        Contact latestContact = contacts.get(0);
        for(int i = 1; i < contacts.size(); i++){
            if (contacts.get(i).isNewerThan(latestContact)) {
                latestContact = contacts.get(i);
            }
        }
        return latestContact;
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("{")
                .append("\"ime\":\"").append(firstName).append("\", ")
                .append("\"prezime\":\"").append(lastName).append("\", ")
                .append("\"vozrast\":").append(age).append(", ")
                .append("\"grad\":\"").append(city).append("\", ")
                .append("\"indeks\":").append(index).append(", ")
                .append("\"telefonskiKontakti\":").append(listToString(contacts, "Phone"))
                .append("\"emailKontakti\":").append(listToString(contacts, "Email"))
                .append("}");

        return json.toString();
    }

    private String listToString(ArrayList<Contact> contacts, String type) {

        long countTypes = contacts.stream().filter(i -> i.getType().equals(type)).count();
        int addedCount = 0;

        StringBuilder jsonArray = new StringBuilder();
        jsonArray.append("[");
        for (int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getType().equals(type)){
                if (type.equals("Phone") && contacts.get(i) instanceof PhoneContact) {
                    PhoneContact phoneContact = (PhoneContact) contacts.get(i);
                    jsonArray.append("\"").append(phoneContact.getPhone()).append("\"");
                } else if (type.equals("Email") && contacts.get(i) instanceof EmailContact) {
                    EmailContact emailContact = (EmailContact) contacts.get(i);
                    jsonArray.append("\"").append(emailContact.getEmail()).append("\"");
                }
                addedCount++;
                if (addedCount < countTypes) {
                    jsonArray.append(", ");

                }
            }
        }
        jsonArray.append("]");
        return jsonArray.toString();
    }
}

class Faculty {
    String name;
    Student[] students;

    public Faculty() {}

    public Faculty(String name, Student [] students){
        this.name = name;
        this.students = new Student[students.length];
        for(int i=0; i<students.length; i++){
            this.students[i] = students[i];
        }
    }

    public int countStudentsFromCity(String cityName){
        int count = 0;
        for(int i = 0; i < students.length; i++){
            if(students[i].getCity().equals(cityName)){
                count++;
            }
        }
        return count;
    }

    public Student getStudent(long index){
        Student student = null;
        for(int i = 0; i < students.length; i++){
            if(students[i].index == index){
                student = students[i];
            }
        }
        return student;
    }

    public double getAverageNumberOfContacts(){
        double sum = 0;
        for(int i = 0; i < students.length; i++){
            sum += students[i].contacts.size();
        }
        return sum / students.length;
    }

    public Student getStudentWithMostContacts(){
        Student student = students[0];
        long index = students[0].getIndex();
        for(int i = 0; i < students.length; i++){
            if (students[i].contacts.size() > student.contacts.size() ||
                    (students[i].contacts.size() == student.contacts.size() && students[i].getIndex() > index)) {
                student = students[i];
                index = students[i].getIndex();
            }
        }
        return student;
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("{")
                .append("\"fakultet\":\"").append(name).append("\", ")
                .append("\"studenti\":[");
        for(int i = 0; i < students.length; i++){
            json.append(students[i].toString());
            if(i < students.length - 1){
                json.append(", ");
            }
        }
        json.append("]}");
        return json.toString();
    }
}


public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
