package adventofcode.day16;

public class Parser {
    private final String data;
    public int pointer = 0;

    public Parser(String data) {
        this.data = hexToBin(data);
    }

    private String hexToBin(String hex) {
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }

    long readBinaryLong(int length) {
        long value = binaryToDecimal(data.substring(pointer, pointer + length));
        pointer = pointer + length;
        return value;
    }

    int readBinaryInt(int length) {
        int value = Integer.parseInt(data.substring(pointer, pointer + length), 2);
        pointer = pointer + length;
        return value;
    }

    private long binaryToDecimal(String value) {
        return Long.parseLong(value, 2);
    }

    String readRaw(int length) {
        String value = data.substring(pointer, pointer + length);
        pointer = pointer + length;
        return value;
    }

    int readFlag() {
        int value = Integer.parseInt(data.substring(pointer, pointer + 1));
        pointer = pointer + 1;
        return value;
    }

    public long readLiteral() {
        boolean literalRead = false;
        String value = "";
        while (!literalRead) {
            int literalFlag = readFlag();
            value = value + readRaw(4);
            if (literalFlag == 0) {
                literalRead = true;
            }
        }
        return binaryToDecimal(value);
    }
}