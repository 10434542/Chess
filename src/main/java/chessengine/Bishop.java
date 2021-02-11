package chessengine;

public class Bishop extends Piece{

    protected Bishop(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String origin, String destination) {
        return false;
    }
}
