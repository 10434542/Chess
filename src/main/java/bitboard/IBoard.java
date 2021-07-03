package bitboard;

import chessengine.IllegalMoveException;

import java.util.List;

public interface IBoard {

    public void makeMove(Move move) throws IllegalMoveException;

    public void unMakeMove();

    public List<Move> getMoves();
}
