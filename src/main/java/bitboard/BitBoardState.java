package bitboard;


import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public class BitBoardState {
    private final long[] bitBoards;
    private final long[] occupancies;
    private final long whiteOccupancy;
    private final long blackOccupancy;
    private final long allOccupancy; // read allPieces
    private final int allCastlingRights;
    private final int[] turnCounter;
    private final SideToMove currentSide;
    private final int enPassantSquare;

    public BitBoardState(final long[] bitBoards, final long[] occupancies, final int allCastlingRights,
                         final int[] turnCounter, final SideToMove currentSide, final int enPassantSquare) {
        this.bitBoards = bitBoards;
        this.occupancies = occupancies;
        this.whiteOccupancy = occupancies[0];
        this.blackOccupancy = occupancies[1];
        this.allOccupancy = occupancies[2];
        this.allCastlingRights = allCastlingRights;
        this.currentSide = currentSide;
        this.turnCounter = turnCounter;
        this.enPassantSquare = enPassantSquare;
    }

    private BitBoardState(BitBoardStateBuilder bitBoardStateBuilder) {
        this.bitBoards = bitBoardStateBuilder.bitBoards;
        this.occupancies = bitBoardStateBuilder.occupancies;
        this.whiteOccupancy = bitBoardStateBuilder.whiteOccupancy;
        this.blackOccupancy = bitBoardStateBuilder.blackOccupancy;
        this.allOccupancy = bitBoardStateBuilder.allOccupancy;
        this.allCastlingRights = bitBoardStateBuilder.allCastlingRights;
        this.currentSide = bitBoardStateBuilder.sideToMove;
        this.turnCounter = bitBoardStateBuilder.turnCounter;
        this.enPassantSquare = bitBoardStateBuilder.enPassantSquare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BitBoardState)) return false;
        BitBoardState that = (BitBoardState) o;
        return whiteOccupancy == that.whiteOccupancy &&
                blackOccupancy == that.blackOccupancy &&
                allOccupancy == that.allOccupancy &&
                allCastlingRights == that.allCastlingRights &&
                enPassantSquare == that.enPassantSquare &&
                Arrays.equals(bitBoards, that.bitBoards) &&
                Arrays.equals(occupancies, that.occupancies) &&
                Arrays.equals(turnCounter, that.turnCounter) &&
                currentSide == that.currentSide;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(whiteOccupancy, blackOccupancy, allOccupancy, allCastlingRights, currentSide, enPassantSquare);
        result = 31 * result + Arrays.hashCode(bitBoards);
        result = 31 * result + Arrays.hashCode(occupancies);
        result = 31 * result + Arrays.hashCode(turnCounter);
        return result;
    }
    public static class BitBoardStateBuilder {

        private long[] bitBoards;
        private long[] occupancies;
        private long whiteOccupancy;
        private long blackOccupancy;
        private long allOccupancy; // read allPieces
        private int allCastlingRights;
        private int[] turnCounter;
        private SideToMove sideToMove;
        private int enPassantSquare;

        public BitBoardStateBuilder(long[] bitBoards) {
            this.bitBoards = bitBoards;
        }

        public BitBoardStateBuilder() {

        }

        public BitBoardStateBuilder bitBoards(long[] bitBoards) {
            this.bitBoards = bitBoards;
            return this;
        }

        public BitBoardStateBuilder occupancies(long[] occupancies) {
            this.occupancies = occupancies;
            this.whiteOccupancy = occupancies[0];
            this.blackOccupancy = occupancies[1];
            this.allOccupancy = occupancies[2];
            return this;
        }

        public BitBoardStateBuilder whiteOccupancy(long whiteOccupancy) {
            this.whiteOccupancy = whiteOccupancy;
            return this;
        }

        public BitBoardStateBuilder blackOccupancy(long blackOccupancy) {
            this.blackOccupancy = blackOccupancy;
            return this;
        }

        public BitBoardStateBuilder allOccupancy(long allOccupancy) {
            this.allOccupancy = allOccupancy;
            return this;
        }

        public BitBoardStateBuilder allCastlingRights(int allCastlingRights) {
            this.allCastlingRights = allCastlingRights;
            return this;
        }

        public BitBoardStateBuilder turnCounter(int[] turnCounter) {
            this.turnCounter = turnCounter;
            return this;
        }

        public BitBoardStateBuilder sideToMove(SideToMove sideToMove) {
            this.sideToMove = sideToMove;
            return this;
        }

        public BitBoardStateBuilder enPassantSquare(int enPassantSquare) {
            this.enPassantSquare = enPassantSquare;
            return this;
        }

        public BitBoardState build() {
            return new BitBoardState(this);
        }
    }


}
