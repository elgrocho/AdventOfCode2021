package adventofcode.day17;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TrickShot {
    public static void main(String[] args) {
        InputStream inputStream = TrickShot.class.getResourceAsStream("input.txt");
        Scanner scanner = new Scanner(inputStream);
        String[] parts = scanner.nextLine().split(" ");
        TargetArea targetArea = new TargetArea(
                Integer.parseInt(parts[2].split("=")[1].replace(",", "").split("\\.\\.")[0]),
                Integer.parseInt(parts[2].split("=")[1].replace(",", "").split("\\.\\.")[1]),
                Integer.parseInt(parts[3].split("=")[1].replace(",", "").split("\\.\\.")[0]),
                Integer.parseInt(parts[3].split("=")[1].replace(",", "").split("\\.\\.")[1]));


        int maxHeight = 0;
        Set<Point> velocities = new HashSet<>();
        for(int x=1; x <= targetArea.maxX(); x++) {
            for(int y=-1000; y < 1000; y++) {
                Probe probe = new Probe(x, y);
                boolean tooLate = false;
                while (!tooLate) {
                    Point point = probe.nextStep();
                    if(inTargetArea(point, targetArea)) {
                        velocities.add(new Point(x,y));
                        if(probe.getMaxHeight() > maxHeight) {
                            maxHeight = probe.getMaxHeight();
                        }
                    }
                    tooLate = tooLate(point, targetArea);
                }
            }
        }

        System.out.println("max: " + maxHeight);
        System.out.println("velocities no: " + velocities.size());
    }

    static boolean inTargetArea(Point point, TargetArea targetArea) {
        if(point.x() > targetArea.maxX() || point.x() < targetArea.minX()) {
            return false;
        }
        if(point.y() > targetArea.maxY() || point.y() < targetArea.minY()) {
            return false;
        }
        return true;
    }

    static boolean tooLate(Point point, TargetArea targetArea) {
        if(point.x() > targetArea.maxX()) {
            return true;
        }
        if(point.y() < targetArea.minY()) {
            return true;
        }
        return false;
    }

    record TargetArea(int minX, int maxX, int minY, int maxY){}
    record Point(int x, int y){}

    static class Probe {
        private int velocityX;
        private int velocityY;
        private int maxHeight = 0;
        private Point currentLocation = new Point(0,0);

        public Probe(int velocityX, int velocityY) {
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        int getMaxHeight() {
            return maxHeight;
        }

        Point nextStep() {
            Point nextLocation = new Point(currentLocation.x() + velocityX, currentLocation.y() + velocityY);
            if(nextLocation.y() > maxHeight) {
                maxHeight = nextLocation.y();
            }

            if(velocityX > 0)
                velocityX--;

            if(velocityX < 0)
                velocityX++;

            velocityY--;
            currentLocation = nextLocation;
            return nextLocation;
        }
    }


}
