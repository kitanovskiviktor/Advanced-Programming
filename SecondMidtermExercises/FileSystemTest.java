package SecondMidtermExercises;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */

class File implements Comparable<File> {
    String name;
    Integer size;
    LocalDateTime createdAt;

    public File(String name, Integer size, LocalDateTime createdAt){
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public Integer getSize(){
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public int compareTo(File other) {
        int dateComparison = this.createdAt.compareTo(other.createdAt);
        if (dateComparison != 0) return dateComparison;

        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) return nameComparison;

        return Integer.compare(this.size, other.size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File)) return false;
        File file = (File) o;
        return size == file.size && name.equals(file.name) && createdAt.equals(file.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, createdAt);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, createdAt);
    }
}


class FileSystem {

    Map<Character, TreeSet<File>> folders;

    public FileSystem(){
        this.folders = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        File newFile = new File(name, size, createdAt);
        folders.putIfAbsent(folder, new TreeSet<>());
        folders.get(folder).add(new File(name, size, createdAt));
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return folders.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .filter(file -> file.name.startsWith("."))
                .filter(file -> file.size < size)
                .collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character>folders){
        Set<Character> checkFolders = new HashSet<>(folders);

        return this.folders.entrySet().stream()
                .filter(folder -> checkFolders.contains(folder.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .mapToInt(File::getSize)
                .sum();
    }

    public Map<Integer, Set<File>> byYear(){
        return folders.values().stream()
                .flatMap(TreeSet::stream)
                .collect(Collectors.groupingBy(
                        file -> file.getCreatedAt().getYear(),
                        Collectors.toCollection(TreeSet::new)
                ));
    }

    public Map<String, Long> sizeByMonthAndDay(){
        return folders.values().stream()
                .flatMap(TreeSet::stream)
                .collect(Collectors.groupingBy(
                        file -> file.getCreatedAt().getMonth() + "-" + file.getCreatedAt().getDayOfMonth(),
                        Collectors.summingLong(File::getSize)
                ));
    }
}



public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here


