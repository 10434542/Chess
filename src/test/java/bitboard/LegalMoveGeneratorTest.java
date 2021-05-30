package bitboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static bitboard.BitBoardState.fenStringToBitBoardState;
import static bitboard.BitBoardUtils.bitScanForwardDeBruijn64;
import static bitboard.BitBoardUtils.popBit;
import static bitboard.Piece.WHITE_QUEEN;
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

    @ParameterizedTest
    @CsvSource({
            "rnb1kbnr/8/8/8/8/8/8/RNBQKBNR w KQkq - 0 1 , 14", // BUG BEFORE: b means b has moved according to bitBoardState, so w needs to move; NOW: fixed b means b needs to move
            "8/8/8/8/8/7Q/8/8 w KQkq - 0 1 , 21"
    })
    void generatePseudoLegalQueenMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        BitBoard someBoard = new BitBoard(state, generator);
        assertEquals(numberOfMoves, generator.generatePseudoLegalQueenMoves(state).size());
    }
}