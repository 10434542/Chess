package chessengine;

import java.util.List;

public abstract class Piece {
    protected PlayerColor color;

    protected Piece(PlayerColor color) {
        this.color = color;
    }
    public PlayerColor getColor() {
        return this.color;
    }

    public abstract boolean validateMove(Square startingSquare, Square endingSquare);

    public abstract List<Square> getAttackingSquares(Square currentSquare);
}
