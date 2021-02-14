package chessengine;

import java.util.List;

public class Knight extends Piece{
    protected Knight(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(Square startingSquare, Square endingSquare) {
        return false;
    }

    @Override
    public List<Square> getAttackingSquares(Square currentSquare) {
        return null;
    }
}
