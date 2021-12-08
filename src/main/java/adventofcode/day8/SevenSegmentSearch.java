package adventofcode.day8;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class SevenSegmentSearch {

    public static final Map<Integer, Integer> DIGIT_TO_SEGMENTS_NO = Map.ofEntries(
            Map.entry(0, 6),
            Map.entry(1, 2),
            Map.entry(2, 5),
            Map.entry(3, 5),
            Map.entry(4, 4),
            Map.entry(5, 5),
            Map.entry(6, 6),
            Map.entry(7, 3),
            Map.entry(8, 7),
            Map.entry(9, 6)
    );

    public static void main(String[] args) {
        InputStream inputStream = SevenSegmentSearch.class.getResourceAsStream("input.txt");
        List<InputOutput> inputOutputs = new ArrayList<>();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split("\\|");
            List<String> digits = Arrays.stream(split[0].split(" "))
                    .map(v -> v.replace(" ", ""))
                    .map(SevenSegmentSearch::sort)
                    .toList();
            List<String> output = Arrays.stream(split[1].split(" "))
                    .map(v -> v.replace(" ", ""))
                    .filter(v -> v != null && !v.equalsIgnoreCase(""))
                    .map(SevenSegmentSearch::sort)
                    .toList();
            inputOutputs.add(new InputOutput(digits, output));
        }

        //first part
        Map<Integer, List<String>> segmentsNoToCodes = inputOutputs.stream()
                .flatMap(v -> v.output().stream())
                .collect(Collectors.groupingBy(String::length));
        System.out.println(segmentsNoToCodes.get(DIGIT_TO_SEGMENTS_NO.get(1)).size()
                + segmentsNoToCodes.get(DIGIT_TO_SEGMENTS_NO.get(4)).size()
                + segmentsNoToCodes.get(DIGIT_TO_SEGMENTS_NO.get(7)).size()
                + segmentsNoToCodes.get(DIGIT_TO_SEGMENTS_NO.get(8)).size());

        //second part
        int result = inputOutputs.stream()
                .mapToInt(inputOutput -> {
                    Map<String, Integer> codeToDigit = decode(inputOutput.digits());
                    String resultString = inputOutput.output().stream()
                            .map(output -> codeToDigit.get(output).toString())
                            .collect(Collectors.joining());
                    return Integer.parseInt(resultString);
                })
                .sum();
        System.out.println(result);
    }

    private static Map<String, Integer> decode(List<String> input) {
        Map<Integer, List<String>> segmentsNoToCodes = input.stream()
                .collect(Collectors.groupingBy(String::length));

        String one = segmentsNoToCodes.get(DIGIT_TO_SEGMENTS_NO.get(1)).get(0);
        String four = segmentsNoToCodes.get(DIGIT_TO_SEGMENTS_NO.get(4)).get(0);
        String seven = segmentsNoToCodes.get(DIGIT_TO_SEGMENTS_NO.get(7)).get(0);
        String eight = segmentsNoToCodes.get(DIGIT_TO_SEGMENTS_NO.get(8)).get(0);

        //9 - 6/9/0 have 6 elements, but only 9 will have 4 inside
        String nine = segmentsNoToCodes.get(6).stream()
                .filter(v -> contains(v, four))
                .findFirst().get();

        //6 - 6 element which does not contain 1
        String six = segmentsNoToCodes.get(6).stream()
                .filter(v -> !contains(v, one))
                .findFirst().get();

        //0 - 6 element but not 6 or 9
        String zero = segmentsNoToCodes.get(6).stream()
                .filter(v -> !v.equals(six) && !v.equals(nine))
                .findFirst().get();
        //3 - 5 element and only this one contains 1
        String three = segmentsNoToCodes.get(5).stream()
                .filter(v -> contains(v, one))
                .findFirst().get();
        //5 - 5 element and 6 contains 5
        String five = segmentsNoToCodes.get(5).stream()
                .filter(v -> contains(six, v))
                .findFirst().get();
        //2 - 5 element and not 3 or 5
        String two = segmentsNoToCodes.get(5).stream()
                .filter(v -> !v.equals(three) && !v.equals(five))
                .findFirst().get();

        return Map.ofEntries(
                Map.entry(zero, 0),
                Map.entry(one, 1),
                Map.entry(two, 2),
                Map.entry(three, 3),
                Map.entry(four, 4),
                Map.entry(five, 5),
                Map.entry(six, 6),
                Map.entry(seven, 7),
                Map.entry(eight, 8),
                Map.entry(nine, 9));
    }

    private static boolean contains(String value1, String value2) {
        for (char character : value2.toCharArray()) {
            if (!value1.contains(String.valueOf(character))) {
                return false;
            }
        }
        return true;
    }

    private static String subtract(String value1, String value2) {
        String newValue = value1;
        for (char character : value2.toCharArray()) {
            newValue = newValue.replace(String.valueOf(character), "");
        }
        return newValue;
    }

    private static String sort(String value) {
        return value.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    record InputOutput(List<String> digits, List<String> output) {
    }
}
