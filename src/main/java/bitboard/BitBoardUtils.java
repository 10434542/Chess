package bitboard;

import lombok.Getter;

import java.util.*;
import java.util.stream.IntStream;

public class BitBoardUtils {

    private BitBoardUtils() {

    }

    //<editor-fold desc="Bitboard utilities">
    private static final long deBruijn = 0x03f79d71b4cb0a89L;

    private static final @Getter int[] castlingSquares = {
            13, 15, 15, 15, 12, 15, 15, 14,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15,
            7, 15, 15, 15,  3, 15, 15, 11
    };

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

    private static final @Getter List<Character> allPieceEncodings = Arrays.asList('P','N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k');
    private static final @Getter List<String> unicodePieces = Arrays.asList("♙", "♘", "♗", "♖", "♕", "♔", "♟", "♞", "♝", "♜", "♛", "♚"); // first white, then black
    private static final @Getter Map<Character, Integer> charsToIndices = IntStream.range(0, allPieceEncodings.size())
            .collect(HashMap::new, (m, i) -> m
                    .put(allPieceEncodings
                            .get(i), i), Map::putAll );

    private static final List<Integer> allPromotedPieces = Arrays.asList(4, 3, 2, 1, 10, 9, 8, 7);
    private static final @Getter Map<Integer, Character> promotedPieces = IntStream.range(0, allPromotedPieces.size())
            .collect(HashMap::new, (m, i) -> m
                    .put(allPromotedPieces
                            .get(i), Character.toLowerCase(allPieceEncodings.get(i))), Map::putAll);

    private static final @Getter List<String> allSquareStrings = List.of(
            "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1",
            "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2",
            "A3", "B3","C3", "D3", "E3", "F3", "G3", "H3",
            "A4", "B4","C4", "D4", "E4", "F4", "G4", "H4",
            "A5", "B5","C5", "D5", "E5", "F5", "G5", "H5",
            "A6", "B6","C6", "D6", "E6", "F6", "G6", "H6",
            "A7", "B7","C7", "D7", "E7", "F7", "G7", "H7",
            "A8", "B8","C8", "D8", "E8", "F8", "G8", "H8");

    private static final @Getter
    Map<String, Integer> allSquaresToIndices = IntStream.range(0, allSquareStrings.size())
            .collect(HashMap::new, (m, i) -> m
                    .put(allSquareStrings
                            .get(i), i), Map::putAll);

    private static final @Getter CastlingRight wk = CastlingRight.WHITE_KING_SIDE;
    private static final @Getter CastlingRight bk = CastlingRight.BLACK_KING_SIDE;
    private static final @Getter CastlingRight wq = CastlingRight.WHITE_QUEEN_SIDE;
    private static final @Getter CastlingRight bq = CastlingRight.BLACK_QUEEN_SIDE;

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
        temp &= ~(1L << square); // bug fixed added parenthesis
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
