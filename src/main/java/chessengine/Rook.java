package chessengine;

public class Rook extends Piece {

    protected Rook(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String origin, String destination) {
        return false;
    }
}
