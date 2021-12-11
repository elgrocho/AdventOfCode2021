package adventofcode.day11;

import adventofcode.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DumboOctopus {
    public static void main(String[] args) {
        int[][] matrix = Util.getMatrix(DumboOctopus.class.getResourceAsStream("input.txt"));

        //part 1
        long part1Result = 0;
        for(int i=0; i < 100; i++) {
            long currentFlashes = calculateStepFlashes(matrix);
            part1Result = part1Result + currentFlashes;
        }
        System.out.println(part1Result);

        //part 2
        int[][] matrix2 = Util.getMatrix(DumboOctopus.class.getResourceAsStream("input.txt"));
        int matrixSize = matrix2.length * matrix2[0].length;
        boolean notAllFlashed = true;
        int step = 0;
        while(notAllFlashed) {
            step++;
            long currentFlashes = calculateStepFlashes(matrix2);
            if(currentFlashes == matrixSize) {
                notAllFlashed = false;
            }
        }
        System.out.println(step);
    }

    private static long calculateStepFlashes(int[][] matrix) {
        matrix = increaseBy1(matrix);

        Set<Point> flashPoints = new HashSet<>();
        //get initial set of flash points
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                Point point = new Point(x, y);
                if(value(point, matrix) > 9) {
                    flashPoints.add(point);
                }
            }
        }
        //start processing flash points
        Deque<Point> stack = new LinkedList<>(flashPoints);
        while(!stack.isEmpty()) {
            Point flashPoint = stack.pop();
            for (Point adjacentPoint : getAdjacentPoints(flashPoint, matrix)) {
                int value = increment(matrix, adjacentPoint);
                if(!flashPoints.contains(adjacentPoint) && value > 9) {
                    flashPoints.add(adjacentPoint);
                    stack.add(adjacentPoint);
                }
            }
        }
        //set flash points to 0
        for (Point flashPoint : flashPoints) {
            matrix[flashPoint.y()][flashPoint.x()] = 0;
        }

        return flashPoints.size();
    }

    private static int increment(int[][] matrix, Point adjacentPoint) {
        int value = value(adjacentPoint, matrix) + 1;
        matrix[adjacentPoint.y()][adjacentPoint.x()] = value;
        return value;
    }

    private static List<Point> getAdjacentPoints(Point point, int[][] matrix) {
        return Stream.of(
                new Point(point.x() -1, point.y() -1),
                new Point(point.x(), point.y() - 1),
                new Point(point.x() + 1, point.y() - 1),
                new Point(point.x() - 1, point.y()),
                new Point(point.x(), point.y()),
                new Point(point.x() + 1, point.y()),
                new Point(point.x() - 1, point.y() + 1),
                new Point(point.x(), point.y() + 1),
                new Point(point.x() + 1, point.y() + 1)
        )
                .filter(p -> withinMatrix(p, matrix))
                .collect(Collectors.toList());
    }

    private static boolean withinMatrix(Point point, int[][] matrix) {
        int maxY = matrix.length;
        int maxX = matrix[0].length;
        return point.x() >= 0 && point.x() < maxX && point.y() >= 0 && point.y() < maxY;
    }

    private static int[][] increaseBy1(int[][] matrix) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                matrix[y][x] = matrix[y][x] + 1;
            }
        }
        return matrix;
    }

    private static int value(Point p, int[][] matrix) {
        return matrix[p.y()][p.x()];
    }

    record Point(int x, int y){}
}
