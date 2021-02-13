package chessengine;

public class Queen extends Piece{

    protected Queen(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(int deltaX, int deltaY) {
        return false;
    }
}
