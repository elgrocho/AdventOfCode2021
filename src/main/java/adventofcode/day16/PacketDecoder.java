package adventofcode.day16;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class PacketDecoder {

    public static void main(String[] args) {
        InputStream inputStream = PacketDecoder.class.getResourceAsStream("input.txt");
        Scanner scanner = new Scanner(inputStream);
        String packets = scanner.nextLine();

        PacketDecoder packetDecoder = new PacketDecoder();
        Packet packet = packetDecoder.parsePackets(packets);
        System.out.println("versions total: " + packetDecoder.countVersions(packet));
        System.out.println("result: " + packetDecoder.evaluate(packet));
    }


    public long evaluate(Packet packet) {
        return switch (packet.type()) {
            case 0 -> packet.subpackets().stream().mapToLong(this::evaluate).sum();
            case 1 -> packet.subpackets().stream().mapToLong(this::evaluate).reduce(1, (a, b) -> a * b);
            case 2 -> packet.subpackets().stream().mapToLong(this::evaluate).min().getAsLong();
            case 3 -> packet.subpackets().stream().mapToLong(this::evaluate).max().getAsLong();
            case 4 -> packet.value();
            case 5 -> evaluate(packet.subpackets().get(0)) > evaluate(packet.subpackets().get(1)) ? 1 : 0;
            case 6 -> evaluate(packet.subpackets().get(0)) < evaluate(packet.subpackets().get(1)) ? 1 : 0;
            case 7 -> evaluate(packet.subpackets().get(0)) == evaluate(packet.subpackets().get(1)) ? 1 : 0;
            default -> throw new IllegalStateException("Unexpected value: " + packet.type());
        };
    }

    public long countVersions(Packet packet) {
        if (packet.subpackets() == null) {
            return packet.version();
        }

        long total = packet.version();
        for (Packet sub : packet.subpackets()) {
            total = total + countVersions(sub);
        }
        return total;
    }

    public Packet parsePackets(String packets) {
        return readPacket(new Parser(packets));
    }

    private Packet readPacket(Parser parser) {
        long version = parser.readBinaryLong(3);
        int packetType = parser.readBinaryInt(3);
        if (packetType == 4) {
            long value = parser.readLiteral();
            return new Packet(version, packetType, value, null);
        }

        int operatorType = parser.readFlag();
        if (operatorType == 0) {
            long lengthOfSubPackets = parser.readBinaryLong(15);
            long start = parser.pointer;
            boolean done = false;
            List<Packet> subpackets = new ArrayList<>();
            while (!done) {
                subpackets.add(readPacket(parser));
                if (lengthOfSubPackets == (parser.pointer - start)) {
                    done = true;
                }
            }
            return new Packet(version, packetType, null, subpackets);
        }

        long subPacketsNo = parser.readBinaryLong(11);
        List<Packet> subpackets = LongStream.range(0, subPacketsNo)
                .mapToObj(v -> readPacket(parser))
                .collect(Collectors.toList());
        return new Packet(version, packetType, null, subpackets);
    }

    record Packet(long version, int type, Long value, List<Packet> subpackets) {
    }
}
