package bitboard.generators;

import bitboard.moves.Move;
import bitboard.constants.Side;
import bitboard.engine.BitBoard;
import bitboard.engine.BitBoardState;
import bitboard.utils.PreCalculatedData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static bitboard.utils.BitBoardUtils.bitScanForwardDeBruijn64;
import static bitboard.engine.BitBoardState.fenStringToBitBoardState;
import static bitboard.constants.Piece.WHITE_KING;
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
        System.out.println("hi");
        List<Move> generatedMoves = generator.generateMoves(state);
        System.out.println(generatedMoves);
        for (Move generatedMove : generatedMoves) {
            System.out.println(generatedMove.toString());
        }

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
        List<Move> outcomeBlack = generator.generateMoves(newState);


        assertEquals(newState.getSideToMove(), Side.BLACK);
        List<Move> outcomeTwo = generator.generateMoves(newState);
        BitBoardState secondNewState = generator.moveToState(outcomeTwo.get(0), newState);
        assertEquals(secondNewState.getSideToMove(), Side.WHITE);
        List<Move> outcomeThree = generator.generateMoves(secondNewState);
        BitBoardState thirdNewState = generator.moveToState(outcomeThree.get(0), secondNewState);
        assertEquals(thirdNewState.getSideToMove(), Side.BLACK);
    }
}