package mailbox;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{

    protected Bishop(PlayerColor color) {
        super(color);
    }

    public List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart) {

        List<Pair<Integer, Integer>> upperLeft = new ArrayList<>();
        List<Pair<Integer, Integer>> upperRight = new ArrayList<>();
        List<Pair<Integer, Integer>> downLeft = new ArrayList<>();
        List<Pair<Integer, Integer>> downRight = new ArrayList<>();

        int shortestDistanceUpRight = Math.min(9 - xStart, 9 - yStart);
        int shortestDistanceDownRight = Math.min(9 - xStart, yStart);
        int shortestDistanceUpLeft = Math.min(xStart, 9 - yStart);
        int shortestDistanceDownLeft = Math.min(xStart, yStart);

        for (int i = 1; i < shortestDistanceUpRight; i++) {
            upperLeft.add(new MutablePair<>(xStart +i, yStart +i));
        }
        for (int i = 1; i < shortestDistanceDownRight; i++) {
            upperRight.add(new MutablePair<>(xStart +i, yStart -i));
        }
        for (int i = 1; i < shortestDistanceDownLeft; i++) {
            downLeft.add(new MutablePair<>(xStart -i, yStart -i));
        }
        for (int i = 1; i < shortestDistanceUpLeft; i++) {
            downRight.add(new MutablePair<>(xStart -i, yStart +i));
        }
        return List.of(upperLeft,upperRight, downLeft, downRight);
    }
}
