package bitboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static bitboard.BitBoardUtils.bitScanForwardDeBruijn64;
import static bitboard.BitBoardUtils.getAllSquaresToIndices;
import static bitboard.BitBoardState.fenStringToBitBoardState;
import static bitboard.Piece.WHITE_KING;
import static org.junit.jupiter.api.Assertions.*;

/*
    TODO: add more fenstrings to the tests, specifically to generateMoves.
     think of moves such as checking yourself when capturing en passant or stuff like that.

 */
class LegalMoveGeneratorTest {
    LegalMoveGenerator generator;
    PreCalculatedData data;
    BitBoard board = new BitBoard();

    @BeforeEach
    void setup() {
        data = new PreCalculatedData();
        generator = new LegalMoveGenerator(data);
    }


    @ParameterizedTest
    @CsvSource({
            "8/8/8/8/8/8/q7/2K5 w - - 0 1, 3"
    })
    void tryMoveSucceeds(String fenString, int destinationSquare) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        BitBoard bitBoard = new BitBoard(state, generator);
//        System.out.println(bitBoard.toBoardString());
//        Move illegaLMove = new Move.MoveBuilder(bitScanForwardDeBruijn64(state.getWhiteOccupancy()), destinationSquare)
//                .capture(0).pieceType(WHITE_KING).castling(0).setDouble(0).enPassant(0).promoted(0).encodeMove().build();
        System.out.println("hi");
        List<Move> generatedMoves = generator.generateMoves(state);
        System.out.println(generatedMoves);
        for (Move generatedMove : generatedMoves) {
            System.out.println(generatedMove.toString());
        }
//        generator.generateMoves(state).forEach(System.out::print);
//        assertTrue(generator.generateMoves(state).contains(illegaLMove));

    }

    @ParameterizedTest
    @CsvSource({
            "8/8/8/8/8/8/q7/2K5 w - - 0 1, 1"
    })
    void tryMoveFails(String fenString, int destinationSquare) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        Move illegaLMove = new Move.MoveBuilder(bitScanForwardDeBruijn64(state.getWhiteOccupancy()), destinationSquare)
                .capture(0).pieceType(WHITE_KING).castling(0).setDouble(0).enPassant(0).promoted(0).encodeMove().build();
        assertFalse(generator.generateMoves(state).contains(illegaLMove));
    }

    @ParameterizedTest
    @CsvSource({
            "8/8/8/7Q/8/8/8/8 w - - 0 1, G5"
    })
    void isNotInCheck() {

    }

    @ParameterizedTest
    @CsvSource({
            "8/8/8/7Q/8/8/8/8 w - - 0 1, G5"
    })
    void isSquareAttacked(String fenString, String square) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        boolean actualOutcome = generator.isSquareAttacked(getAllSquaresToIndices().get(square), state.getSideToMove() == Side.WHITE? 0: 1, state);
        assertTrue(actualOutcome);
    }

    @ParameterizedTest
    @CsvSource({
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBN1 w KQkq - 0 1, 20"
    })
    void generateMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        List<Move> outcome = generator.generateMoves(state);
        assertEquals(numberOfMoves,outcome.size());
    }

    @ParameterizedTest
    @CsvSource({
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBN1 w KQkq - 0 1"
    })
    void WhiteToPlayWhenBlackMoved(String fenString) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        BitBoard someBoard = new BitBoard(state, generator);
        System.out.println(someBoard.toBoardString());

        List<Move> outcome = generator.generateMoves(new BitBoardState(state));
        System.out.println(someBoard.toBoardString());
        BitBoardState newState = generator.moveToState(outcome.get(0), state);
        System.out.println(someBoard.toBoardString());
        someBoard.setCurrentState(newState);
        System.out.println(someBoard.toBoardString());
//        System.out.println(someBoard.toBoardString());
//        System.out.println(newState.getSideToMove());
        List<Move> outcomeBlack = generator.generateMoves(newState);


        assertEquals(newState.getSideToMove(), Side.BLACK);
        List<Move> outcomeTwo = generator.generateMoves(newState);
        BitBoardState secondNewState = generator.moveToState(outcomeTwo.get(0), newState);
        assertEquals(secondNewState.getSideToMove(), Side.WHITE);
        List<Move> outcomeThree = generator.generateMoves(secondNewState);
        BitBoardState thirdNewState = generator.moveToState(outcomeThree.get(0), secondNewState);
        assertEquals(thirdNewState.getSideToMove(), Side.BLACK);
    }

    @ParameterizedTest
    @CsvSource({
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBN1 b KQkq - 0 1"
    })
    void debugTesting(String fenString) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        BitBoard someBoard = new BitBoard(state, generator);
        List<Move> blackMoves = generator.generateMoves(state);
        BitBoardState newState = generator.moveToState(blackMoves.get(0), state);
        List<Move> whiteMoves = generator.generateMoves(newState);

        System.out.println(whiteMoves.size());
        List<Move> bishops = generator.generatePseudoLegalBishopMoves(newState);
        System.out.println(bishops.size());

    }



}