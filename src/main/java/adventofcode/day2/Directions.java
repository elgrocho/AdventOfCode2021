package adventofcode.day2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Directions {

    public static void main(String[] args) {
        InputStream inputStream = Directions.class.getResourceAsStream("directions.txt");
        Scanner scanner = new Scanner(inputStream);
        List<Command> commands = new ArrayList<>();
        while (scanner.hasNext()) {
            commands.add(new Command(scanner.next(), scanner.nextInt()));
        }

        Position finalPosition = commands.stream().reduce(new Position(0, 0, 0),
                (position, command) -> switch(command.command()){
                    case "forward" -> position.forward(command.value);
                    case "down" -> position.down(command.value);
                    case "up" -> position.up(command.value);
                    default -> throw new IllegalStateException("Unexpected value: " + command.command());
                },
                Position::add);

        System.out.println(finalPosition.x * finalPosition.y);
    }

    record Command(String command, int value){}
    record Position(int x, int y, int aim) {
        Position add(Position position) {
            return new Position(x + position.x, y + position.y, aim + position.aim);
        }
        Position forward(int value) {
            return new Position(x + value, y + aim * value, aim);
        }

        Position down(int value) {
            return new Position(x, y, aim + value);
        }

        Position up(int value) {
            return new Position(x, y, aim - value);
        }
    }
}
