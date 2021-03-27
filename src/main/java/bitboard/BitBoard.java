package bitboard;

import java.util.*;
import java.util.stream.IntStream;

import static bitboard.BitBoardUtils.*;

public class BitBoard {

    //<editor-fold desc="all bitboards">
    private final long[] bitBoards = new long[12];
    private long whiteOccupancy;
    private long blackOccupancy;
    private long allOccupancy; // read allPieces
    //</editor-fold>

    //<editor-fold desc="pieces">
    int whitePawn = 0;
    int whiteKnight = 1;
    int whiteBishop = 2;
    int whiteRook = 3;
    int whiteQueen = 4;
    int whiteKing = 5;
    int blackPawn = 6;
    int blackKnight = 7;
    int blackBishop = 8;
    int blackRook = 9;
    int blackQueen = 10;
    int blackKing = 11;
    List<Character> pieceEncoding = Arrays.asList('P','N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k');
    List<String> unicodePieces = Arrays.asList("♙", "♘", "♗", "♖", "♕", "♔", "♟︎", "♞", "♝", "♜", "♛", "♚");
    Map<Character, Integer> charToIndex = IntStream.range(0, pieceEncoding.size()).collect(HashMap::new, (m, i) -> m.put(pieceEncoding.get(i), i), Map::putAll );
    //</editor-fold>

    //<editor-fold desc="Square mappings">
    List<String> squareStrings = List.of(
            "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1",
            "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2",
            "A3", "B3","C3", "D3", "E3", "F3", "G3", "H3",
            "A4", "B4","C4", "D4", "E4", "F4", "G4", "H4",
            "A5", "B5","C5", "D5", "E5", "F5", "G5", "H5",
            "A6", "B6","C6", "D6", "E6", "F6", "G6", "H6",
            "A7", "B7","C7", "D7", "E7", "F7", "G7", "H7",
            "A8", "B8","C8", "D8", "E8", "F8", "G8", "H8");
    Map<String, Integer> squareToIndex = IntStream.range(0, squareStrings.size()).collect(HashMap::new, (m, i) -> m.put(squareStrings.get(i), i), Map::putAll);
    //</editor-fold>

    //<editor-fold desc="castling rights">
    CastlingRight wk = CastlingRight.WHITE_KING_SIDE;
    CastlingRight bk = CastlingRight.BLACK_KING_SIDE;
    CastlingRight wq = CastlingRight.WHITE_QUEEN_SIDE;
    CastlingRight bq = CastlingRight.BLACK_QUEEN_SIDE;
    int allCastlingRights = 0;
    //</editor-fold>

    //<editor-fold desc=" en passant and side to move">
    int enPassantSquare = -1;
    SideToMove sideToMove;
    //</editor-fold>

    //<editor-fold desc="Precalculated data">
    PreCalculatedData data = new PreCalculatedData();
    //</editor-fold>

    public BitBoard() {

        // initialize standard board
       this.bitBoards[whitePawn] = setBits(0L, new int[] {8, 9, 10, 11, 12, 13, 14, 15});
       this.bitBoards[whiteRook] = setBits(0L, new int[] {0,7});
       this.bitBoards[whiteKnight] = setBits(0L, new int[] {1, 6});
       this.bitBoards[whiteBishop] = setBits(0L, new int[] {2, 5});
       this.bitBoards[whiteKing] = setBit(0L, 4);
       this.bitBoards[whiteQueen] = setBit(0L, 3);
       this.bitBoards[blackPawn] = setBits(0L, new int[] {48, 49, 50, 51, 52, 53, 54, 55});
       this.bitBoards[blackRook] = setBits(0L, new int[] {56,63});
       this.bitBoards[blackKnight] = setBits(0L, new int[] {57, 62});
       this.bitBoards[blackBishop] = setBits(0L, new int[] {58, 61});
       this.bitBoards[blackKing] = setBit(0L, 60);
       this.bitBoards[blackQueen] = setBit(0L, 59);
       this.sideToMove = SideToMove.WHITE;
       this.allCastlingRights = wk.getType() ^ bk.getType() ^ wq.getType() ^ bq.getType();

    }

    public BitBoard(String fenString) {
        parseFenString(fenString);
    }

    //<editor-fold desc="getters and setters">
    public long[] getBitBoards() {
        return bitBoards;
    }

    public long getWhiteOccupancy() {
        return whiteOccupancy;
    }

    public void setWhiteOccupancy(long whiteOccupancy) {
        this.whiteOccupancy = whiteOccupancy;
    }

    public long getBlackOccupancy() {
        return blackOccupancy;
    }

    public void setBlackOccupancy(long blackOccupancy) {
        this.blackOccupancy = blackOccupancy;
    }

    public long getAllOccupancy() {
        return allOccupancy;
    }

    public void setAllOccupancy(long allOccupancy) {
        this.allOccupancy = allOccupancy;
    }

    public int getWhitePawn() {
        return whitePawn;
    }

    public int getWhiteKnight() {
        return whiteKnight;
    }

    public int getWhiteBishop() {
        return whiteBishop;
    }

    public int getWhiteRook() {
        return whiteRook;
    }

    public int getWhiteQueen() {
        return whiteQueen;
    }

    public int getWhiteKing() {
        return whiteKing;
    }

    public int getBlackPawn() {
        return blackPawn;
    }

    public int getBlackKnight() {
        return blackKnight;
    }

    public int getBlackBishop() {
        return blackBishop;
    }

    public int getBlackRook() {
        return blackRook;
    }

    public int getBlackQueen() {
        return blackQueen;
    }

    public int getBlackKing() {
        return blackKing;
    }
    //</editor-fold>

    public String toBoardString() {
        List<Long> tempBitBoards = new ArrayList<>();
        List<String> allSquaresEncoded = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            allSquaresEncoded.add(".");
        }
        for(long l: bitBoards) {
            tempBitBoards.add(l);
        }
        for (int i = 0; i < tempBitBoards.size(); i++) {
            List<Integer> currentIndices = getSquareIndices(tempBitBoards.get(i));
            for (Integer currentIndex : currentIndices) {
                allSquaresEncoded.set(currentIndex, unicodePieces.get(i));
            }
        }

        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < allSquaresEncoded.size(); i++) {
            if (i != 0 && i%8==0) {
                boardString.append("\n");
            }
            boardString.append(allSquaresEncoded.get(i));

        }
        // reverse string somewhere here
        return boardString.toString();
    }

    //<editor-fold desc="FEN string parser and getter">
    public void parseFenString(String fen) {
        List<String> splitString = Arrays.asList(fen.split("/"));
        List<String> lastRankAndStateInfo = Arrays.asList(splitString.get(7).split(" "));
        List<String> ranks = new ArrayList<>(splitString.subList(0, 7));
        ranks.add(lastRankAndStateInfo.get(0));
        String side = lastRankAndStateInfo.get(1);
        String castlingRight = lastRankAndStateInfo.get(2);
        String enPassantSquareString = lastRankAndStateInfo.get(3);
//        String blackTurns = lastRankAndStateInfo.get(4); may need this later on
//        String whiteTurns = lastRankAndStateInfo.get(5);
        this.sideToMove = side.equals(new String(new char[]{'w'})) ? SideToMove.WHITE: SideToMove.BLACK;
        this.enPassantSquare = enPassantSquareString.equals("-")? -1: squareToIndex.get(enPassantSquareString);
        Collections.reverse(ranks); // else board is flipped

        int counter = 0;
        for (String rank : ranks) {
            for (char c: rank.trim().toCharArray()) {
                if (Character.isLetter(c)) {
                    this.bitBoards[charToIndex.get(c)] = setBit(this.bitBoards[charToIndex.get(c)], counter);
                    if (charToIndex.get(c) < 6) {
                        whiteOccupancy |= this.bitBoards[charToIndex.get(c)];
                    }
                    else {
                        blackOccupancy |= this.bitBoards[charToIndex.get(c)];
                    }
                    counter++;
                }
                else {
                    counter += Character.getNumericValue(c);
                }
            }
        }
        allOccupancy = whiteOccupancy | blackOccupancy;

        for (char c: castlingRight.trim().toCharArray()) {
            if (c == 'K') {
                this.allCastlingRights |= wk.getType();
            } else if (c == 'Q') {
                this.allCastlingRights |= wq.getType();
            } else if (c == 'k') {
                this.allCastlingRights |= bk.getType();
            } else if (c == 'q') {
                this.allCastlingRights |= bq.getType();
            }
        }
    }

    public String toFenString() {
        String allOccupancyString = toBitBoardRepresentation(allOccupancy);
        return "0";
    }
    //</editor-fold>

    public static void main(String[] args) {
        String startPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ";
        BitBoard another = new BitBoard(startPosition);
    }
}
