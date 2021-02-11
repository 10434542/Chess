package chessengine;

public class Pawn extends Piece {

    protected Pawn(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String origin, String destination) {
        return false;
    }
}
