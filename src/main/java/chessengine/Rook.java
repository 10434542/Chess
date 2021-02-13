package chessengine;

public class Rook extends Piece {

    protected Rook(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(int deltaX, int deltaY) {
        return false;
    }
}
