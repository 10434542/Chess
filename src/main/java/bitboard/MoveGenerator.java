package bitboard;

import java.util.List;

public interface MoveGenerator {

    public List<Move> generateMoves(BitBoardState state);

    public boolean isSquareAttacked(int square, int side, BitBoardState state);

    public BitBoardState moveToState(Move move, BitBoardState state);
}
