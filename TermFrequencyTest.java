package com.example.np_lab2_zad1;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class TermFrequency {

    Map<String, Integer> frequencyMap;
    Set<String> stopWordsSet;
    private Integer totalWords = 0;

    public TermFrequency(InputStream inputStream, String[] stopWords){
        this.stopWordsSet = new HashSet<>();
        stopWordsSet.addAll(List.of(stopWords));

        this.frequencyMap = new HashMap<>();

        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNext()){
            String word = scanner.next();
            word = word.toLowerCase().replace(',', '\0').replace('.', '\0').trim();
            if(!(word.isEmpty() || stopWordsSet.contains(word))){
                int counter = frequencyMap.computeIfAbsent(word, value -> 0);
                frequencyMap.put(word, ++counter);
                totalWords++;
            }
        }
    }

    public int countTotal(){
        return totalWords;
    }

    public List<String> mostOften(int k) {
        return frequencyMap.entrySet().stream()
                .sorted((word1, word2) -> (int) word2.getValue() == word1.getValue() ? (word1.getKey().compareTo(word2.getKey())) : Integer.compare(word2.getValue(), word1.getValue()))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public int countDistinct(){
        return frequencyMap.size();
    }
}



public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
