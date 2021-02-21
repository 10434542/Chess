package chessengine;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class King extends Piece {

    protected King(PlayerColor color) {
        super(color);
    }


    @Override
    public List<Square> getPossibleMoves(ChessBoard chessBoard, int xStart, int yStart) {
        return null;
    }

    @Override
    public List<List<Pair<Integer, Integer>>> getDirections(int xStart, int yStart) {
        return null;
    }

}
