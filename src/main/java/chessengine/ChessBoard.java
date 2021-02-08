package chessengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class ChessBoard {

    private Map<String, Map<Integer, Square>> allFiles;

    public ChessBoard(List<ImmutablePair<String, Piece>> squaresAndPieces) {
        assembleBoard();
    }
    public ChessBoard() {
        assembleBoard();
    }

    private void assembleBoard() {
        this.allFiles = new HashMap<>();
        for (int j = 65; j < 73; j++) {
            String currentKey = Character.toString((char) j);
            allFiles.put(currentKey, new HashMap<>());
            for (int i = 1; i < 9; i++) {
                Square square = new Square();
                allFiles.get(currentKey).put(i, square);
            }
        }
    }
    public ChessBoard addAllPieces() {
        this.allFiles.keySet().stream()
                .map(i -> this.getSquareAt(i, 1))
                .collect(Collectors.toList())
                .forEach(x -> x.setCurrentPiece(new Pawn(PlayerColor.WHITE)));
        this.allFiles.keySet().stream()
                .map(i -> this.getSquareAt(i, 6))
                .collect(Collectors.toList())
                .forEach(x -> x.setCurrentPiece(new Pawn(PlayerColor.BLACK)));
        return this;
    }

    public ChessBoard addAPiece(ImmutablePair<String, Piece> squareAndPiece) {
        Square toPlacePieceAt = this.getSquareAt(squareAndPiece.left.substring(0,1), Integer.parseInt(squareAndPiece.left.substring(1)));
        toPlacePieceAt.setCurrentPiece(squareAndPiece.right);
        return this;
    }

    public ChessBoard addPieces(List<ImmutablePair<String, ? extends Piece>> squaresAndPieces) {
        return this;
    }

    public Square getSquareAt(String a, int i) {
        return allFiles.get(a).get(i);
    }
}
