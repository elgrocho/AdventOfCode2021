package adventofcode.day14;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

public class ExtendedPolymerization {
    public static void main(String[] args) {
        InputStream inputStream = ExtendedPolymerization.class.getResourceAsStream("input.txt");
        Scanner scanner = new Scanner(inputStream);
        Map<String, Pairs> extensionMapping = new HashMap<>();
        String polymer = scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] values = scanner.nextLine().split(" -> ");
            String pair = values[0];
            extensionMapping.put(pair, new Pairs(pair.charAt(0) + values[1], values[1] + pair.charAt(1)));
        }

        Map<String, Long> pairCounters = IntStream.range(1, polymer.length())
                .mapToObj(index -> String.valueOf(polymer.charAt(index - 1)) + polymer.charAt(index))
                .collect(groupingBy(v -> v, reducing(0L, v -> 1L, Long::sum)));

        for(int i=0; i< 40; i++) {
            pairCounters = getNewPairs(pairCounters, extensionMapping);
        }

        Map<Character, Long> charactersMap = countCharacters(polymer, pairCounters);

        var maxEntry = charactersMap.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        var minEntry = charactersMap.entrySet().stream().min(Map.Entry.comparingByValue()).get();
        System.out.println("max: " + maxEntry.getKey() + " " + maxEntry.getValue());
        System.out.println("min: " + minEntry.getKey() + " " + minEntry.getValue());
        System.out.println(maxEntry.getValue() - minEntry.getValue());
    }

    private static Map<Character, Long> countCharacters(String polymer, Map<String, Long> pairCounters) {
        Set<Character> allCharacters = pairCounters.keySet().stream()
                .flatMap(v -> v.chars().mapToObj(c -> (char) c))
                .collect(Collectors.toSet());
        Map<Character, Long> charactersMap = new HashMap<>();
        for (Character character : allCharacters) {
            long characterCounter = polymer.endsWith(String.valueOf(character)) ? 1 : 0;
            for (Map.Entry<String, Long> entry : pairCounters.entrySet()) {
                if (entry.getKey().charAt(0) == character) {
                    characterCounter = characterCounter + entry.getValue();
                }
            }
            charactersMap.put(character, characterCounter);
        }
        return charactersMap;
    }

    private static Map<String, Long> getNewPairs(Map<String, Long> pairCounters, Map<String, Pairs> extensionMap) {
        Map<String, Long> map = new HashMap<>();
        for (Map.Entry<String, Long> entry : pairCounters.entrySet()) {
            Pairs pairs = extensionMap.get(entry.getKey());
            map.compute(pairs.a(), (key, value) -> value == null ? entry.getValue() : value + entry.getValue());
            map.compute(pairs.b(), (key, value) -> value == null ? entry.getValue() : value + entry.getValue());
        }
        return map;
    }

    record Pairs(String a, String b){}
}