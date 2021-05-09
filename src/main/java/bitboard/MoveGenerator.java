package bitboard;

import java.util.List;

public interface MoveGenerator {

    public List<Move> generateMoves(SideToMove sideToMove);
}
