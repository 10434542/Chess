package chessengine;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    protected Pawn(PlayerColor color) {
        super(color);
    }

    @Override
    public List<Square> getPossibleMoves(ChessBoard chessBoard, int xStart, int yStart) {
        // commented out , int xEnd, int yEnd as parameters
        List<Square> possibleSquares = new ArrayList<>();
        int possibleY;
        int leftPossibleX;
        int rightPossibleX;
        //
        if (this.color == PlayerColor.WHITE) {
            possibleY = yStart + 1;
            leftPossibleX = xStart - 1;
            rightPossibleX = xStart + 1;
            if (possibleY < 9) {
                if (leftPossibleX > 0 && chessBoard.getSquareAt(leftPossibleX, possibleY).isContested()) {
                    possibleSquares.add(chessBoard.getSquareAt(leftPossibleX, possibleY));
                }
                if (rightPossibleX < 9 && chessBoard.getSquareAt(rightPossibleX, possibleY).isContested()) {
                    possibleSquares.add(chessBoard.getSquareAt(rightPossibleX, possibleY));
                }
                if (!(chessBoard.getSquareAt(xStart, possibleY)).isContested()) {
                    possibleSquares.add(chessBoard.getSquareAt(xStart, possibleY));
                }
            }
            if (yStart == 2 && !chessBoard.getSquareAt(xStart, possibleY).isContested()
                    && !chessBoard.getSquareAt(xStart, possibleY+1).isContested()) {
                possibleSquares.add(chessBoard.getSquareAt(xStart, possibleY +1));
            }
        }
        else {
            possibleY = yStart - 1;
            leftPossibleX = xStart - 1;
            rightPossibleX = xStart + 1;
            if (possibleY > 0) {
                if (leftPossibleX > 0 && chessBoard.getSquareAt(leftPossibleX, possibleY).isContested()) {
                    possibleSquares.add(chessBoard.getSquareAt(leftPossibleX, possibleY));
                }
                if (rightPossibleX < 9 && chessBoard.getSquareAt(rightPossibleX, possibleY).isContested()) {
                    possibleSquares.add(chessBoard.getSquareAt(rightPossibleX, possibleY));
                }
                if (!(chessBoard.getSquareAt(xStart, possibleY)).isContested()) {
                    possibleSquares.add(chessBoard.getSquareAt(xStart, possibleY));
                }
            }
            if (yStart == 7 && !chessBoard.getSquareAt(xStart, possibleY).isContested()
                    && !chessBoard.getSquareAt(xStart, possibleY - 1).isContested()) {
                possibleSquares.add(chessBoard.getSquareAt(xStart, possibleY - 1));
            }
        }
        return possibleSquares;
    }

    @Override
    protected List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart) {
        return null;
    }
}