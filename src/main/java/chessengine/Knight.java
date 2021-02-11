package chessengine;

public class Knight extends Piece{
    protected Knight(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String origin, String destination) {
        return false;
    }
}
