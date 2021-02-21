package chessengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;

public class ChessBoard {
    private final List<String> fileNames = List.of("A", "B", "C", "D", "E", "F", "G", "H");
    private Map<String, Map<Integer, Square>> allFiles;

    public ChessBoard(List<ImmutablePair<String, Piece>> squaresAndPieces) {
        assembleBoard();
        addPiecesFromPairs(squaresAndPieces);
    }
    public ChessBoard() {
        assembleBoard();
    }

    private void assembleBoard() {
        this.allFiles = new HashMap<>();
        for (int x = 1; x < fileNames.size()+1; x++) {
            allFiles.put(fileNames.get(x-1), new HashMap<>());
            for (int y = 1; y < 9; y++) {
                Square square = new Square(x,y);
                allFiles.get(fileNames.get(x-1)).put(y, square);
            }
        }
    }

    public ChessBoard addAllPieces() {
        this.allFiles.keySet().stream()
                .map(i -> this.getSquareAt(i, 2))
                .collect(Collectors.toList())
                .forEach(x -> x.setCurrentPiece(new Pawn(PlayerColor.WHITE)));
        this.allFiles.keySet().stream()
                .map(i -> this.getSquareAt(i, 7))
                .collect(Collectors.toList())
                .forEach(x -> x.setCurrentPiece(new Pawn(PlayerColor.BLACK)));

        this.allFiles.get("A").get(1).setCurrentPiece(new Rook(PlayerColor.WHITE));
        this.allFiles.get("B").get(1).setCurrentPiece(new Knight(PlayerColor.WHITE));
        this.allFiles.get("C").get(1).setCurrentPiece(new Bishop(PlayerColor.WHITE));
        this.allFiles.get("D").get(1).setCurrentPiece(new Queen(PlayerColor.WHITE));
        this.allFiles.get("E").get(1).setCurrentPiece(new King(PlayerColor.WHITE));
        this.allFiles.get("F").get(1).setCurrentPiece(new Bishop(PlayerColor.WHITE));
        this.allFiles.get("G").get(1).setCurrentPiece(new Knight(PlayerColor.WHITE));
        this.allFiles.get("H").get(1).setCurrentPiece(new Rook(PlayerColor.WHITE));

        this.allFiles.get("A").get(8).setCurrentPiece(new Rook(PlayerColor.BLACK));
        this.allFiles.get("B").get(8).setCurrentPiece(new Knight(PlayerColor.BLACK));
        this.allFiles.get("C").get(8).setCurrentPiece(new Bishop(PlayerColor.BLACK));
        this.allFiles.get("D").get(8).setCurrentPiece(new Queen(PlayerColor.BLACK));
        this.allFiles.get("E").get(8).setCurrentPiece(new King(PlayerColor.BLACK));
        this.allFiles.get("F").get(8).setCurrentPiece(new Bishop(PlayerColor.BLACK));
        this.allFiles.get("G").get(8).setCurrentPiece(new Knight(PlayerColor.BLACK));
        this.allFiles.get("H").get(8).setCurrentPiece(new Rook(PlayerColor.BLACK));

        return this;
    }

    public ChessBoard addAPiece(ImmutablePair<String, Piece> squareAndPiece) {
        Square toPlacePieceAt = this.getSquareAt(squareAndPiece.getLeft().substring(0,1), Integer.parseInt(squareAndPiece.getLeft().substring(1)));
        toPlacePieceAt.setCurrentPiece(squareAndPiece.getRight());
        return this;
    }

    @NotNull
    public ChessBoard addPiecesFromPairs(List<ImmutablePair<String, Piece>> piecesToAdd) {
        for (ImmutablePair<String, Piece> pair : piecesToAdd) {
            this.getSquareAt(pair.getLeft()).setCurrentPiece(pair.getRight());
        }
        return this;
    }

    @NotNull
    public Square getSquareAt(String a, int i) {
        return allFiles.get(a).get(i);
    }

    public Square getSquareAt(String squareName) {
        String file = squareName.substring(0,1);
        Integer rank = Integer.parseInt(squareName.substring(1));
        return this.allFiles.get(file).get(rank);
    }

    public Square getSquareAt(int x, int y) {
        return this.allFiles.get(fileNames.get(x-1)).get(y);
    }

    @NotNull
    public Piece removePiece(String a1) {
        Square square = this.getSquareAt(a1.substring(0,1), Integer.parseInt(a1.substring(1)));
        Piece piece = square.getCurrentPiece();
        square.removePiece();
        return piece;
    }

    public Move move(String origin, String destination) throws IllegalMoveException {
        // TODO: maybe add a last move tuple with destination square AND the piece, to make it easier to handle en passant
        Piece piece = getSquareAt(origin).getCurrentPiece();
        Square currentSquare = getSquareAt(origin);
        Square destinationSquare = getSquareAt(destination);
        List<Square> legalMoves = piece.getPossibleMoves(this, currentSquare.getPositionX(), currentSquare.getPositionY());

        if (piece instanceof Pawn && Math.abs(currentSquare.getPositionY() - destinationSquare.getPositionY()) == 2) {
            ((Pawn) piece).setEnPassantCapture(true);
        }

        if (legalMoves.contains(destinationSquare)) {
            getSquareAt(destination).setCurrentPiece(piece);
            getSquareAt(origin).removePiece();
            return new Move(origin, destination);
        }
        else {

            String errorMessage = piece.getClass()
                    .toString() +
                    " " +
                    origin +
                    " " +
                    destination;
            throw new IllegalMoveException(errorMessage);
        }
    }
}
