package SecondMidtermExercises;

import java.util.*;
import java.util.stream.Collectors;


class User {
    String id;
    String username;

    public User(String id, String username){
        this.id = id;
        this.username = username;
    }
}

class Rating {
    String userId;
    String movieId;
    int rating;

    public Rating(){

    }

    public Rating(String userId, String movieId, int rating){
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public int getRating(){
        return rating;
    }

    public String getMovieId(){
        return movieId;
    }



}

class Movie {
    String id;
    String name;

    Set<Rating> ratings;

    public Movie(){
        this.ratings = new HashSet<>();
    }

    public Movie(String id, String name){
        this.id = id;
        this.name = name;
        this.ratings = new HashSet<>();
    }

    public void setRating(Rating newRating){
        this.ratings.add(newRating);
    }

    public double getAverageRating(){
        int sum = ratings.stream().mapToInt(rating -> rating.rating).sum();
        return (double) sum / ratings.size();
    }

    @Override
    public String toString(){
        return String.format("Movie ID: %s Title: %s Rating: %.2f", id, name, getAverageRating());
    }
}

class StreamingPlatform {
    Set<Movie> movieSet;
    Map<String, List<Rating>> userRatingMap;
    Set<User> userSet;

    public StreamingPlatform(){
        this.movieSet = new HashSet<>();
        this.userRatingMap = new HashMap<>();
        this.userSet = new HashSet<>();
    }

    public void addMovie(String id, String name){
        this.movieSet.add(new Movie(id, name));
    }

    public void addUser(String id, String username){
        this.userRatingMap.computeIfAbsent(id, k -> new ArrayList<>());
        this.userSet.add(new User(id, username));
    }

    public void addRating(String userId, String movieId, int rating){
        Rating newRating = new Rating(userId, movieId, rating);
        this.movieSet.stream().filter(movie -> movie.id.equals(movieId))
                .findFirst()
                .get()
                .ratings.add(newRating);

        userRatingMap.get(userId).add(newRating);
    }

    public void topNMovies(int n){
        this.movieSet.stream()
                .sorted(Comparator.comparing(Movie::getAverageRating).reversed())
                .limit(n)
                .forEach(System.out::println);
    }
    public void favouriteMoviesForUsers(List<String> userIds){
        for(String userId : userIds){
            List<Rating> ratingsByUser = userRatingMap.get(userId);

            int maxRating = ratingsByUser.stream()
                    .mapToInt(Rating::getRating)
                    .max().orElse(0);

            List<Movie> getMovies = ratingsByUser.stream()
                    .filter(r -> r.getRating() == maxRating)
                    .map(Rating::getMovieId)
                    .map(movieId -> movieSet.stream()
                            .filter(m -> m.id.equals(movieId))
                            .findFirst()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            System.out.println("User ID: " + userId + " Name: " + userSet.stream().filter(user -> user.id.equals(userId)).findFirst().get().username);
            getMovies.stream().sorted(Comparator.comparing(Movie::getAverageRating).reversed()).forEach(System.out::println);
            System.out.println();
        }
    }

    //Not implemented
    public void similarUsers(String userId) {
    }
}



class CosineSimilarityCalculator {

    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}


public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id ,name);
            } else if (parts[0].equals("addUser")){
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id ,name);
            } else if (parts[0].equals("addRating")){
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")){
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}

