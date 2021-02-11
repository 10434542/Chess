package chessengine;

public class Queen extends Piece{

    protected Queen(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String origin, String destination) {
        return false;
    }
}
