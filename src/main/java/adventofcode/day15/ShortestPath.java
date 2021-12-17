package adventofcode.day15;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShortestPath {

    public static Map<Point, Integer> calculateDistanceMap(PointsProvider provider, Point src) {
        Map<Point, Integer> distanceMap = new HashMap<>();
        Set<Point> settled = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        pq.add(new Node(src, 0));
        distanceMap.put(src, 0);

        while (settled.size() != provider.totalSize()) {
            if (pq.isEmpty())
                return distanceMap;

            // Removing the minimum distance node
            // from the priority queue
            Point minDistancePoint = pq.remove().point;

            // Adding the node whose distance is finalized
            if (settled.contains(minDistancePoint))
                // Continue keyword skips execution for
                // following check
                continue;

            // We don't have to call e_Neighbors(u)
            // if u is already present in the settled set.
            settled.add(minDistancePoint);
            processNeighbours(minDistancePoint, provider, distanceMap, settled, pq);
        }
        return distanceMap;
    }

    private static void processNeighbours(Point point, PointsProvider provider,
                                   Map<Point, Integer> distanceMap,
                                   Set<Point> settled,
                                   PriorityQueue<Node> pq) {
        for (Node neighbour : provider.adjacentNodes(point)) {
            // If current node hasn't already been processed
            if (!settled.contains(neighbour.point)) {
                int edgeDistance = neighbour.cost;
                int newDistance = distanceMap.get(point) + edgeDistance;

                // If new distance is cheaper in cost
                if (newDistance < distanceMap.getOrDefault(neighbour.point, Integer.MAX_VALUE))
                    distanceMap.put(neighbour.point, newDistance);

                // Add the current node to the queue
                pq.add(new Node(neighbour.point, distanceMap.get(neighbour.point)));
            }
        }
    }

    static class PointsProvider {
        private final int[][] matrix;
        private final int multiplier;
        private final int singleMaxX;
        private final int singleMaxY;
        private final int maxX;
        private final int maxY;

        public PointsProvider(int[][] matrix, int multiplier) {
            this.matrix = matrix;
            this.multiplier = multiplier;
            singleMaxX = matrix[0].length;
            singleMaxY = matrix.length;
            maxX = singleMaxX * multiplier;
            maxY = singleMaxY * multiplier;
        }

        List<Node> adjacentNodes(Point point) {
            return getAdjacentPoints(point)
                    .map(p -> new Node(p, getValue(p)))
                    .collect(Collectors.toList());
        }

        Point getLastPoint() {
            return new Point(maxX-1, maxY-1);
        }

        Integer getValue(Point point) {
            int baseValue = matrix[point.y() % singleMaxY][point.x() % singleMaxX];
            int addition = point.x()/singleMaxX + point.y()/singleMaxY;
            int newValue = baseValue + addition;
            return newValue > 9 ? newValue - 9 : newValue;
        }

        public int totalSize() {
            return matrix.length * multiplier * matrix[0].length * multiplier;
        }

        private Stream<Point> getAdjacentPoints(Point point) {
            return Stream.of(
                            new Point(point.x(), point.y() - 1),
                            new Point(point.x(), point.y() + 1),
                            new Point(point.x() - 1, point.y()),
                            new Point(point.x() + 1, point.y())
                    )
                    .filter(this::withinMap);
        }

        private boolean withinMap(Point point) {
            return point.x() >= 0 && point.x() < maxX && point.y() >= 0 && point.y() < maxY;
        }
    }

    static class Node implements Comparable<Node> {
        public Point point;
        public int cost;

        public Node(Point point, int cost) {
            this.point = point;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node node) {
            if (this.cost < node.cost)
                return -1;

            if (this.cost > node.cost)
                return 1;

            return 0;
        }
    }
}
