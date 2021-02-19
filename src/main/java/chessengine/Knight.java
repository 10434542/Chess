package chessengine;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Knight extends Piece{

    protected Knight(PlayerColor color) {
        super(color);
    }

    @Override
    protected List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart) {
        List<Pair<Integer, Integer>> coordinates = new ArrayList<>();

//        if (xStart + 1 < 9) {
//            if (yStart + 2 < 9) {
//                coordinates.add(new MutablePair<>(xStart+1, yStart+2));
//            }
//            if (yStart - 2 > 0) {
//                coordinates.add(new MutablePair<>(xStart+1, yStart-2));
//            }
//        }
//        if (xStart + 2 < 9) {
//            if (yStart + 1 < 9) {
//                coordinates.add(new MutablePair<>(xStart+2, yStart+1));
//            }
//            if (yStart - 1 > 0) {
//                coordinates.add(new MutablePair<>(xStart+2, yStart-1));
//            }
//        }
//        if (xStart - 1 > 0) {
//            if (yStart + 2 < 9) {
//                coordinates.add(new MutablePair<>(xStart-1, yStart+2));
//            }
//            if (yStart - 2 > 0) {
//                coordinates.add(new MutablePair<>(xStart-1, yStart-2));
//            }
//        }
//        if (xStart - 2 < 9) {
//            if (yStart + 1 < 9) {
//                coordinates.add(new MutablePair<>(xStart-2, yStart+1));
//            }
//            if (yStart - 1 > 0) {
//                coordinates.add(new MutablePair<>(xStart-2, yStart-1));
//            }
//        }
        coordinates.add(new MutablePair<>(xStart+1, yStart+2));
        coordinates.add(new MutablePair<>(xStart+1, yStart-2));
        coordinates.add(new MutablePair<>(xStart+2, yStart+1));
        coordinates.add(new MutablePair<>(xStart+2, yStart-1));
        coordinates.add(new MutablePair<>(xStart-1, yStart+2));
        coordinates.add(new MutablePair<>(xStart-1, yStart-2));
        coordinates.add(new MutablePair<>(xStart-2, yStart+1));
        coordinates.add(new MutablePair<>(xStart-2, yStart-1));
        return List.of(coordinates.stream().
                filter(x -> x.getLeft() > 0 && x.getRight() > 0 && x.getLeft() < 9 && x.getRight() < 9).collect(Collectors.toList()));
    }

}
