package adventofcode.day5;

import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Lines {
    public static void main(String[] args) {
        InputStream inputStream = Lines.class.getResourceAsStream("lines.txt");
        List<Line> lines = new ArrayList<>();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String[] valuesAsStrings = scanner.nextLine().replace(" -> ", ",").split(",");
            List<Integer> values = Arrays.stream(valuesAsStrings)
                    .mapToInt(Integer::parseInt)
                    .boxed().toList();
            lines.add(new Line(
                    new Point(values.get(0), values.get(1)),
                    new Point(values.get(2), values.get(3)))
            );
        }

        Map<Point, List<Point>> pointsInHorizontalVerticalLines = lines.stream()
                .filter(line -> line.isHorizontal() || line.isVertical())
                .flatMap(Lines::getLinePoints)
                .collect(groupingBy(point -> point));

        long crossingPointsHorizontalVertical = pointsInHorizontalVerticalLines.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .count();
        System.out.println(crossingPointsHorizontalVertical);

        Map<Point, List<Point>> pointsInAllLines = lines.stream()
                .flatMap(Lines::getLinePoints)
                .collect(groupingBy(point -> point));

        long crossingPointsAll = pointsInAllLines.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .count();
        System.out.println(crossingPointsAll);

    }

    private static Stream<Point> getLinePoints(Line line) {
        if (line.isHorizontal()) {
            int start = Math.min(line.start.x, line.end.x);
            int end = Math.max(line.start.x, line.end.x);
            return IntStream.rangeClosed(start, end)
                    .mapToObj(index -> new Point(index, line.start.y));
        }

        if (line.isVertical()) {
            int start = Math.min(line.start.y, line.end.y);
            int end = Math.max(line.start.y, line.end.y);
            return IntStream.rangeClosed(start, end)
                    .mapToObj(index -> new Point(line.start.x, index));
        }

        Point startPoint = line.start.x < line.end.x ? line.start : line.end;
        Point endPoint = line.start.x > line.end.x ? line.start : line.end;
        int directionY = startPoint.y < endPoint.y ? 1 : -1;
        return IntStream.rangeClosed(0, endPoint.x - startPoint.x)
                .mapToObj(index -> new Point(startPoint.x + index, startPoint.y + index * directionY));
    }

    record Point(int x, int y) {
    }

    record Line(Point start, Point end) {
        public boolean isHorizontal() {
            return start.y == end.y;
        }

        public boolean isVertical() {
            return start.x == end.x;
        }
    }
}
