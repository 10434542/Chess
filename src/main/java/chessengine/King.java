package chessengine;

public class King extends Piece {

    protected King(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(int deltaX, int deltaY) {
        return false;
    }
}
