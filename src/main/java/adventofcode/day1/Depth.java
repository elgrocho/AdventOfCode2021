package adventofcode.day1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Depth {
    public static void main(String[] args) {
        InputStream inputStream = Depth.class.getResourceAsStream("depth.txt");
        Scanner scanner = new Scanner(inputStream);
        List<Integer> depthMeasurements = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                depthMeasurements.add(scanner.nextInt());
            } else {
                scanner.next();
            }
        }

        long depthIncreasedCount1 = IntStream.range(1, depthMeasurements.size())
                .filter(i -> depthMeasurements.get(i) > depthMeasurements.get(i-1))
                .count();
        System.out.println(depthIncreasedCount1);

        long depthIncreasedCount2 = IntStream.range(3, depthMeasurements.size())
                .filter(i -> depthMeasurements.get(i) > depthMeasurements.get(i-3))
                .count();
        System.out.println(depthIncreasedCount2);
    }
}
