package bitboard.generators;

import bitboard.moves.Move;
import bitboard.engine.BitBoardState;

import java.util.List;

public interface MoveGenerator {

    public List<Move> generateMoves(BitBoardState state);

    public BitBoardState moveToState(Move move, BitBoardState state);
}
