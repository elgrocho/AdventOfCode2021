package adventofcode.day13;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class TransparentOrigami {
    public static void main(String[] args) {
        InputStream inputStream = TransparentOrigami.class.getResourceAsStream("input.txt");
        Scanner scanner = new Scanner(inputStream);
        Set<Point> dots = new HashSet<>();
        List<Point> folds = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isBlank())
                continue;
            if (line.startsWith("fold")) {
                String[] values = line.split(" ")[2].split("=");
                int value = Integer.parseInt(values[1]);
                String axis = values[0];
                int x = axis.equals("x") ? value : 0;
                int y = axis.equals("y") ? value : 0;
                folds.add(new Point(x, y));
            } else {
                String[] values = line.split(",");
                dots.add(new Point(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
            }
        }

        //part 1
        Transformer firstFoldTransformer = new Transformer(folds.subList(0, 1));
        Set<Point> firstFoldDots = dots.stream()
                .map(firstFoldTransformer::transform)
                .collect(Collectors.toSet());
        System.out.println(firstFoldDots.size());

        //part 2
        Transformer fullTransformer = new Transformer(folds);
        Set<Point> allFoldsDots = dots.stream()
                .map(fullTransformer::transform)
                .collect(Collectors.toSet());
        print(allFoldsDots);
    }

    private static void print(Set<Point> allFoldsDots) {
        int maxX = allFoldsDots.stream().mapToInt(Point::x).max().orElse(0) + 1;
        int maxY = allFoldsDots.stream().mapToInt(Point::y).max().orElse(0) + 1;
        char[][] display = new char[maxY][maxX];
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                display[y][x] = ' ';
            }
        }
        for (Point dot : allFoldsDots) {
            display[dot.y][dot.x] = 'X';
        }

        for (int y = 0; y < maxY; y++) {
            System.out.println(display[y]);
        }
    }

    record Point(int x, int y) {
    }

    static class Transformer {
        private final List<Point> folds;

        public Transformer(List<Point> folds) {
            this.folds = folds;
        }

        Point transform(Point point) {
            Point newPoint = point;
            for (Point fold : folds) {
                int x = fold.x() > 0 && newPoint.x() > fold.x()
                        ? fold.x() - (newPoint.x() - fold.x())
                        : newPoint.x();
                int y = fold.y() > 0 && newPoint.y() > fold.y()
                        ? fold.y() - (newPoint.y() - fold.y())
                        : newPoint.y();
                newPoint = new Point(x, y);
            }
            return newPoint;
        }
    }
}
