package chessengine;

public class Knight extends Piece{
    protected Knight(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(int deltaX, int deltaY) {
        return false;
    }
}
