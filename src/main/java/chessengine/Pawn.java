package chessengine;

import java.util.List;

public class Pawn extends Piece {

    protected Pawn(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(Square startingSquare, Square endingSquare) {
        int xOrigin = startingSquare.getPositionX();
        int yOrigin = startingSquare.getPositionY();
        int xDestination = endingSquare.getPositionX();
        int yDestination = endingSquare.getPositionY();
        int deltaX = xDestination - xOrigin;
        int deltaY = yDestination - yOrigin;

        if (this.getColor() == PlayerColor.BLACK) {
            if (deltaY == -1 && deltaX == 0 && endingSquare.getCurrentPiece() == null) {
                return true;
            }
            else return deltaY == -1 && Math.abs(deltaX) == 1 && endingSquare.getCurrentPiece() != null;
        }
        else {
            if (deltaY == 1 && deltaX == 0 && endingSquare.getCurrentPiece() == null) {
                return true;
            }
            else return deltaY == 1 && Math.abs(deltaX) == 1 && endingSquare.getCurrentPiece() != null;
        }
    }

    @Override
    public List<Square> getAttackingSquares(Square currentSquare) {
        return null;
    }
}
