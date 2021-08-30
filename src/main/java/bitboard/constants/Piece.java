package bitboard.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Piece {
    public static final int WHITE_PAWN = 0;
    public static final int WHITE_KNIGHT = 1;
    public static final int WHITE_BISHOP = 2;
    public static final int WHITE_ROOK = 3;
    public static final int WHITE_QUEEN = 4;
    public static final int WHITE_KING = 5;
    public static final int BLACK_PAWN = 6;
    public static final int BLACK_KNIGHT = 7;
    public static final int BLACK_BISHOP = 8;
    public static final int BLACK_ROOK = 9;
    public static final int BLACK_QUEEN = 10;
    public static final int BLACK_KING = 11;
    public static final int[] MATERIAL_SCORE = {
            100, 300, 350, 500, 1000, 10000, -100, -300, -350, -500, -1000, -10000
    };
}
