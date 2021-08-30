package mailbox;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {

    protected PlayerColor color;

    protected Piece(PlayerColor color) {
        this.color = color;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    public List<Square> getAttackSquares(ChessBoard chessBoard, int xStart, int yStart) {
        return this.getPossibleMoves(chessBoard, xStart, yStart);
    }

    public abstract List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart);

    public List<Square> getPossibleMoves(ChessBoard chessBoard, int xStart, int yStart) {
//        !(currentSquare.getCurrentPiece() instanceof King) && !(currentSquare.getCurrentPiece().getColor().equals(this.color))
        List<Square> legalMoves = new ArrayList<>();
        Square currentSquare;
        List<List<Pair<Integer, Integer>>> possibleDirections = getDirections(xStart, yStart);
        for (List<Pair<Integer, Integer>> possibleDirection : possibleDirections) {
            for (Pair<Integer, Integer> integerIntegerPair : possibleDirection) {
                int x = integerIntegerPair.getLeft();
                int y = integerIntegerPair.getRight();
                currentSquare = chessBoard.getSquareAt(x, y);
                if (currentSquare.isContested()) {
                    if (!currentSquare.getCurrentPiece().getColor().equals(this.color)) {
                        legalMoves.add(currentSquare);
                    }
                    break;
                } else {
                    legalMoves.add(currentSquare);
                }
            }
        }
        return legalMoves;

    }
}
