package adventofcode.day10;

import java.io.InputStream;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class SyntaxScoring {

    private static final Map<Character, Integer> SCORES_1 = Map.of(
            ')', 3,
            ']', 57,
            '}', 1197,
            '>', 25137
    );

    private static final Map<Character, Integer> SCORES_2 = Map.of(
            '(', 1,
            '[', 2,
            '{', 3,
            '<', 4
    );

    private static final Map<Character, Character> BRACKETS = Map.of(
            '(',')',
            '[',']',
            '{','}',
            '<','>'
    );

    private static final Map<Character, Character> INVERTED_BRACKETS = BRACKETS.entrySet().stream()
            .collect(toMap(Map.Entry::getValue, Map.Entry::getKey));

    public static void main(String[] args) {
        InputStream inputStream = SyntaxScoring.class.getResourceAsStream("example.txt");
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        //part1
        int resul1 = lines.stream()
                .map(SyntaxScoring::getInvalidSymbol)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(SCORES_1::get)
                .sum();
        System.out.println(resul1);

        //part2
        List<Long> results = lines.stream()
                .filter(line -> getInvalidSymbol(line).isEmpty())
                .map(SyntaxScoring::getLineScore)
                .sorted()
                .toList();
        System.out.println(results.get(results.size()/2));
    }

    private static long getLineScore(String line) {
        Deque<Character> stack = new LinkedList<>();
        for (char symbol : line.toCharArray()) {
            if(stack.isEmpty() || BRACKETS.get(symbol) != null) {
                stack.push(symbol);
                continue;
            }
            if (INVERTED_BRACKETS.get(symbol) == stack.peek())
                stack.pop();
        }

        long result = 0;
        while(!stack.isEmpty()) {
            Character symbol = stack.pop();
            result = result * 5 + SCORES_2.get(symbol);
        }

        return result;
    }

    private static Optional<Character> getInvalidSymbol(String line) {
        Deque<Character> stack = new LinkedList<>();
        for (char symbol : line.toCharArray()) {
            if(stack.isEmpty() || BRACKETS.get(symbol) != null) {
                stack.push(symbol);
                continue;
            }
            if (INVERTED_BRACKETS.get(symbol) == stack.peek())
                stack.pop();
            else
                return Optional.of(symbol);
        }
        return Optional.empty();
    }
}
