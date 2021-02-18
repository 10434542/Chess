package chessengine;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{

    protected Queen(PlayerColor color) {
        super(color);
    }

    @Override
    public List<List<Pair<Integer, Integer>>> getDirections(int x, int y) {
        List<Pair<Integer, Integer>> up = new ArrayList<>();
        List<Pair<Integer, Integer>> down = new ArrayList<>();
        List<Pair<Integer, Integer>> left = new ArrayList<>();
        List<Pair<Integer, Integer>> right = new ArrayList<>();
        for (int i = x; i < 9; i++) {
            right.add(new MutablePair<>(i, y));
        }
        for (int i = x; i > 0 ; i--) {
            left.add(new MutablePair<>(i, y));
        }
        for (int i = y; i < 9; i++) {
            up.add(new MutablePair<>(x, i));
        }
        for (int i = x; i > 0; i--) {
            down.add(new MutablePair<>(x, i));
        }
        List<Pair<Integer, Integer>> upperLeft = new ArrayList<>();
        List<Pair<Integer, Integer>> upperRight = new ArrayList<>();
        List<Pair<Integer, Integer>> downLeft = new ArrayList<>();
        List<Pair<Integer, Integer>> downRight = new ArrayList<>();

        int shortestDistanceUpRight = Math.min(8 - x, 9 - y);
        int shortestDistanceDownRight = Math.min(8 - x, y);
        int shortestDistanceUpLeft = Math.min(x, 9 - y);
        int shortestDistanceDownLeft = Math.min(x, y);

        for (int i = 1; i < shortestDistanceUpRight; i++) {
            upperLeft.add(new MutablePair<>(x+i,y+i));
        }
        for (int i = 1; i < shortestDistanceDownRight; i++) {
            upperRight.add(new MutablePair<>(x+i, y-i));
        }
        for (int i = 1; i < shortestDistanceDownLeft; i++) {
            downLeft.add(new MutablePair<>(x-i, y-i));
        }
        for (int i = 1; i < shortestDistanceUpLeft; i++) {
            downRight.add(new MutablePair<>(x-i, y+i));
        }

        return List.of(up, down, left, right, downLeft, downRight, upperLeft, upperRight);
    }
}
