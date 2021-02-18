package chessengine;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Knight extends Piece{
    protected Knight(PlayerColor color) {
        super(color);
    }

    @Override
    protected List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart) {
        return null;
    }

}
