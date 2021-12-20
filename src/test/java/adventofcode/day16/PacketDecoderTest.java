package adventofcode.day16;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PacketDecoderTest {

    private final PacketDecoder packetDecoder = new PacketDecoder();

    @Test
    void testLiteral() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("D2FE28");
        assertEquals(packet.version(), 6);
        assertEquals(packet.type(), 4);
        assertEquals(packet.value(), 2021);
        assertEquals(packet.subpackets(), null);
    }

    @Test
    void testOperation1() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("38006F45291200");
        assertEquals(packet.version(), 1);
        assertEquals(packet.type(), 6);
        assertEquals(packet.value(), null);
        assertEquals(packet.subpackets().size(), 2);

        assertEquals(packet.subpackets().get(0).version(), 6);
        assertEquals(packet.subpackets().get(0).type(), 4);
        assertEquals(packet.subpackets().get(0).value(), 10);
        assertEquals(packet.subpackets().get(0).subpackets(), null);

        assertEquals(packet.subpackets().get(1).version(), 2);
        assertEquals(packet.subpackets().get(1).type(), 4);
        assertEquals(packet.subpackets().get(1).value(), 20);
        assertEquals(packet.subpackets().get(1).subpackets(), null);
    }

    @Test
    void testOperation2() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("EE00D40C823060");
        assertEquals(packet.version(), 7);
        assertEquals(packet.type(), 3);
        assertEquals(packet.value(), null);
        assertEquals(packet.subpackets().size(), 3);

        assertEquals(packet.subpackets().get(0).version(), 2);
        assertEquals(packet.subpackets().get(0).type(), 4);
        assertEquals(packet.subpackets().get(0).value(), 1);
        assertEquals(packet.subpackets().get(0).subpackets(), null);

        assertEquals(packet.subpackets().get(1).version(), 4);
        assertEquals(packet.subpackets().get(1).type(), 4);
        assertEquals(packet.subpackets().get(1).value(), 2);
        assertEquals(packet.subpackets().get(1).subpackets(), null);

        assertEquals(packet.subpackets().get(2).version(), 1);
        assertEquals(packet.subpackets().get(2).type(), 4);
        assertEquals(packet.subpackets().get(2).value(), 3);
        assertEquals(packet.subpackets().get(2).subpackets(), null);
    }

    @Test
    void testSum() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("C200B40A82");
        assertEquals(packetDecoder.evaluate(packet), 3);
    }

    @Test
    void testProduct() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("04005AC33890");
        assertEquals(packetDecoder.evaluate(packet), 54);
    }

    @Test
    void testMin() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("880086C3E88112");
        assertEquals(packetDecoder.evaluate(packet), 7);
    }

    @Test
    void testMax() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("CE00C43D881120");
        assertEquals(packetDecoder.evaluate(packet), 9);
    }

    @Test
    void testGreaterThan() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("D8005AC2A8F0");
        assertEquals(packetDecoder.evaluate(packet), 1);
    }

    @Test
    void testLessThan() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("F600BC2D8F");
        assertEquals(packetDecoder.evaluate(packet), 0);
    }

    @Test
    void testEqual() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("9C005AC2F8F0");
        assertEquals(packetDecoder.evaluate(packet), 0);
    }

    @Test
    void testComplex() {
        PacketDecoder.Packet packet = packetDecoder.parsePackets("9C0141080250320F1802104A08");
        assertEquals(packetDecoder.evaluate(packet), 1);
    }
}