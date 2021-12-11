package adventofcode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {
    public static int[][] getMatrix(InputStream inputStream) {
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
}
