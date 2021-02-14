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
            "B4, WHITE",
            "B4, BLACK"
    })
    void getAttackingSquaresPawn(String startingSquare, PlayerColor pieceColor) {
        ChessBoard chessBoard = new ChessBoard(List.of(new ImmutablePair<>(startingSquare, new Pawn(pieceColor))));
        Square currentSquare = chessBoard.getSquareAt(startingSquare);
        List<Square> expectedAttackedSquares = new ArrayList<>();
        int x = currentSquare.getPositionX();
        int y = currentSquare.getPositionY();
        if (pieceColor == PlayerColor.WHITE) {
            expectedAttackedSquares.add(chessBoard.getSquareAt(x+1, y+1));
            expectedAttackedSquares.add(chessBoard.getSquareAt(x-1, y+1));
        }
        else {
            expectedAttackedSquares.add(chessBoard.getSquareAt(x+1, y-1));
            expectedAttackedSquares.add(chessBoard.getSquareAt(x-1, y-1));
        }
        List<Square> actualAttackedSquares = currentSquare.getCurrentPiece().getAttackingSquares(currentSquare);
        Assertions.assertSame(expectedAttackedSquares, actualAttackedSquares);
    }
    //</editor-fold>

    //<editor-fold desc="Bishop tests">
    @ParameterizedTest
    @CsvSource({
            "A2, B1",
    })
    void illegalBishopMoves(String initialSquare, String endingSquare) {
        ImmutablePair<String, Piece> testPair = new ImmutablePair<>(initialSquare, new Bishop(PlayerColor.BLACK));
        ChessBoard chessBoard = new ChessBoard(List.of(testPair));
        Assertions.assertThrows(IllegalMoveException.class, () -> chessBoard.move(initialSquare, endingSquare));
    }
    //</editor-fold>

}
