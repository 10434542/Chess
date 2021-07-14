package bitboard;

import java.util.List;

public interface MoveGenerator {

    public List<Move> generateMoves(BitBoardState state);

    public BitBoardState moveToState(Move move, BitBoardState state);
}
