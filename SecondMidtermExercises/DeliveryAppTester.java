

import java.util.*;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class Person {
    String id;
    String name;
    Location currentLocation;

    int totalProfit;
    int totalDeliveries;

    public Person(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.totalProfit = 0;
        this.totalDeliveries = 0;
    }

    public int getTotalDeliveries(){
        return totalDeliveries;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setDelivery(Location other){
        int additional = (currentLocation.distance(other) / 10) * 10;
        totalProfit += additional + 90;
        totalDeliveries++;
    }

    public int getTotalProfit(){
        return totalProfit;
    }

    float getAverageFee(){
        if(totalDeliveries == 0){
            return 0;
        }
        return (float) totalProfit / totalDeliveries;
    }

    public String getId(){
        return id;
    }

    @Override
    public String toString(){
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f", id, name, totalDeliveries, (float)totalProfit, getAverageFee());
    }
}

class Restaurant {
    String id;
    String name;
    Location location;

    float totalPrice;
    int totalOrders;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalPrice = 0;
        this.totalOrders = 0;
    }

    public Location getLocation(){
        return  location;
    }

    public void setOrders(){
        totalOrders++;
    }

    public void setPrice(float price){
        totalPrice += price;
    }

    public float getAveragePrice(){
        if(totalOrders == 0){
            return 0;
        }
        return totalPrice / totalOrders;
    }

    public String getId(){
        return id;
    }

    @Override
    public String toString(){
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f", id, name, totalOrders, totalPrice, getAveragePrice());
    }
}

class User {
    String id;
    String name;
    Map<String, Location> addresses;

    float totalPrice;
    int totalOrders;
    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.addresses = new HashMap<>();
        this.totalPrice = 0;
        this.totalOrders = 0;
    }

    public void addAddress(String name, Location location){
        this.addresses.put(name, location);
    }

    public void setPrice(float price){
        this.totalPrice+=price;
    }

    public float getTotalPrice(){
        return totalPrice;
    }

    public void setTotalOrders(){
        totalOrders++;
    }

    public float getAveragePrice(){
        if(totalOrders == 0){
            return 0;
        }
        return totalPrice / totalOrders;
    }

    public String getId(){
        return id;
    }
    @Override
    public String toString(){
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f", id, name, totalOrders, totalPrice, getAveragePrice());
    }

}


class DeliveryApp {

    String name;
    Set<Person> deliveryPeople;
    Set<Restaurant> restaurants;
    Set<User> users;

    public DeliveryApp(String name) {
        this.name = name;
        this.deliveryPeople = new HashSet<>();
        this.restaurants = new HashSet<>();
        this.users = new HashSet<>();
    }

    public void registerDeliveryPerson(String id, String name, Location currentLocation){
        Person newPerson = new Person(id,name, currentLocation);
        this.deliveryPeople.add(newPerson);
    }

    public void addRestaurant(String id, String name, Location currentLocation){
        Restaurant newRestaurant = new Restaurant(id, name, currentLocation);
        this.restaurants.add(newRestaurant);
    }

    public void addUser(String id, String name){
        User newUser = new User(id, name);
        this.users.add(newUser);
    }

    public void addAddress(String id, String addressName, Location location){
        users.stream().filter(v -> v.id.equals(id)).findFirst().get().addAddress(addressName, location);
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost){
        User getUser = users.stream().filter(user -> user.id.equals(userId)).findFirst().get();
        Location userLocation = getUser.addresses.get(userAddressName);
        Restaurant getRestaurant = restaurants.stream().filter(r -> r.id.equals(restaurantId)).findFirst().get();
        Person getPerson = deliveryPeople.stream()
                .min(Comparator.comparing((Person x) -> x.currentLocation.distance(getRestaurant.getLocation()))
                        .thenComparing(Person::getTotalDeliveries).thenComparing(Person::getId))
                .get();

        getRestaurant.setOrders();
        getRestaurant.setPrice(cost);
        getPerson.setDelivery(userLocation);
        getPerson.currentLocation = userLocation;
        getUser.setPrice(cost);
        getUser.setTotalOrders();
    }

    public void printUsers(){
        users.stream()
                .sorted(Comparator.comparing(User::getTotalPrice).thenComparing(User::getId).reversed())
                .forEach(System.out::println);
    }

    public void printRestaurants(){
        restaurants.stream()
                .sorted(Comparator.comparing(Restaurant::getAveragePrice).thenComparing(Restaurant::getId).reversed())
                .forEach(System.out::println);
    }

    public void printDeliveryPeople(){
        deliveryPeople.stream()
                .sorted(Comparator.comparing(Person::getTotalProfit).reversed())
                .forEach(System.out::println);
    }
}


public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}

