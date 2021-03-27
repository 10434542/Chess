package bitboard;

public class MoveGenerator {
    private final BitBoard bitBoard;
    private final PreCalculatedData data = new PreCalculatedData();

    public MoveGenerator(BitBoard bitBoard) {
        this.bitBoard = bitBoard;
    }

    public boolean isSquareAttacked(int square, int side) {
        if (side == 0 && ((data.getAllPawnAttacks()[1][square] & bitBoard.getBitBoards()[bitBoard.getWhitePawn()]) != 0L)) {
            return true;
        }
        if (side == 1 && ((data.getAllPawnAttacks()[0][square] & bitBoard.getBitBoards()[bitBoard.getBlackPawn()]) != 0L)) {
            return true;
        }
        if ((data.getBishopAttacks(square, bitBoard.getAllOccupancy()) &
                ((side == 0 ? bitBoard.getBitBoards()[bitBoard.getWhiteBishop()]:bitBoard.getBitBoards()[bitBoard.getBlackBishop()]))) != 0L) {
            return true;
        }
        if ((data.getRookAttacks(square, bitBoard.getAllOccupancy()) &
                ((side == 0 ? bitBoard.getBitBoards()[bitBoard.getWhiteRook()]:bitBoard.getBitBoards()[bitBoard.getBlackRook()]))) != 0L) {
            return true;
        }
        if ((data.getQueenAttacks(square, bitBoard.getAllOccupancy()) &
                ((side == 0 ? bitBoard.getBitBoards()[bitBoard.getWhiteQueen()]:bitBoard.getBitBoards()[bitBoard.getBlackQueen()]))) != 0L) {
            return true;
        }
        return (data.getKingAttacks()[square] & ((side == 0 ? bitBoard.getBitBoards()[bitBoard.getWhiteKing()] : bitBoard.getBitBoards()[bitBoard.getBlackKing()]))) != 0L;
    }

}
