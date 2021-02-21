package chessengine;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    protected Rook(PlayerColor color) {
        super(color);
    }

    @Override
    public List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart) {
        List<Pair<Integer, Integer>> up = new ArrayList<>();
        List<Pair<Integer, Integer>> down = new ArrayList<>();
        List<Pair<Integer, Integer>> left = new ArrayList<>();
        List<Pair<Integer, Integer>> right = new ArrayList<>();
        for (int i = xStart +1; i < 9; i++) {
            right.add(new MutablePair<>(i, yStart));
        }
        for (int i = xStart -1; i > 0 ; i--) {
            left.add(new MutablePair<>(i, yStart));
        }
        for (int i = yStart +1; i < 9; i++) {
            up.add(new MutablePair<>(xStart, i));
        }
        for (int i = xStart -1; i > 0; i--) {
            down.add(new MutablePair<>(xStart, i));
        }
        return List.of(up, down, left, right);
    }
}
