package adventofcode.day3;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinaryDiagnostics{
    public static void main(String[] args) {
        InputStream inputStream = BinaryDiagnostics.class.getResourceAsStream("binarydiagnostics.txt");
        Scanner scanner = new Scanner(inputStream);
        List<String> diagnostics = new ArrayList<>();
        while (scanner.hasNext()) {
            diagnostics.add(scanner.next());
        }
        int size = diagnostics.get(0).length();
        String gamma = IntStream.range(0, size)
                .mapToObj(index -> calculateMostCommon(diagnostics, index) >= 0 ? "1" : "0")
                .collect(Collectors.joining());

        String epsilon = IntStream.range(0, size)
                .mapToObj(index -> calculateMostCommon(diagnostics, index) >= 0 ? "0" : "1")
                .collect(Collectors.joining());

        int gammaFinal = Integer.parseInt(gamma, 2);
        int epsilonFinal = Integer.parseInt(epsilon, 2);
        System.out.println(gamma);
        System.out.println(gammaFinal);
        System.out.println(epsilon);
        System.out.println(epsilonFinal);
        System.out.println(gammaFinal * epsilonFinal);

        String o2 = calculateParam(diagnostics, size, value -> value >= 0 ? '1' : '0');
        String co2 = calculateParam(diagnostics, size, value -> value >= 0 ? '0' : '1');
        System.out.println("oxygen: " + o2);
        System.out.println("co2: " + co2);
        System.out.println(Integer.parseInt(o2,2) * Integer.parseInt(co2, 2));
    }

    private static String calculateParam(List<String> diagnostics, int size, Function<Integer, Character> toBitChar) {
        String param = null;
        for(int i = 0; i< size; i++) {
            char mostCommonChar = toBitChar.apply(calculateMostCommon(diagnostics, i));
            final int index = i;
            diagnostics = diagnostics.stream()
                    .filter(v -> v.charAt(index) == mostCommonChar)
                    .collect(Collectors.toList());
            if (diagnostics.size() == 1) {
                param = diagnostics.get(0);
                break;
            }
        }
        return param;
    }

    private static int calculateMostCommon(List<String> instructions, int index) {
        int sum = instructions.stream()
                .map(instruction -> instruction.charAt(index))
                .mapToInt(character -> character == '1' ? 1 : -1)
                .sum();
        return Integer.compare(sum, 0);
    }
}
