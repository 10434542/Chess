package chessengine;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{

    protected Bishop(PlayerColor color) {
        super(color);
    }

    @Override
    public List<List<Pair<Integer, Integer>>> getDirections(int x, int y) {

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
        return List.of(upperLeft,upperRight, downLeft, downRight);
    }
}
