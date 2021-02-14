package chessengine;

import java.util.List;

public class King extends Piece {

    protected King(PlayerColor color) {
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
