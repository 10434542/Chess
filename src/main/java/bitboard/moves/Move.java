package bitboard.moves;

import static bitboard.utils.BitBoardUtils.*;

public class Move {

    private final int sourceSquare;
    private final int targetSquare;
    private final int pieceType;
    private final int promoted;
    private final int capture;
    private final int aDouble;
    private final int enPassant;
    private final int castling;
    private final int encodedMove;

    public Move(int sourceSquare, int targetSquare, int piece, int promoted, int capture, int aDouble, int enPassant, int castling) {
        this.sourceSquare = sourceSquare;
        this.targetSquare = targetSquare;
        this.pieceType = piece;
        this.promoted = promoted;
        this.capture = capture;
        this.aDouble = aDouble;
        this.enPassant = enPassant;
        this.castling = castling;
        this.encodedMove = encodeMove();

    }

    private Move(MoveBuilder moveBuilder) {
        this.sourceSquare = moveBuilder.sourceSquare;
        this.targetSquare = moveBuilder.targetSquare;
        this.pieceType = moveBuilder.pieceType;
        this.promoted = moveBuilder.promoted;
        this.capture = moveBuilder.capture;
        this.aDouble = moveBuilder.aDouble;
        this.enPassant = moveBuilder.enPassant;
        this.castling = moveBuilder.castling;
        this.encodedMove = moveBuilder.encodedMove;
    }

    private int encodeMove() {
        return (sourceSquare | (targetSquare << 6) | (pieceType << 12) |
                (promoted << 16) | (capture << 20) | (aDouble << 21) |
                (enPassant << 22) | (castling << 23)) & ~(Integer.MIN_VALUE >> 7); // int = 32 bit, mask remaining 8 bits
    }

    public int getSourceSquare() {
        return (this.encodedMove & 0x3f);
    }

    public int getTargetSquare() {
        return ((this.encodedMove & 0xfc0) >>> 6);
    }

    public int getPieceType() {
        return ((this.encodedMove & 0xf000) >>> 12);
    }

    public int getPromoted() {
        return ((this.encodedMove & 0xf0000) >>> 16);
    }

    public int getCapture() {
        return (this.encodedMove & 0x100000);
    }

    public int getDouble() {
        return (this.encodedMove & 0x200000);
    }

    public int getEnPassant() {
        return (this.encodedMove & 0x400000);
    }

    public int getCastling() {
        return (this.encodedMove & 0x800000);
    }

    @Override
    public String toString() {
        return getAllSquareStrings().get(getSourceSquare())
                + getAllSquareStrings().get(getTargetSquare())
                + getUnicodePieces().get(getPieceType())
                + (getPromoted() == 0 ? "": getUnicodePieces().get(getPromoted()))+ ""
                + (getCapture() == 0 ? "0":"1") + " "
                + (getDouble() == 0 ? "0":"1") + " "
                + (getEnPassant() == 0 ? "0":"1") + " "
                + (getCastling() == 0 ? "0":"1");
    }

    public static class MoveBuilder {
        private final int sourceSquare;
        private final int targetSquare;
        private int pieceType;
        private int promoted;
        private int capture;
        private int aDouble;
        private int enPassant;
        private int castling;
        private int encodedMove;

        public MoveBuilder(final int sourceSquare, final int targetSquare) {
            this.sourceSquare = sourceSquare;
            this.targetSquare = targetSquare;
        }

        public MoveBuilder pieceType(int pieceType) {
            this.pieceType = pieceType;
            return this;
        }

        public MoveBuilder promoted(int promoted) {
            this.promoted = promoted;
            return this;
        }

        public MoveBuilder capture(int capture) {
            this.capture = capture;
            return this;
        }

        public MoveBuilder setDouble(int aDouble) {
            this.aDouble = aDouble;
            return this;
        }

        public MoveBuilder enPassant(int enPassant) {
            this.enPassant = enPassant;
            return this;
        }

        public MoveBuilder castling(int castling) {
            this.castling = castling;
            return this;
        }

        public MoveBuilder encodeMove() {
            this.encodedMove = (sourceSquare | (targetSquare << 6) | (pieceType << 12) |
                    (promoted << 16) | (capture << 20) | (aDouble << 21) |
                    (enPassant << 22) | (castling << 23)) & ~(Integer.MIN_VALUE >> 7);
            return this;
        }

        public Move build() {
            return new Move(this);
        }
    }
}
