package chessengine;

public class Square {

    private Piece currentPiece;

    public Piece getCurrentPiece() {
        return this.currentPiece;
    }

    public void setCurrentPiece(Piece piece) {
        this.currentPiece = piece;
    }
}
