package bitboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({
            "8/8/P7/8/8/8/8/8 w - - 0 1, 1",
            "8/8/p7/8/8/8/8/8 b - - 0 1, 1",
            "8/pppppppp/8/8/8/8/8/8 b - - 0 1, 16",
            "8/8/pppppppp/8/8/8/8/8 b - - 0 1, 8",
            "8/8/8/8/8/8/PPPPPPPP/8 w - - 0 1, 16",
            "8/8/8/8/8/PPPPPPPP/8/8 w - - 0 1, 8",
            "8/8/8/8/8/4p3/3P4/8 w - - 0 1, 3"

    })
    void generatePseudoLegalPawnMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        assertEquals(numberOfMoves, generator.generatePseudoLegalPawnMoves(state).size());
    }

    @ParameterizedTest
    @CsvSource({
            "p7/8/8/8/8/8/8/R3K3 w KQkq - 0 1, 2",
            "r3kbnr/pppppppp/8/8/8/8/PPPPPPPP/R2QK2R b KQkq - 0 1, 1",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1, 2",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 1, 1",
            "rnbqkbnr/8/8/8/8/8/8/RNBQKBNR w - - 0 1, 0",
            "rnbqkbnr/8/8/8/8/8/8/RNBQKBNR b - - 0 1, 0",
    })
    @DisplayName("Testing pseudo legal castling moves, disregarding the false moves")
    void generatePseudoLegalCastlingMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        assertEquals(numberOfMoves, generator.generatePseudoLegalCastlingMoves(state).size());
    }

    @ParameterizedTest
    @CsvSource({
            "8/8/8/3N4/8/8/8/8 w - - 0 1, 8",
            "8/8/8/3n4/8/8/8/8 b - - 0 1, 8",
            "8/8/8/3n4/8/8/8/8 b - - 0 1, 8",
            "8/8/8/1p6/3N4/1K6/2Q5/8 w - - 0 1, 6",
            "8/8/8/1K6/NQ6/8/8/8 w - - 0 1, 4",
            "8/8/8/8/8/8/8/n7 b - - 0 1, 2",
            "8/8/8/8/8/1K6/2B5/N7 w - - 0 1, 0",
    })
    @DisplayName("Testing pseudo legal Knight moves")
    void generatePseudoLegalKnightMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        assertEquals(numberOfMoves, generator.generatePseudoLegalKnightMoves(state).size());
    }

    @ParameterizedTest
    @CsvSource({
            "8/8/8/8/8/3K4/8/8 w - - 0 1, 8",
            "8/K7/8/8/8/8/8/8 w - - 0 1, 5",
            "k7/8/8/8/8/8/8/8 b - - 0 1, 3",
            "8/8/1pkp4/1bKQ4/1rBp/8/8/8 w - - 0 1, 7",
            "8/8/1PKP4/1Bkq4/1RbP/8/8/8 b - - 0 1, 7",
            "8/k7/8/8/8/8/8/8 b - - 0 1, 5"

    })
    @DisplayName("Testing pseudo legal King moves")
    void generatePseudoLegalKingMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        assertEquals(numberOfMoves, generator.generatePseudoLegalKingMoves(state).size());
    }

    @ParameterizedTest
    @CsvSource({
            "8/8/8/8/8/8/8/B7 w - - 0 1, 7",
            "8/8/8/8/8/8/8/1B6 w - - 0 1, 7",
            "8/8/8/8/8/8/3b4/8 b - - 0 1, 9",
            "8/8/8/8/8/3b4/8/8 b - - 0 1, 11",
            "8/8/8/8/3b4/8/8/8 b - - 0 1, 13",
            "8/8/8/2B5/8/8/8/8 w - - 0 1, 11",
            "8/8/8/8/8/8/8/B7 w - - 0 1, 7",
            "8/8/8/8/8/3KBr2/8/8 w - - 0 1, 11", // can check yourself when creating pseudo legal move!
            "8/8/8/8/5r2/3KBr2/8/8 w - - 0 1, 9"
    })
    @DisplayName("Testing pseudo legal Bishop moves")
    void generatePseudoLegalBishopMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        assertEquals(numberOfMoves, generator.generatePseudoLegalBishopMoves(state).size());
    }

    @ParameterizedTest
    @CsvSource({
            "8/8/8/8/8/8/8/R7 w - - 0 1, 14", // empty board
            "8/8/8/8/R7/8/8/8 w - - 0 1, 14", // empty board
            "8/8/8/8/8/2KR1r2/8/8 w - - 0 1, 9", // also counting checking yourself
            "8/8/8/8/3B4/2KRQ3/3P4/8 w - - 0 1, 0", // cant move
            "8/8/8/8/3B4/2KRk3/3P4/8 w - - 0 1, 1", // capture king
            "8/8/8/8/8/8/8/R7 w - - 0 1, 14",
            "8/8/8/8/8/8/8/R7 w - - 0 1, 14",
            "8/8/8/8/8/8/8/R7 w - - 0 1, 14",
            "8/8/8/8/8/8/8/R7 w - - 0 1, 14",
            "8/8/8/8/8/8/8/r7 b - - 0 1, 14", // empty board
            "8/8/8/8/r7/8/8/8 b - - 0 1, 14", // empty board
            "8/8/8/8/8/2kr1R2/8/8 b - - 0 1, 9", // also counting checking yourself
            "8/8/8/8/3b4/2krq3/3p4/8 b - - 0 1, 0", // cant move
            "8/8/8/8/3b4/2krK3/3b4/8 b - - 0 1, 1", // capture king
            "8/8/8/8/8/8/8/r7 b - - 0 1, 14",
            "8/8/8/8/8/8/8/r7 b - - 0 1, 14",
            "8/8/8/8/8/8/8/r7 b - - 0 1, 14",
            "8/8/8/8/8/8/8/r7 b - - 0 1, 14"
    })
    @DisplayName("testing pseudo legal rook moves")
    void generatePseudoLegalRookMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        assertEquals(numberOfMoves, generator.generatePseudoLegalRookMoves(state).size());
    }

    @ParameterizedTest
    @CsvSource({
            "rnb1kbnr/8/8/8/8/8/8/RNBQKBNR w KQkq - 0 1 , 14", // BUG BEFORE: b means b has moved according to bitBoardState, so w needs to move; NOW: fixed b means b needs to move
            "8/8/8/8/8/7Q/8/8 w KQkq - 0 1 , 21",
            "8/8/8/8/8/8/1KQ5/8 w - - 0 1, 21",
            "8/8/8/8/8/1Q6/8/8 w - - 0 1, 23",
            "8/8/8/8/8/8/1Q6/8 w - - 0 1, 23",
            "8/8/8/8/1Q6/8/8/8 w - - 0 1, 23",
            "8/8/8/1Q6/8/8/8/8 w - - 0 1, 23",
            "8/8/8/2Q5/8/8/8/8 w - - 0 1, 25",
            "8/8/8/3Q4/8/8/8/8 w - - 0 1, 27",
            "8/8/8/8/8/8/RK6/QB6 w - - 0 1, 0",
            "rnbqkbnr/8/8/8/8/8/8/RNBQKBNR b KQkq - 0 1 , 14", // BUG BEFORE: b means b has moved according to bitBoardState, so w needs to move; NOW: fixed b means b needs to move
            "8/8/8/8/8/7q/8/8 b KQkq - 0 1 , 21",
            "8/8/8/8/8/8/1kq5/8 b - - 0 1, 21",
            "8/8/8/8/8/1q6/8/8 b - - 0 1, 23",
            "8/8/8/8/8/8/1q6/8 b - - 0 1, 23",
            "8/8/8/8/1q6/8/8/8 b - - 0 1, 23",
            "8/8/8/1q6/8/8/8/8 b - - 0 1, 23",
            "8/8/8/2q5/8/8/8/8 b - - 0 1, 25",
            "8/8/8/3q4/8/8/8/8 b - - 0 1, 27",
            "8/8/8/8/8/8/rk6/qb6 b - - 0 1, 0"
    })
    @DisplayName("testing pseudo legal moves of queen")
    void generatePseudoLegalQueenMoves(String fenString, int numberOfMoves) {
        BitBoardState state = fenStringToBitBoardState(fenString);
        assertEquals(numberOfMoves, generator.generatePseudoLegalQueenMoves(state).size());
    }
}