package bitboard;

import chessengine.IllegalMoveException;
import lombok.Getter;
import lombok.Setter;
import org.junit.platform.commons.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bitboard.BitBoardState.fenStringToBitBoardState;
import static bitboard.BitBoardUtils.*;
import static bitboard.LegalMoveGenerator.*;
import static bitboard.Piece.BLACK_BISHOP;
import static bitboard.Piece.WHITE_PAWN;

public class BitBoard implements IBoard{

    private @Getter List<BitBoardState> stateHistory;
    private @Getter @Setter
    BitBoardState currentState;
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
            allSquaresEncoded.add(" .");
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
    public void makeMove(Move move) throws IllegalMoveException {
        if (!moveGenerator.generateMoves(currentState).contains(move)) {
            throw new IllegalMoveException("Move not legal");
        }
        else {
            stateHistory.add(currentState);
            this.currentState = moveGenerator.moveToState(move, currentState);
            stateHistory.add(currentState);
        }
    }

    @Override
    public void unMakeMove() {
        int size = stateHistory.size();
        currentState = stateHistory.get(size-1);
        stateHistory.remove(size-1);
    }

    @Override
    public List<Move> getMoves() {
        return moveGenerator.generateMoves(currentState);
    }

    public static class BitBoardBuilder {

    }

    public static void main(String[] args) {
        String startPosition = "rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1";
        String testAttackSquares = "k7/8/8/QK/8/8/8/8 b - - 0 1";
        PreCalculatedData data = new PreCalculatedData();
        String castlingTest = "r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1 ";
        BitBoard another = new BitBoard(startPosition, new LegalMoveGenerator(new PreCalculatedData()));
        LegalMoveGenerator generator = new LegalMoveGenerator(new PreCalculatedData());
        BitBoard castling = new BitBoard(castlingTest, generator);
        BitBoard isCheckedTest = new BitBoard(testAttackSquares,generator);
//        System.out.println(Arrays.toString(getCastlingSquares()));
//        generator.generatePseudoLegalCastlingMoves(castling.currentState).forEach(System.out::println);
//        List<Move> moves = generator.generatePseudoLegalCastlingMoves(castling.currentState);
//        List<Move> castlingStates = moves.stream()
//                .filter(move->generator.isNotInCheck(generator.moveToState(move, new BitBoardState.BitBoardStateBuilder().of(castling.currentState).build())))
//                .collect(Collectors.toList());
//        for (Move move : moves) {
//            System.out.println(toBinaryPaddedString(move.getCastling()));
//        }
//        castlingStates.forEach(System.out::println);

//        System.out.println(Arrays.toString(getCastlingSquares()));
        List<Move> initialMoves = generator.generateMoves(another.getCurrentState());
        List<BitBoardState> firstStates = initialMoves
                .stream()
                .map(x -> generator.moveToState(x, another.currentState))
                .collect(Collectors.toList());
//        long startTime = System.currentTimeMillis();
//        generator.perftDriver(2, another.getCurrentState());
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("time\t\t\t" +(endTime-startTime)/1000.00);
//        System.out.println("Nodes:\t\t\t"+perftStart);
//        System.out.println("Captures:\t\t"+captures);
//        System.out.println("Castlings:\t\t"+castle);
//        System.out.println("Checks:\t\t\t"+checks);
//        System.out.println("NotChecks:\t\t"+notChecks);
//        System.out.println("En Passants:\t"+enpas);
        System.out.println("Square\t Number of Moves");
        for (int i = 0; i < firstStates.size(); i++) {
            generator.perftDriver(2, firstStates.get(i));
            System.out.println(initialMoves.get(i)+"\t"+perftStart);
            perftStart = 0;
        }
    }
}
