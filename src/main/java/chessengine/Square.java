package chessengine;

public class Square {

    private Piece currentPiece;
    private final int positionX;
    private final int positionY;

    public Square(int positionX, int positionY) {

        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Piece getCurrentPiece() {
        return this.currentPiece;
    }

    public void setCurrentPiece(Piece piece) {
        this.currentPiece = piece;
    }

    public void removePiece() {
        this.currentPiece = null;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
}
