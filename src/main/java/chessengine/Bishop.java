package chessengine;

import java.util.List;

public class Bishop extends Piece{

    protected Bishop(PlayerColor color) {
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
