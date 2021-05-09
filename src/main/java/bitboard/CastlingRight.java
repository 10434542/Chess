package bitboard;

import lombok.Getter;

public enum CastlingRight {

    /*
        0001 white king castling rights
        0010 white queen castling rights
        0100 black king castling rights
        1000 black queen castling rights
     */
    WHITE_KING_SIDE(1),
    WHITE_QUEEN_SIDE(2),
    BLACK_KING_SIDE(4),
    BLACK_QUEEN_SIDE(8);

    private final @Getter int type;
    CastlingRight(int type) {
        this.type = type;
    }

}
