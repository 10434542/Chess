package chessengine;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean enPassantCapture = false;

    protected Pawn(PlayerColor color) {
        super(color);
    }

    public boolean getEnPassantCapture() {
        return this.enPassantCapture;
    }

    public void setEnPassantCapture(boolean canBeCaptured) {
        this.enPassantCapture = true;
    }

    @Override
    public List<Square> getAttackSquares(ChessBoard chessBoard, int xStart, int yStart) {
        List<Pair<Integer, Integer>> attackingDirections = this.getAttackingDirections(xStart, yStart);
        List<Square> attackingSquares = new ArrayList<>();
        for (Pair<Integer, Integer> attackingDirection : attackingDirections) {
            attackingSquares.add(chessBoard.getSquareAt(attackingDirection.getLeft(), attackingDirection.getRight()));
        }
        return attackingSquares;
    }

    @Override
    public List<Square> getPossibleMoves(ChessBoard chessBoard, int xStart, int yStart) {
        List<Square> possibleSquares = new ArrayList<>();
        List<Square> attackingSquares = this.getAttackSquares(chessBoard, xStart, yStart);
        List<Pair<Integer, Integer>> normalMoves = this.getDirections(xStart, yStart).get(0);
        for (Pair<Integer, Integer> normalMove : normalMoves) {
            Square currentSquare = chessBoard.getSquareAt(normalMove.getLeft(), normalMove.getRight());
            if (!currentSquare.isContested()) {
                possibleSquares.add(currentSquare);
            }
        }
        for (Square attackingSquare : attackingSquares) {
            if (attackingSquare.isContested() && this.color != attackingSquare.getCurrentPiece().getColor()) {
                possibleSquares.add(attackingSquare);
            }
        }

        // TODO: refactor this stuff
        int rank = this.color == PlayerColor.BLACK? 4 : 5;
        if (yStart == rank && xStart < 8) {
            Square rightSquare = chessBoard.getSquareAt(xStart + 1, rank);
            Piece rightNeighbour = rightSquare.getCurrentPiece();
            Square destination = chessBoard.getSquareAt(xStart+1,yStart+1);
            if (rightNeighbour instanceof Pawn && ((Pawn) rightNeighbour).getEnPassantCapture() && !destination.isContested()) {
                possibleSquares.add(destination);
            }
        }
        if (yStart == rank && xStart > 1) {
            Square leftSquare = chessBoard.getSquareAt(xStart - 1, rank);
            Piece leftNeighbour = leftSquare.getCurrentPiece();
            Square destination = chessBoard.getSquareAt(xStart-1,yStart+1);
            if (leftNeighbour instanceof Pawn && ((Pawn) leftNeighbour).getEnPassantCapture()) {
                possibleSquares.add(destination);
            }
        }
        return possibleSquares;

    }

    private List<Pair<Integer, Integer>> getAttackingDirections(int xStart, int yStart) {
        int leftPossibleX = xStart - 1;
        int rightPossibleX = xStart + 1;
        int offset = this.color == PlayerColor.BLACK ? -1 : 1;
        int possibleY = yStart + offset;
        boolean bound = this.color == PlayerColor.BLACK ? possibleY > 0 : possibleY < 9;
        List<Pair<Integer, Integer>> attacks = new ArrayList<>();
        if (bound) {
            if (leftPossibleX > 0) {
                attacks.add(new MutablePair<>(xStart - 1, yStart + offset));
            }
            if (rightPossibleX < 9) {
                attacks.add(new ImmutablePair<>(xStart + 1, yStart + offset));
            }
        }
        return attacks;
    }

    public List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart) {
        int startingRankId = this.color == PlayerColor.BLACK ? 7 : 2;
        int offset = this.color == PlayerColor.BLACK ? -1 : 1;
        int possibleY = yStart + offset;
        boolean bound = this.color == PlayerColor.BLACK ? possibleY > 0 : possibleY < 9;
        List<Pair<Integer, Integer>> normalMoves = new ArrayList<>();
        if (bound) {
            normalMoves.add(new MutablePair<>(xStart, possibleY));
        }
        if (yStart == startingRankId) {
            normalMoves.add(new MutablePair<>(xStart, possibleY + offset));
        }

        return List.of(normalMoves);
    }
}