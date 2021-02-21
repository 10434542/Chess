package chessengine;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class chessBoardTest {

    //<editor-fold desc="Board initialization tests">
    private void assertPiecesPresentOnStandardBoard(PlayerColor color, ChessBoard currentBoard) {
        List<Square> pawnSquares, otherPieces;
        String files = "ABCDEFGH";

        if (color == PlayerColor.BLACK) {
            pawnSquares = files.codePoints().mapToObj(c -> String.valueOf((char) c)).map(i -> currentBoard.getSquareAt(i, 7)).collect(Collectors.toList());
            otherPieces = files.codePoints().mapToObj(c -> String.valueOf((char) c)).map(i -> currentBoard.getSquareAt(i, 8)).collect(Collectors.toList());

        }
        else {
            pawnSquares = files.codePoints().mapToObj(c -> String.valueOf((char) c)).map(i -> currentBoard.getSquareAt(i, 2)).collect(Collectors.toList());
            otherPieces = files.codePoints().mapToObj(c -> String.valueOf((char) c)).map(i -> currentBoard.getSquareAt(i, 1)).collect(Collectors.toList());
        }
        Assertions.assertAll(
                () -> Assertions.assertTrue(pawnSquares.get(0).getCurrentPiece() instanceof Pawn && pawnSquares.get(0).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(pawnSquares.get(1).getCurrentPiece() instanceof Pawn && pawnSquares.get(1).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(pawnSquares.get(2).getCurrentPiece() instanceof Pawn && pawnSquares.get(2).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(pawnSquares.get(3).getCurrentPiece() instanceof Pawn && pawnSquares.get(3).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(pawnSquares.get(4).getCurrentPiece() instanceof Pawn && pawnSquares.get(4).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(pawnSquares.get(5).getCurrentPiece() instanceof Pawn && pawnSquares.get(5).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(pawnSquares.get(6).getCurrentPiece() instanceof Pawn && pawnSquares.get(6).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(pawnSquares.get(7).getCurrentPiece() instanceof Pawn && pawnSquares.get(7).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(otherPieces.get(0).getCurrentPiece() instanceof Rook && otherPieces.get(0).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(otherPieces.get(1).getCurrentPiece() instanceof Knight && otherPieces.get(1).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(otherPieces.get(2).getCurrentPiece() instanceof Bishop && otherPieces.get(2).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(otherPieces.get(3).getCurrentPiece() instanceof Queen && otherPieces.get(3).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(otherPieces.get(4).getCurrentPiece() instanceof King && otherPieces.get(4).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(otherPieces.get(5).getCurrentPiece() instanceof Bishop && otherPieces.get(5).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(otherPieces.get(6).getCurrentPiece() instanceof Knight && otherPieces.get(6).getCurrentPiece().getColor() == color),
                () -> Assertions.assertTrue(otherPieces.get(7).getCurrentPiece() instanceof Rook && otherPieces.get(7).getCurrentPiece().getColor() == color));
    }
    @Test // simple test
    void allSquaresExist() {
        ChessBoard chessBoard = new ChessBoard();
        Square ofInterest = chessBoard.getSquareAt("A", 1);
        Assertions.assertNotNull(ofInterest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(8, (int) Stream.iterate(1, n -> n + 1).limit(8).map(n -> chessBoard.getSquareAt("A", n)).count()),
                () -> Assertions.assertEquals(8, (int) Stream.iterate(1, n -> n + 1).limit(8).map(n -> chessBoard.getSquareAt("B", n)).count()),
                () -> Assertions.assertEquals(8, (int) Stream.iterate(1, n -> n + 1).limit(8).map(n -> chessBoard.getSquareAt("C", n)).count()),
                () -> Assertions.assertEquals(8, (int) Stream.iterate(1, n -> n + 1).limit(8).map(n -> chessBoard.getSquareAt("D", n)).count()),
                () -> Assertions.assertEquals(8, (int) Stream.iterate(1, n -> n + 1).limit(8).map(n -> chessBoard.getSquareAt("E", n)).count()),
                () -> Assertions.assertEquals(8, (int) Stream.iterate(1, n -> n + 1).limit(8).map(n -> chessBoard.getSquareAt("F", n)).count()),
                () -> Assertions.assertEquals(8, (int) Stream.iterate(1, n -> n + 1).limit(8).map(n -> chessBoard.getSquareAt("G", n)).count()),
                () -> Assertions.assertEquals(8, (int) Stream.iterate(1, n -> n + 1).limit(8).map(n -> chessBoard.getSquareAt("H", n)).count()));

    }

    @Test
    void addPieceToSquare() {
        ChessBoard chessBoard = new ChessBoard().addAPiece(new ImmutablePair<>("A1", new Pawn(PlayerColor.WHITE)));
        Assertions.assertAll(
                () -> Assertions.assertNotNull(chessBoard.getSquareAt("A", 1).getCurrentPiece()),
                () -> Assertions.assertTrue(chessBoard.getSquareAt("A", 1).getCurrentPiece() instanceof Pawn));
    }

    @Test
    void boardCanBeBuildWithAllPiecesPresent() {
        ChessBoard chessBoard = new ChessBoard().addAllPieces();
        assertPiecesPresentOnStandardBoard(PlayerColor.WHITE, chessBoard);
        assertPiecesPresentOnStandardBoard(PlayerColor.BLACK, chessBoard);
        chessBoard.getSquareAt("A", 1);
        System.out.println(chessBoard.getSquareAt("A", 1).getCurrentPiece());
    }

    @Test
    void createBoardWithListOfTuples() {
        List<ImmutablePair<String, Piece>> piecesInitializeWith = List.
                of(new ImmutablePair<>("A1", new Pawn(PlayerColor.BLACK)), new ImmutablePair<>("A2", new Pawn(PlayerColor.BLACK)),
                        new ImmutablePair<>("C4", new Queen(PlayerColor.WHITE)));
        ChessBoard chessBoard = new ChessBoard(piecesInitializeWith);
        Assertions.assertAll(
                () -> Assertions.assertTrue(chessBoard.getSquareAt("A", 1).getCurrentPiece() instanceof Pawn),
                () -> Assertions.assertTrue(chessBoard.getSquareAt("A", 2).getCurrentPiece() instanceof Pawn),
                () -> Assertions.assertTrue(chessBoard.getSquareAt("C", 4).getCurrentPiece() instanceof Queen));
    }

    @Test
    void removePiecesFromBoard() {
        List<ImmutablePair<String, Piece>> piecesInitializeWith = List.
                of(new ImmutablePair<>("A1", new Pawn(PlayerColor.BLACK)), new ImmutablePair<>("A2", new Pawn(PlayerColor.BLACK)),
                        new ImmutablePair<>("C4", new Queen(PlayerColor.WHITE)));
        ChessBoard chessBoard = new ChessBoard(piecesInitializeWith);
        Piece aPawn = chessBoard.removePiece("A1");
        Piece anotherPawn = chessBoard.removePiece("A2");
        Piece aQueen = chessBoard.removePiece("C4");
        Assertions.assertAll(
                () -> Assertions.assertNull(chessBoard.getSquareAt("A", 1).getCurrentPiece()),
                () -> Assertions.assertNull(chessBoard.getSquareAt("A", 2).getCurrentPiece()),
                () -> Assertions.assertNull(chessBoard.getSquareAt("A", 3).getCurrentPiece()),
                () -> Assertions.assertTrue(aPawn instanceof Pawn),
                () -> Assertions.assertTrue(anotherPawn instanceof Pawn),
                () -> Assertions.assertTrue(aQueen instanceof Queen)
        );
    }
    //</editor-fold>

    //<editor-fold desc="Pawn tests">
    @ParameterizedTest
    @CsvSource({
            "B7, B8, BLACK",
            "B7, B4, BLACK",
            "B6, B4, BLACK",
            "B7, A8, BLACK",
            "B7, A7, BLACK",
            "B7, A6, BLACK",
            "B7, C8, BLACK",
            "B7, C7, BLACK",
            "B7, C6, BLACK",
            "B2, A3, WHITE",
            "B2, A2, WHITE",
            "B2, A1, WHITE",
            "B2, C3, WHITE",
            "B2, C2, WHITE",
            "B2, C1, WHITE",
            "B2, B1, WHITE",
            "B2, B5, WHITE",
            "B3, B5, WHITE"
    })
    void illegalPawnMoves(String initialSquare, String endingSquare, PlayerColor color) {
        ImmutablePair<String, Piece> testPair = new ImmutablePair<>(initialSquare, new Pawn(color));
        ChessBoard chessBoard = new ChessBoard(List.of(testPair));
        Assertions.assertThrows(IllegalMoveException.class, () -> chessBoard.move(initialSquare, endingSquare));
    }

    @ParameterizedTest
    @CsvSource({
            "B4, C5, WHITE, BLACK",
            "B4, A5, WHITE, BLACK",
            "B4, C3, BLACK, WHITE",
            "B4, A3, BLACK, WHITE"
    })
    void pawnCanCaptureAttackingSquare(String initialSquare, String attackingSquare,
                                       PlayerColor attackerColor, PlayerColor attackedColor) throws IllegalMoveException {
        Piece attacked = new Pawn(attackedColor);
        Piece attacker = new Pawn(attackerColor);
        ImmutablePair<String, Piece> testPair = new ImmutablePair<>(initialSquare, attacker);
        ImmutablePair<String, Piece> capturePair = new ImmutablePair<>(attackingSquare, attacked);
        ChessBoard chessBoard = new ChessBoard(List.of(testPair, capturePair));

        chessBoard.move(initialSquare, attackingSquare);
        Assertions.assertSame(chessBoard.getSquareAt(attackingSquare).getCurrentPiece(), attacker);
    }

    @ParameterizedTest
    @CsvSource({
            "B5, B6, WHITE",
            "B5, C7, WHITE",
            "B5, A7, WHITE"
    })
    void illegalPawnMovesInvolvingPiecesOfSameColor(String pawnSquare, String otherPieceSquare, PlayerColor color) {
        ChessBoard chessBoard = new ChessBoard(List.of(
                new ImmutablePair<>(pawnSquare, new Pawn(color)),
                new ImmutablePair<>(otherPieceSquare, new Pawn(color))));
        Assertions.assertThrows(IllegalMoveException.class, () -> chessBoard.move(pawnSquare, otherPieceSquare));
    }

    @Test
    void pawnCanMoveTwoSquaresFromStartingRank() throws IllegalMoveException {
        ChessBoard chessBoard = new ChessBoard().addAllPieces();
        chessBoard.move("A2", "A4");
        chessBoard.move("A7", "A6");
        Assertions.assertAll(
                () -> Assertions.assertTrue(chessBoard.getSquareAt("A4").getCurrentPiece() instanceof Pawn),
                () -> Assertions.assertTrue(chessBoard.getSquareAt("A6").getCurrentPiece() instanceof Pawn));
    }

    //</editor-fold>

    //<editor-fold desc="En passant tests">
    //TODO en passant capture/attack.
    @Test
    void pawnCanTakeEnPassant() throws IllegalMoveException {
        List<ImmutablePair<String, Piece>> pieces = List.of(
                new ImmutablePair<>("A5", new Pawn(PlayerColor.WHITE)),
                new ImmutablePair<>("B7", new Pawn(PlayerColor.BLACK)));
        ChessBoard chessBoard = new ChessBoard(pieces);
        chessBoard.move("B7","B5");
        Pawn blackPawn = (Pawn) chessBoard.getSquareAt("B5").getCurrentPiece();
        System.out.println(blackPawn.getEnPassantCapture());
        Assertions.assertDoesNotThrow(() -> chessBoard.move("A5", "B6"));

    }
    //</editor-fold>

    //<editor-fold desc="Bishop tests">
    @ParameterizedTest
    @CsvSource({
            "A2, B2",
            "D4, E4",
            "D4, C4",
            "D4, D3",
            "D4, D5"
    })
    void illegalBishopMoveDirections(String initialSquare, String endingSquare) {
        ImmutablePair<String, Piece> testPair = new ImmutablePair<>(initialSquare, new Bishop(PlayerColor.BLACK));
        ChessBoard chessBoard = new ChessBoard(List.of(testPair));
        Assertions.assertThrows(IllegalMoveException.class, () -> chessBoard.move(initialSquare, endingSquare));
    }

    @ParameterizedTest
    @CsvSource({
            "D4, E5, F6",
            "D4, E3, F2",
            "D4, C5, B6",
            "D4, C3, B2",

    })
    void bishopCantMoveThroughPieces(String initialSquare, String obstructingSquare, String destinationSquare) {
        List<ImmutablePair<String, Piece>> blackPieces = List.of(
                new ImmutablePair<>(initialSquare, new Bishop(PlayerColor.BLACK)),
                new ImmutablePair<>(obstructingSquare, new Pawn(PlayerColor.BLACK)));
        List<ImmutablePair<String, Piece>> blackAndWhitePieces = List.of(
                new ImmutablePair<>(initialSquare, new Bishop(PlayerColor.BLACK)),
                new ImmutablePair<>(obstructingSquare, new Pawn(PlayerColor.WHITE)));
        ChessBoard chessBoardBlack = new ChessBoard(blackPieces);
        ChessBoard chessBoardBlackAndWhite = new ChessBoard(blackAndWhitePieces);
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalMoveException.class, () -> chessBoardBlack.move(initialSquare, destinationSquare)),
                () -> Assertions.assertThrows(IllegalMoveException.class, () -> chessBoardBlackAndWhite.move(initialSquare, destinationSquare)));
    }

    @ParameterizedTest
    @CsvSource({
            "D4, E5",
            "D4, E3",
            "D4, C5",
            "D4, C3",
    })
    void bishopCantTakePieceOfIdenticalColor(String initialSquare, String destinationSquare) {
        List<ImmutablePair<String, Piece>> blackPieces = List.of(
                new ImmutablePair<>(initialSquare, new Bishop(PlayerColor.BLACK)),
                new ImmutablePair<>(destinationSquare, new Pawn(PlayerColor.BLACK)));
        List<ImmutablePair<String, Piece>> whitePieces = List.of(
                new ImmutablePair<>(initialSquare, new Bishop(PlayerColor.WHITE)),
                new ImmutablePair<>(destinationSquare, new Pawn(PlayerColor.WHITE)));
        ChessBoard chessBoardBlack = new ChessBoard(blackPieces);
        ChessBoard chessBoardWhite = new ChessBoard(whitePieces);
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalMoveException.class, () -> chessBoardBlack.move(initialSquare, destinationSquare)),
                () -> Assertions.assertThrows(IllegalMoveException.class, () -> chessBoardWhite.move(initialSquare, destinationSquare)));

    }

    @ParameterizedTest
    @CsvSource({
            "D4, F6",
            "D4, B6",
            "D4, F2",
            "D4, B2",
            "D4, E5",
            "D4, C5"
    })
    void bishopCanTakePieces(String initialSquare, String destinationSquare) throws IllegalMoveException {
        List<ImmutablePair<String, Piece>> pieces = List.of(
                new ImmutablePair<>(initialSquare, new Bishop(PlayerColor.BLACK)),
                new ImmutablePair<>(destinationSquare, new Queen(PlayerColor.WHITE)));
        ChessBoard chessBoard = new ChessBoard(pieces);
        chessBoard.move(initialSquare, destinationSquare);
        Assertions.assertTrue(chessBoard.getSquareAt(destinationSquare).getCurrentPiece() instanceof Bishop);
    }
    //</editor-fold>

    //<editor-fold desc="Rook tests">

    @ParameterizedTest
    @CsvSource({
            "D4, C5",
            "D4, C3",
            "D4, E5",
            "D4, E3"
    })
    void illegalRookMoves(String initialSquare, String destinationSquare) {
        ImmutablePair<String, Piece> testPair = new ImmutablePair<>(initialSquare, new Rook(PlayerColor.BLACK));
        ChessBoard chessBoard = new ChessBoard(List.of(testPair));
        Assertions.assertThrows(IllegalMoveException.class, () -> chessBoard.move(initialSquare, destinationSquare));
    }

    @ParameterizedTest
    @CsvSource({
            "D4, C4, B4",
            "D4, D5, D6",
            "D4, E4, F6",
            "D4, D3, D2"
    })
    void rookCantMoveThroughPieces(String initialSquare, String obstructingSquare, String destinationSquare) {
        List<ImmutablePair<String, Piece>> blackPieces = List.of(
                new ImmutablePair<>(initialSquare, new Rook(PlayerColor.BLACK)),
                new ImmutablePair<>(obstructingSquare, new Pawn(PlayerColor.BLACK)));
        List<ImmutablePair<String, Piece>> blackAndWhitePieces = List.of(
                new ImmutablePair<>(initialSquare, new Rook(PlayerColor.BLACK)),
                new ImmutablePair<>(obstructingSquare, new Pawn(PlayerColor.WHITE)));
        ChessBoard chessBoardBlack = new ChessBoard(blackPieces);
        ChessBoard chessBoardWhite = new ChessBoard(blackAndWhitePieces);
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalMoveException.class, () -> chessBoardBlack.move(initialSquare, destinationSquare)),
                () -> Assertions.assertThrows(IllegalMoveException.class, () -> chessBoardWhite.move(initialSquare, destinationSquare)));
    }

    @ParameterizedTest
    @CsvSource({
            "D4, C4",
            "D4, D5",
            "D4, E4",
            "D4, D3"

    })
    void rookCantTakePieceOfIdenticalColor(String initialSquare, String destinationSquare) {
        List<ImmutablePair<String, Piece>> blackPieces = List.of(
                new ImmutablePair<>(initialSquare, new Rook(PlayerColor.BLACK)),
                new ImmutablePair<>(destinationSquare, new Pawn(PlayerColor.BLACK)));
        List<ImmutablePair<String, Piece>> whitePieces = List.of(
                new ImmutablePair<>(initialSquare, new Rook(PlayerColor.WHITE)),
                new ImmutablePair<>(destinationSquare, new Pawn(PlayerColor.WHITE)));
        ChessBoard chessBoardBlack = new ChessBoard(blackPieces);
        ChessBoard chessBoardWhite = new ChessBoard(whitePieces);
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalMoveException.class, () -> chessBoardBlack.move(initialSquare, destinationSquare)),
                () -> Assertions.assertThrows(IllegalMoveException.class, () -> chessBoardWhite.move(initialSquare, destinationSquare)));
    }

    @ParameterizedTest
    @CsvSource({
            "D4, C4",
            "D4, D5",
            "D4, E4",
            "D4, D3"

    })
    void rookCanTakePieces(String initialSquare, String destinationSquare) {
        List<ImmutablePair<String, Piece>> blackAndWhitePieces = List.of(
                new ImmutablePair<>(initialSquare, new Rook(PlayerColor.BLACK)),
                new ImmutablePair<>(destinationSquare, new Pawn(PlayerColor.WHITE)));
        ChessBoard chessBoard = new ChessBoard(blackAndWhitePieces);
        Assertions.assertDoesNotThrow(()-> chessBoard.move(initialSquare, destinationSquare));
    }
    //</editor-fold>

    //<editor-fold desc="Knight tests">

    @ParameterizedTest
    @CsvSource({
            "D4, E5",
            "D4, E4",
            "D4, E3",
            "D4, D5",
            "D4, D3",
            "D4, C5",
            "D4, C4",
            "D4, C3"
    })
    void illegalKnightMoves(String initialSquare, String destinationSquare) {
        ImmutablePair<String, Piece> testPair = new ImmutablePair<>(initialSquare, new Knight(PlayerColor.WHITE));
        ChessBoard chessBoard = new ChessBoard(List.of(testPair));
        Assertions.assertThrows(IllegalMoveException.class, () -> chessBoard.move(initialSquare, destinationSquare));
    }

    @ParameterizedTest
    @CsvSource({
            "D4, C6",
            "D4, C2",
            "D4, B5",
            "D4, B3",
            "D4, E6",
            "D4, E2",
            "D4, F5",
            "D4, F3"
    })
    void legalKnightMoves(String initialSquare, String destinationSquare) {
        ImmutablePair<String, Piece> testPair = new ImmutablePair<>(initialSquare, new Knight(PlayerColor.WHITE));
        ChessBoard chessBoard = new ChessBoard(List.of(testPair));
        Assertions.assertDoesNotThrow(() -> chessBoard.move(initialSquare, destinationSquare));
    }

    @Test
    void knightCanJumpOverPieces() {
        ChessBoard chessBoard = new ChessBoard().addAllPieces();
        Assertions.assertDoesNotThrow(() -> chessBoard.move("B1", "C3"));
    }

    @Test
    void knightCanTakePiece() throws IllegalMoveException {
        ImmutablePair<String, Piece> knight = new ImmutablePair<>("A1", new Knight(PlayerColor.WHITE));
        ImmutablePair<String, Piece> pawn = new ImmutablePair<>("B3", new Pawn(PlayerColor.BLACK));
        ChessBoard chessBoard = new ChessBoard(List.of(pawn, knight));
        chessBoard.move("A1", "B3");
        Assertions.assertTrue(chessBoard.getSquareAt("B3").getCurrentPiece() instanceof Knight);
    }

    @Test
    void knightCantTakePieceOfIdenticalColor() throws IllegalMoveException {
        ImmutablePair<String, Piece> knight = new ImmutablePair<>("A1", new Knight(PlayerColor.WHITE));
        ImmutablePair<String, Piece> pawn = new ImmutablePair<>("B3", new Pawn(PlayerColor.WHITE));
        ChessBoard chessBoard = new ChessBoard(List.of(pawn, knight));

        Assertions.assertThrows(IllegalMoveException.class, () -> chessBoard.move("A1", "B3"));
    }
    //</editor-fold>

    //<editor-fold desc="Queen tests">

    //TODO add queen tests!

    //</editor-fold>

    //<editor-fold desc="King tests">

    //TODO add King tests (take forks/pins into account)

    //</editor-fold>
}
