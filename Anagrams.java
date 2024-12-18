import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {

        HashMap<String, List<String>> wordsMap = new HashMap<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> wordsList = List.of(br.lines().toArray(String[]::new));

        for (String word : wordsList) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String sortedWords = new String(chars);
            wordsMap.putIfAbsent(sortedWords, new ArrayList<>());
            wordsMap.get(sortedWords).add(word);
        }

        List<List<String>> resultList = new ArrayList<>();

        for (List<String> wordsGroup : wordsMap.values()) {
            if (wordsGroup.size() >= 5) {
                Collections.sort(wordsGroup);
                resultList.add(wordsGroup);
            }
        }

        resultList.sort(Comparator.comparing(group -> wordsList.indexOf(group.get(0))));

        for (List<String> group : resultList) {
            System.out.println(String.join(" ", group));
        }


    }
}
