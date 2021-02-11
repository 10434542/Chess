package chessengine;

public class King extends Piece {

    protected King(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String origin, String destination) {
        return false;
    }
}
