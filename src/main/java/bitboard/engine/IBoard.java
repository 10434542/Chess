package bitboard.engine;

import bitboard.moves.Move;

import java.util.List;

public interface IBoard {

    public void makeMove(Move move);

    public void unMakeMove();

    public List<Move> getMoves();
}
