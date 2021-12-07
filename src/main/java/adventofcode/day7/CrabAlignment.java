package adventofcode.day7;

import java.util.List;
import java.util.Scanner;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class CrabAlignment {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(CrabAlignment.class.getResourceAsStream("input.txt"));
        List<Integer> crabPositions = Stream.of(scanner.nextLine().split(","))
                .map(Integer::parseInt)
                .toList();


        int min = crabPositions.stream().mapToInt(v -> v).min().getAsInt();
        int max = crabPositions.stream().mapToInt(v -> v).max().getAsInt();

        long fuel1 = 999999999;
        long fuel2 = 999999999;
        for (int i = min; i < max; i++) {
            final int index = i;
            long currentFuel1 = crabPositions.stream().mapToLong(v -> Math.abs(v - index)).sum();
            fuel1 = Math.min(fuel1, currentFuel1);

            long currentFuel2 = crabPositions.stream().mapToLong(v -> LongStream.rangeClosed(1, Math.abs(v - index)).sum()).sum();
            fuel2 = Math.min(fuel2, currentFuel2);
        }
        System.out.println(fuel1);
        System.out.println(fuel2);
    }
}
