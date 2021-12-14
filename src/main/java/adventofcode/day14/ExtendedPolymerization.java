package adventofcode.day14;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ExtendedPolymerization {
    public static void main(String[] args) {
        InputStream inputStream = ExtendedPolymerization.class.getResourceAsStream("input.txt");
        Scanner scanner = new Scanner(inputStream);
        Map<String, Pairs> extensionMap = new HashMap<>();
        String polymer = scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] values = scanner.nextLine().split(" -> ");
            String pair = values[0];
            extensionMap.put(pair, new Pairs(pair.charAt(0) + values[1], values[1] + pair.charAt(1)));
        }

        Map<String, Long> pairsCounters = new HashMap<>();

        long start = System.currentTimeMillis();
        List<String> initialPairs = IntStream.range(1, polymer.length())
                .mapToObj(index -> String.valueOf(polymer.charAt(index - 1)) + polymer.charAt(index))
                .toList();
        initialPairs.forEach(pair -> countCharacters(pair, pairsCounters, extensionMap, 1, 30));

        long seconds = (System.currentTimeMillis() - start)/1000;
        System.out.println("Done in seconds:" + seconds);
//        var maxEntry = charactersMap.entrySet().stream().max(Map.Entry.comparingByValue()).get();
//        var minEntry = charactersMap.entrySet().stream().min(Map.Entry.comparingByValue()).get();
//        System.out.println("max: " + maxEntry.getKey() + " " + maxEntry.getValue());
//        System.out.println("min: " + minEntry.getKey() + " " + minEntry.getValue());
//        System.out.println(maxEntry.getValue() - minEntry.getValue());
    }

    private static void countCharacters(String pair,
                                        Map<String, Long> pairsCounters,
                                        Map<String, Pairs> extensionMap,
                                        int stepNo,
                                        int maxSteps) {

        long value = pairsCounters.getOrDefault(pair, 0l) + 1;
        pairsCounters.put(pair, value);
        Pairs newPairs = extensionMap.get(pair);
        if (newPairs == null) return;
        if (stepNo == maxSteps) return;
        stepNo = stepNo + 1;
        countCharacters(newPairs.a(), pairsCounters, extensionMap, stepNo, maxSteps);
        countCharacters(newPairs.b(), pairsCounters, extensionMap, stepNo, maxSteps);
    }

    record Pairs(String a, String b){}
}
