package chessengine;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    private boolean isCheckMated;

    protected King(PlayerColor color) {
        super(color);
    }
    private boolean isChecked = false;

    @Override
    public List<Square> getAttackSquares(ChessBoard chessBoard, int xStart, int yStart) {
        List<Square> attackingSquares = new ArrayList<>();
        List<List<Pair<Integer, Integer>>> possibleDirections = this.getDirections(xStart, yStart);
        for (List<Pair<Integer, Integer>> possibleDirection : possibleDirections) {
            for (Pair<Integer, Integer> direction : possibleDirection) {
                Square destinationSquare = chessBoard.getSquareAt(direction.getLeft(), direction.getRight());

                attackingSquares.add(destinationSquare);
            }
        }
        return attackingSquares;
    }

    @Override
    public List<Square> getPossibleMoves(ChessBoard chessBoard, int xStart, int yStart) {
        List<Square> possibleMoves = new ArrayList<>();
        List<List<Pair<Integer, Integer>>> possibleDirections = this.getDirections(xStart, yStart);
        for (List<Pair<Integer, Integer>> possibleDirection : possibleDirections) {
            for (Pair<Integer, Integer> direction : possibleDirection) {
                Square destinationSquare = chessBoard.getSquareAt(direction.getLeft(), direction.getRight());
                if (chessBoard.getAttackingSquaresOpponent(this.getColor()).isEmpty() ||
                        !chessBoard.getAttackingSquaresOpponent(this.getColor()).contains(destinationSquare)) {
                    possibleMoves.add(destinationSquare);
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart) {
        List<Pair<Integer, Integer>> up = new ArrayList<>();
        List<Pair<Integer, Integer>> down = new ArrayList<>();
        List<Pair<Integer, Integer>> left = new ArrayList<>();
        List<Pair<Integer, Integer>> right = new ArrayList<>();

        if (xStart - 1 > 0) {
            left.add(new MutablePair<>(xStart-1,yStart));
            if (yStart - 1 > 0) {
                left.add(new MutablePair<>(xStart-1, yStart -1));
            }
            if (yStart + 1 < 9) {
                left.add(new MutablePair<>(xStart-1, yStart+1));
            }
        }
        if (xStart + 1 < 9) {
            right.add(new MutablePair<>(xStart+1, yStart));
            if (yStart - 1 > 0) {
                right.add(new MutablePair<>(xStart+1, yStart -1));
            }
            if (yStart + 1 < 9) {
                right.add(new MutablePair<>(xStart+1, yStart+1));
            }
        }
        if (yStart + 1 < 9) {
            up.add(new MutablePair<>(xStart, yStart+1));
        }
        if (yStart - 1 > 0) {
            down.add(new MutablePair<>(xStart, yStart-1));
        }

        return List.of(up, down, left, right);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean b) {
        this.isChecked = b;
    }

    public boolean isCheckMated() {
        return isCheckMated;
    }

    public void setCheckMated() {
        this.isCheckMated = true;
    }
}
