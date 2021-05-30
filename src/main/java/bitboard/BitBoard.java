package bitboard;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static bitboard.BitBoardState.fenStringToBitBoardState;
import static bitboard.BitBoardUtils.*;

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


    public BitBoardState getBitBoardState() {
        return null;
    }

    public static void main(String[] args) {
        String startPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ";
        String testAttackSquares = "8/8/4r3/3B4/8/8/8/8 w - - 0-1";
        BitBoard another = new BitBoard(startPosition, new LegalMoveGenerator(new PreCalculatedData()));
        System.out.println(another.toBoardString());

        BitBoard testBoard = new BitBoard(new LegalMoveGenerator(new PreCalculatedData()));

        long start = System.nanoTime();
        List<Move> generatedMoves = testBoard.getMoveGenerator().generateMoves(testBoard.getCurrentState());

        long end = System.nanoTime();
        long time = (end-start)/1000000;

    }
}
