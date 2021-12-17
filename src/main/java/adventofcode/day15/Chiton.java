package adventofcode.day15;

import adventofcode.Util;

import java.util.Map;

public class Chiton {

    public static void main(String arg[]) {
        int[][] riskMatrix = Util.getMatrix(Chiton.class.getResourceAsStream("input.txt"));
        ShortestPath.PointsProvider provider = new ShortestPath.PointsProvider(riskMatrix, 5);
        Map<Point, Integer> distanceMap = ShortestPath.calculateDistanceMap(provider, new Point(0, 0));
        System.out.println(distanceMap.get(provider.getLastPoint()));
    }

}

