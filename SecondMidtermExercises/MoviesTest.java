package SecondMidtermExercises;

import java.util.*;
import java.util.stream.Collectors;

class Movie {
    String name;
    List<Integer> ratings;

    int maxRating;

    public Movie(String name, int[] ratings){
        this.name = name;
        this.ratings = new ArrayList<>();
        this.ratings.addAll(Arrays.stream(ratings).boxed().collect(Collectors.toList()));
    }

    public String getName(){
        return name;
    }

    public double getAverageRating(){
        return ratings.stream().mapToDouble(rating -> rating)
                .average()
                .orElse(0);
    }

    public double getRatingCoef(){
        return getAverageRating() * ratings.size() / maxRating;
    }

    public void setMaxRating(int maxRating){
        this.maxRating = maxRating;
    }

    public int ratingsSize(){
        return ratings.size();
    }

    @Override
    public String toString(){
        return name + " (" + String.format("%.2f", getAverageRating()) + ")" + " of " + ratings.size() + " ratings";
    }
}

class MoviesList {
    List<Movie> movies;

    public MoviesList(){
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }
    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted(
                        Comparator.comparing(Movie::getAverageRating)
                                .reversed()
                                .thenComparing(Comparator.comparing(Movie::ratingsSize).reversed())
                                .thenComparing(Movie::getName).reversed()
                )
                .limit(10)
                .collect(Collectors.toList());
    }


    public List<Movie> top10ByRatingCoef(){

        int maxRating = movies.stream().map(Movie::ratingsSize).max(Integer::compare).orElse(0);
        movies.forEach(movie -> movie.setMaxRating(maxRating));

        return movies.stream()
                .sorted(Comparator.comparing(Movie::getRatingCoef).thenComparing(Movie::ratingsSize).thenComparing(Movie::getName).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde