package bitboard;

public class MoveGenerator {
    private BitBoard bitBoard;
    private final PreCalculatedData data = new PreCalculatedData();

    public MoveGenerator(BitBoard bitBoard) {
        this.bitBoard = bitBoard;
    }

}
