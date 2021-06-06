package bitboard;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static bitboard.BitBoardState.fenStringToBitBoardState;
import static bitboard.BitBoardUtils.*;
import static bitboard.Piece.WHITE_KING;
import static bitboard.Piece.WHITE_ROOK;

public class BitBoard implements IBoard{

    private @Getter List<BitBoardState> history;
    private @Getter BitBoardState currentState;
    private @Getter MoveGenerator moveGenerator;

    public BitBoard() {

    }

    public BitBoard(MoveGenerator moveGenerator) {
       this.currentState = fenStringToBitBoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ");
       this.moveGenerator = moveGenerator;
    }

    public BitBoard(String fenString, MoveGenerator moveGenerator) {
        this.currentState = fenStringToBitBoardState(fenString);
        this.moveGenerator = moveGenerator;
    }

    public BitBoard(BitBoardState bitBoardState, MoveGenerator moveGenerator) {
        this.currentState = bitBoardState;
        this.moveGenerator = moveGenerator;
    }

    public String toBoardString() {
        List<Long> tempBitBoards = new ArrayList<>();
        List<String> allSquaresEncoded = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            allSquaresEncoded.add(".");
        }
        for(long l: this.currentState.getBitBoards()) {
            tempBitBoards.add(l);
        }

        for (int i = 0; i < tempBitBoards.size(); i++) {
            List<Integer> currentIndices = getSquareIndices(tempBitBoards.get(i));
            for (Integer currentIndex : currentIndices) {
                allSquaresEncoded.set(currentIndex, getUnicodePieces().get(i));
            }
        }

        StringBuilder boardString = new StringBuilder();
        var tempEncodings = Stream.iterate(1, n->n+1).limit(8).map(x -> allSquaresEncoded.subList((x-1)*8,x*8)).collect(Collectors.toList());
        Collections.reverse(tempEncodings);
        int sizeEncodings = tempEncodings.size();

        for (int i = 0; i < tempEncodings.size(); i++) {
            if (i != 0 && i<8) {
                boardString.append("\n");
            }
            for (String s: tempEncodings.get(i)) {
                boardString.append(s);
            }

        }
        return boardString.toString();
    }

    //<editor-fold desc="FEN string parser and getter">

    public String toFenString() {
        String allOccupancyString = toBitBoardRepresentation(currentState.getAllOccupancy());
        return "0";
    }
    //</editor-fold>

    @Override
    public BitBoardState makeMove() {

        return null;
    }

    @Override
    public BitBoardState unMakeMove() {
        return null;
    }

    @Override
    public List<Move> getMoves() {
        return null;
    }

    public static class BitBoardBuilder {

    }

    public static void main(String[] args) {
        String startPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBN1 w KQkq - 0 1 ";
        String testAttackSquares = "8/8/4r3/3B4/8/8/8/8 w - - 0-1";
        BitBoard another = new BitBoard(startPosition, new LegalMoveGenerator(new PreCalculatedData()));
        System.out.println(bitScanForwardDeBruijn64(another.getCurrentState().getBitBoards()[WHITE_KING]));
        System.out.println(bitScanForwardDeBruijn64(another.getCurrentState().getBitBoards()[WHITE_ROOK]));
        System.out.println(toBitBoardRepresentation(another.getCurrentState().getWhiteOccupancy()));
        System.out.println(another.toBoardString());
        System.out.println(another.moveGenerator.isSquareAttacked(0,0,another.getCurrentState()));
    }
}
