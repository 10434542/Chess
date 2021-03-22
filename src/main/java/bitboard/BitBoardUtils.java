package bitboard;

import java.util.ArrayList;
import java.util.List;

public class BitBoardUtils {

    private BitBoardUtils() {

    }
    //<editor-fold desc="Bitboard utilities">
    private static final long deBruijn = 0x03f79d71b4cb0a89L;

    private static final int[] magicTable = {
            0, 1,48, 2,57,49,28, 3,
            61,58,50,42,38,29,17, 4,
            62,55,59,36,53,51,43,22,
            45,39,33,30,24,18,12, 5,
            63,47,56,27,60,41,37,16,
            54,35,52,21,44,32,23,11,
            46,26,40,15,34,20,31,10,
            25,14,19, 9,13, 8, 7, 6,
    };

    public static int bitScanForwardDeBruijn64 (long b) {
        int idx = (int)(((b & -b) * deBruijn) >>> 58);
        return magicTable[idx];
    }

    public static List<Integer> getSquareIndices(long b) {
        List<Integer> squareIndices = new ArrayList<>();
        long temp = b;
        while(temp != 0L) {
            squareIndices.add(bitScanForwardDeBruijn64(temp));
            temp &= temp -1;
        }
        return squareIndices;
    }

    public static String toBinaryPaddedString(long number) {
        String binary = Long.toBinaryString(number);
        if (binary.length() < 64) {
            int paddingSize = 64 - binary.length();
            binary = "0".repeat(paddingSize) + binary;
        }
        return binary;
    }

    static long setBit(long bitBoard, int square) {
        return bitBoard | (1L << square) ;
    }

    static long setBits(long bitBoard, int[] squares) {
        for (int square : squares) {
            bitBoard |= (1L << square);
        }
        return bitBoard;
    }
    static long getBit(long bitBoard, int square) {
        return bitBoard & (1L << square);
    }

    static long removeBit(long bitBoard, int square) {
        long temp = bitBoard;
        if (temp << square == 0) {
            return bitBoard;
        }
        else {
            temp ^= (1L << square);
            return temp;
        }
    }

    static long popBit(long bitBoard, int square) {
        long temp = bitBoard;
        temp &= ~(1<< square);
        return temp;
    }

    static String bitBoardToBinary(String bitboardRepresentation) {
        String toReverse = bitboardRepresentation.replace("\n", "");
        StringBuilder binaryRepresentation = new StringBuilder();
        List<String> splitString = List.of(
                toReverse.substring(0,8),
                toReverse.substring(8,16),
                toReverse.substring(16,24),
                toReverse.substring(24,32),
                toReverse.substring(32,40),
                toReverse.substring(40,48),
                toReverse.substring(48,56),
                toReverse.substring(56,64));
        for (String s : splitString) {
            for (char c: new StringBuffer(s).reverse().toString().toCharArray()) {
                binaryRepresentation.append(c);
            }
        }
        return binaryRepresentation.toString();
    }

    static boolean containsSquare(long bitboard, int square) {
        return ((bitboard >> square)&1) != 0;
    }

    static String toBitBoardRepresentation(String binaryRepresentation) {
        List<String> splitString = List.of(
                binaryRepresentation.substring(0,8),
                binaryRepresentation.substring(8,16),
                binaryRepresentation.substring(16,24),
                binaryRepresentation.substring(24,32),
                binaryRepresentation.substring(32,40),
                binaryRepresentation.substring(40,48),
                binaryRepresentation.substring(48,56),
                binaryRepresentation.substring(56,64));
        StringBuilder bitBoardRepresentation = new StringBuilder();

        for (String s : splitString) {
            for (char c: new StringBuffer(s).reverse().toString().toCharArray()) {
                bitBoardRepresentation.append(c);
            }
            bitBoardRepresentation.append("\n");

        }
        return  bitBoardRepresentation.toString();
    }

    static String toBitBoardRepresentation(long binary) {
        String binaryRepresentation = toBinaryPaddedString(binary);
        List<String> splitString = List.of(
                binaryRepresentation.substring(0,8),
                binaryRepresentation.substring(8,16),
                binaryRepresentation.substring(16,24),
                binaryRepresentation.substring(24,32),
                binaryRepresentation.substring(32,40),
                binaryRepresentation.substring(40,48),
                binaryRepresentation.substring(48,56),
                binaryRepresentation.substring(56,64));
        StringBuilder bitBoardRepresentation = new StringBuilder();

        for (String s : splitString) {
            for (char c: new StringBuffer(s).reverse().toString().toCharArray()) {
                bitBoardRepresentation.append(c);
            }
            bitBoardRepresentation.append("\n");

        }
        return  bitBoardRepresentation.toString();
    }

    //</editor-fold>
}
