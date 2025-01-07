package SecondMidtermExercises;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Discounts
 */


class Store {
    String name;
    Map<Integer, Integer> products;

    public Store(){
        this.products = new HashMap<>();
    }

    public void setProducts(Integer discount, Integer price){
        this.products.put(discount, price);
    }

    public void setName(String name){
        this.name = name;
    }

    public double getAverageDiscount() {
        return products.entrySet().stream()
                .mapToDouble(
                        entry -> {
                            int discountPrice = entry.getKey();
                            int regularPrice = entry.getValue();
                            return ((regularPrice - discountPrice) / (double) regularPrice) * 100.0;
                        }
                )
                .average()
                .orElse(0);
    }

    public int getTotalDiscount(){
        return products.entrySet().stream()
                .mapToInt(
                        entry -> entry.getValue() - entry.getKey()
                )
                .sum();
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("Average discount: ").append(String.format("%.1f", getAverageDiscount())).append("%\n");
        sb.append("Total discount: ").append(getTotalDiscount()).append("\n");

        List<Map.Entry<Integer, Integer>> sortedProducts = products.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    double discount1 = (entry1.getValue() - entry1.getKey()) / (double) entry1.getValue() * 100;
                    double discount2 = (entry2.getValue() - entry2.getKey()) / (double) entry2.getValue() * 100;
                    return Double.compare(discount2, discount1);
                })
                .collect(Collectors.toList());

        for (int i = 0; i < sortedProducts.size(); i++) {
            Map.Entry<Integer, Integer> entry = sortedProducts.get(i);
            double discount = (entry.getValue() - entry.getKey()) / (double) entry.getValue() * 100;
            sb.append((int) discount).append("% ")
                    .append(entry.getKey()).append("/").append(entry.getValue());

            if (i < sortedProducts.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }


}

class Discounts {

    Map<String, Store> storeMap;

    public Discounts(){
        this.storeMap = new HashMap<>();
    }

    public int readStores(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String[] lines = br.lines().toArray(String[]::new);

        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            String storeName = parts[0];
            Store newStore = new Store();
            newStore.setName(storeName);
            for (int i = 1; i < parts.length; i++) {
                if (!parts[i].isEmpty()) {
                    String[] discountPrice = parts[i].split(":");
                    if (discountPrice.length == 2) {
                        newStore.setProducts(
                                Integer.parseInt(discountPrice[0]),
                                Integer.parseInt(discountPrice[1])
                        );
                    }
                }
            }
            storeMap.put(storeName, newStore);
        }
        return storeMap.size();

    }

    public List<Store> byAverageDiscount() {
        return storeMap.values().stream()
                .sorted(Comparator.comparing(Store::getAverageDiscount).thenComparing(Store::getName).reversed())
                .limit(3)
                .collect(Collectors.toList());

    }

    public List<Store> byTotalDiscount() {
        return storeMap.values().stream()
                .sorted(Comparator.comparing(Store::getTotalDiscount).thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }




}



public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde