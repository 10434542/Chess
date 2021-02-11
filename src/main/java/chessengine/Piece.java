package chessengine;

public abstract class Piece {
    protected PlayerColor color;

    protected Piece(PlayerColor color) {
        this.color = color;
    }
    public PlayerColor getColor() {
        return this.color;
    }

    public abstract boolean validateMove(String origin, String destination);
}
