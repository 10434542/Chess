package chessengine;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class chessBoardTest {
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
    void removePieceFromBoard() {
        List<ImmutablePair<String, Piece>> piecesInitializeWith = List.
                of(new ImmutablePair<>("A1", new Pawn(PlayerColor.BLACK)), new ImmutablePair<>("A2", new Pawn(PlayerColor.BLACK)),
                        new ImmutablePair<>("C4", new Queen(PlayerColor.WHITE)));
        ChessBoard chessBoard = new ChessBoard(piecesInitializeWith);
        chessBoard.removePiece("A1");
    }
}
