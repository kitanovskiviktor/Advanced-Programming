package SecondMidtermExercises;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Payment {
    String index;
    Map<String, Integer> paymentMap;
    int bankFee;

    public Payment(){
        this.paymentMap = new HashMap<>();
    }

    public void setIndex(String index){
        this.index = index;
    }

    public void addPayment(String description, Integer price){
        paymentMap.put(description, price);
    }

    int getBankFee(){
        int total = paymentMap.values().stream().mapToInt(Integer::intValue).sum();
        double fee = total * 0.0114;
        if(fee < 3){
            fee = 3;
        }
        else if(fee > 300){
            fee = 300;
        }
        return (int) Math.round(fee);
    }

    int totalNet(){
        return paymentMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Student: " + index + " Net: " + totalNet() + " Fee: " + getBankFee() + " Total: " + (totalNet() + getBankFee()));
        sb.append("\nItems: \n");
        int[] z = {1};
        paymentMap.entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    int valueComparison = e2.getValue().compareTo(e1.getValue());
                    return (valueComparison != 0) ? valueComparison : e2.getKey().compareTo(e1.getKey());
                })
                .forEach(entry -> sb.append(z[0]++).append(". ").append(entry.getKey()).append(" ").append(entry.getValue()).append("\n"));
        if (!paymentMap.isEmpty()) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }
}

class OnlinePayments {

    Map<String, Payment> studentPayments;

    public OnlinePayments(){
        this.studentPayments = new HashMap<>();
    }

    public void readItems(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String [] lines = br.lines().toArray(String[]::new);

        for (String line : lines) {
            String[] parts = line.split(";");
            String index = parts[0];
            String description = parts[1];
            Integer price = Integer.valueOf(parts[2]);
            studentPayments.computeIfAbsent(index, v -> new Payment()).addPayment(description, price);
            studentPayments.get(index).setIndex(index);
        }
    }

    public void printStudentReport(String index, OutputStream os){
        Payment payment = studentPayments.get(index);
        if(payment == null){
            System.out.println("Student " + index + " not found!");
            return;
        }
        System.out.println(payment.toString());
    }

}


public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}
