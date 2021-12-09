package adventofcode.day9;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

public class SmokeBasin {
    public static void main(String[] args) {
        int[][] map = getData();

        int maxY = map.length;
        int maxX = map[0].length;
        List<Point> lowestPoints = new ArrayList<>();
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                Point point = new Point(x, y);
                boolean lowestPoint = getAdjacentPoints(point)
                        .filter(adjacentPoint -> withinMap(map, adjacentPoint))
                        .allMatch(adjacentPoint -> getValue(map, point) < getValue(map, adjacentPoint));
                if(lowestPoint)
                    lowestPoints.add(point);
            }
        }

        int firstSolution = lowestPoints.stream().mapToInt(point -> map[point.y()][point.x()] + 1).sum();
        System.out.println(firstSolution);

        int secondSolution = lowestPoints.stream()
                .map(point -> calculateBasinSize(point, map))
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(1, (v1, v2) -> v1 * v2);
        System.out.println(secondSolution);
    }

    private static int getValue(int[][] map, Point currentPoint) {
        return map[currentPoint.y()][currentPoint.x()];
    }

    private static int calculateBasinSize(Point point, int[][] map) {
        Deque<Point> stack = new LinkedList<>();
        stack.push(point);
        Set<Point> basinPoints = new HashSet<>();
        while (!stack.isEmpty()) {
            Point currentPoint = stack.pop();
            basinPoints.add(currentPoint);
            getAdjacentPoints(currentPoint).forEach(x -> {
                if (belongsToBasin(x, map, basinPoints))
                    stack.push(x);
            });
        }
        return basinPoints.size();
    }

    private static Stream<Point> getAdjacentPoints(Point point) {
        return Stream.of(
                new Point(point.x(), point.y() - 1),
                new Point(point.x(), point.y() + 1),
                new Point(point.x() - 1, point.y()),
                new Point(point.x() + 1, point.y())
        );
    }

    private static boolean belongsToBasin(Point point,
                                          int[][] map,
                                          Set<Point> basinPoints) {
        return !basinPoints.contains(point) && withinMap(map, point) && getValue(map, point) < 9;
    }

    private static boolean withinMap(int[][] map, Point point) {
        int maxY = map.length;
        int maxX = map[0].length;
        return point.x() >= 0 && point.x() < maxX && point.y() >= 0 && point.y() < maxY;
    }

    private static int[][] getData() {
        InputStream inputStream = SmokeBasin.class.getResourceAsStream("input.txt");
        List<String> raw = new ArrayList<>();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            raw.add(scanner.nextLine());
        }
        int maxX = raw.get(0).length();
        int maxY = raw.size();
        int[][] map = new int[maxY][maxX];
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                map[y][x] = Integer.parseInt(String.valueOf(raw.get(y).charAt(x)));
            }
        }
        return map;
    }

    record Point(int x, int y) {
    }
}
