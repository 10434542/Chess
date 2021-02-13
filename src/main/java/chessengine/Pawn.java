package chessengine;

public class Pawn extends Piece {

    protected Pawn(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(int deltaX, int deltaY) {
        return deltaX == 1 || deltaY == 1;
    }
}
