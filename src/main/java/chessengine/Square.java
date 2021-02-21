package chessengine;

import java.util.Objects;

public class Square {

    private Piece currentPiece;
    private final int positionX;
    private final int positionY;
    private boolean contested;

    public Square(int positionX, int positionY) {

        this.positionX = positionX;
        this.positionY = positionY;
        this.contested = false;
    }

    public Piece getCurrentPiece() {
        return this.currentPiece;
    }

    public void setCurrentPiece(Piece piece) {
        this.currentPiece = piece;
        this.contested = true;
    }

    public boolean isContested() {
        return this.contested;
    }

    public void removePiece() {
        this.currentPiece = null;
        this.contested = false;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square)) return false;
        Square square = (Square) o;
        return positionX == square.positionX &&
                positionY == square.positionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY);
    }
}
