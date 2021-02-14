package chessengine;

import java.util.List;

public class Rook extends Piece {

    protected Rook(PlayerColor color) {
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
