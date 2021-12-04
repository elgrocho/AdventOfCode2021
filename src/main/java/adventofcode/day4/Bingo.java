package adventofcode.day4;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Bingo {
    public static void main(String[] args) {
        Data data = getData();

        Set<Integer> markedNumbers = new HashSet<>();
        WinBoard firstWinningBoard = null;
        WinBoard lastWinningBoard = null;
        List<Board> remainingBoards = new ArrayList<>(data.boards());
        for (Integer number : data.numbers()) {
            markedNumbers.add(number);
            List<Board> winBoards = getWiningBoards(remainingBoards, markedNumbers);
            if (!winBoards.isEmpty()) {
                remainingBoards.removeAll(winBoards);
                lastWinningBoard = new WinBoard(winBoards, new HashSet<>(markedNumbers), number);
                if (firstWinningBoard == null) {
                    firstWinningBoard = lastWinningBoard;
                }
            }
        }
        System.out.println("First: " + getTotal(firstWinningBoard));
        System.out.println("Last: " + getTotal(lastWinningBoard));
    }

    private static int getTotal(WinBoard winBoard) {
        return winBoard.boards().stream()
                .mapToInt(board -> getBoardTotal(board, winBoard.numbers(), winBoard.lastNumber()))
                .max()
                .orElseThrow();
    }

    private static int getBoardTotal(Board board, Set<Integer> numbers, int lastNumber) {
        Set<Integer> allNumbers = board.getAll();
        allNumbers.removeAll(numbers);
        int sum = allNumbers.stream().mapToInt(v -> v).sum();
        return sum * lastNumber;
    }

    private static List<Board> getWiningBoards(List<Board> boards, Set<Integer> currentNumbers) {
        return boards.stream().filter(board -> board.match(currentNumbers)).collect(Collectors.toList());
    }

    private static Data getData() {
        InputStream inputStream = Bingo.class.getResourceAsStream("input.txt");
        Scanner scanner = new Scanner(inputStream);
        List<Integer> numbers = Stream.of(scanner.next().split(",")).map(Integer::parseInt).toList();

        List<Board> boards = new ArrayList<>();
        int size = 5;
        while (scanner.hasNext()) {

            int[][] currentBoard = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++)
                    currentBoard[i][j] = scanner.nextInt();
            }
            boards.add(new Board(currentBoard));
        }
        return new Data(numbers, boards);
    }

    record Data(List<Integer> numbers, List<Board> boards) {
    }

    record WinBoard(List<Board> boards, Set<Integer> numbers, int lastNumber) {
    }

    ;

    static class Board {
        List<Set<Integer>> rows;
        List<Set<Integer>> columns;

        public Board(int[][] board) {
            rows = Arrays.stream(board).map(this::toSet).collect(Collectors.toList());
            columns = IntStream.range(0, board.length)
                    .mapToObj(i -> toSet(getColumn(board, i)))
                    .collect(Collectors.toList());
        }

        private Set<Integer> toSet(int[] ints) {
            return Arrays.stream(ints).boxed().collect(Collectors.<Integer>toSet());
        }

        int[] getColumn(int[][] matrix, int column) {
            return IntStream.range(0, matrix.length)
                    .map(i -> matrix[i][column]).toArray();
        }

        public boolean match(Set<Integer> currentNumbers) {
            return Stream.concat(rows.stream(), columns.stream())
                    .anyMatch(currentNumbers::containsAll);
        }

        public Set<Integer> getAll() {
            return rows.stream().flatMap(Collection::stream).collect(Collectors.toSet());
        }
    }
}
