package chessengine;

public class Bishop extends Piece{

    protected Bishop(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(int deltaX, int deltaY) {
        return false;
    }
}
