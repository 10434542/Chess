package bitboard;

import java.util.List;

public interface IBoard {

    public BitBoardState makeMove();

    public BitBoardState unMakeMove();

    public List<Move> getMoves();
}
