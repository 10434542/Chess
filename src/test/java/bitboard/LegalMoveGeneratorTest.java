package bitboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bitboard.BitBoardState.fenStringToBitBoardState;
import static org.junit.jupiter.api.Assertions.assertEquals;


class LegalMoveGeneratorTest {
    LegalMoveGenerator generator;
    PreCalculatedData data;
    BitBoard board = new BitBoard();

    @BeforeEach
    void setup() {
        data = new PreCalculatedData();
        generator = new LegalMoveGenerator(data);
    }


    @Test
    void tryMoveSucceeds() {

    }

    @Test
    void tryMoveFails() {

    }

    @Test
    void isNotInCheck() {
    }

    @Test
    void isSquareAttacked() {
    }

    @Test
    void generateMoves() {
    }

    @Test
    void generatePseudoLegalMoves() {
    }

    @Test
    void generatePseudoLegalPawnMoves() {
    }

    @Test
    void generatePseudoLegalCastlingMoves() {
    }

    @Test
    void generatePseudoLegalKnightMoves() {
    }

    @Test
    void generatePseudoLegalKingMoves() {
    }

    @Test
    void generatePseudoLegalBishopMoves() {
    }

    @Test
    void generatePseudoLegalRookMoves() {
    }

    @Test
    void generatePseudoLegalQueenMoves() {
        BitBoardState state = fenStringToBitBoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ");
        BitBoard someBoard = new BitBoard(state, generator);
        System.out.println(someBoard.toBoardString());
        assertEquals(generator.generatePseudoLegalQueenMoves(state).size(), 21);
    }
}