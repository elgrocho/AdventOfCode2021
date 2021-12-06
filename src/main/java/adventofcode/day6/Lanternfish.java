package adventofcode.day6;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Lanternfish {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(Lanternfish.class.getResourceAsStream("input.txt"));
        List<Integer> initialState = Stream.of(scanner.nextLine().split(","))
                .map(Integer::parseInt)
                .toList();

        Map<Integer, Long> fish = new HashMap<>();
        IntStream.range(0, 9).forEach(value -> fish.put(value, 0l));

        for (Integer timer : initialState) {
            fish.put(timer, fish.get(timer) + 1);
        }

        Map<Integer, Long> currentDay = new HashMap<>(fish);
        for (int i = 0; i < 256; i++) {
            Map<Integer, Long> nextDay = new HashMap<>();
            nextDay.put(8, currentDay.get(0));
            nextDay.put(7, currentDay.get(8));
            nextDay.put(6, currentDay.get(7) + currentDay.get(0));
            nextDay.put(5, currentDay.get(6));
            nextDay.put(4, currentDay.get(5));
            nextDay.put(3, currentDay.get(4));
            nextDay.put(2, currentDay.get(3));
            nextDay.put(1, currentDay.get(2));
            nextDay.put(0, currentDay.get(1));
            currentDay = nextDay;
        }

        System.out.println(currentDay.values().stream().mapToLong(v -> v).sum());
    }
}
