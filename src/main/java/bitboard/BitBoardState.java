package bitboard;


import lombok.Getter;

import java.util.*;

import static bitboard.BitBoardUtils.*;

@Getter
public class BitBoardState {
    private final long[] bitBoards;
    private final long[] occupancies;
    private final long whiteOccupancy;
    private final long blackOccupancy;
    private final long allOccupancy; // read allPieces
    private final int allCastlingRights;
    private final int halfMoveCounter;
    private final int fullMoveCounter;
    private final Side SideThatMoved;
    private final int enPassantSquare;

    public BitBoardState(final long[] bitBoards, final long[] occupancies, final int allCastlingRights,
                         final Side SideThatMoved, final int enPassantSquare,
                         final int halfMoveCounter, final int fullMoveCounter) {
        this.bitBoards = bitBoards;
        this.occupancies = occupancies;
        this.whiteOccupancy = occupancies[0];
        this.blackOccupancy = occupancies[1];
        this.allOccupancy = occupancies[2];
        this.allCastlingRights = allCastlingRights;
        this.SideThatMoved = SideThatMoved;
        this.halfMoveCounter = halfMoveCounter;
        this.fullMoveCounter = fullMoveCounter;
        this.enPassantSquare = enPassantSquare;
    }

    private BitBoardState(BitBoardStateBuilder bitBoardStateBuilder) {
        this.bitBoards = bitBoardStateBuilder.bitBoards;
        this.occupancies = bitBoardStateBuilder.occupancies;
        this.whiteOccupancy = bitBoardStateBuilder.whiteOccupancy;
        this.blackOccupancy = bitBoardStateBuilder.blackOccupancy;
        this.allOccupancy = bitBoardStateBuilder.allOccupancy;
        this.allCastlingRights = bitBoardStateBuilder.allCastlingRights;
        this.SideThatMoved = bitBoardStateBuilder.side;
        this.halfMoveCounter = bitBoardStateBuilder.halfMoveCounter;
        this.fullMoveCounter = bitBoardStateBuilder.fullMoveCounter;
        this.enPassantSquare = bitBoardStateBuilder.enPassantSquare;
    }
    public static BitBoardState fenStringToBitBoardState(String fen) {
        List<String> splitString = Arrays.asList(fen.split("/"));
        List<String> lastRankAndStateInfo = Arrays.asList(splitString.get(7).split(" "));
        List<String> ranks = new ArrayList<>(splitString.subList(0, 7));
        ranks.add(lastRankAndStateInfo.get(0));
        String castlingRight = lastRankAndStateInfo.get(2);
        BitBoardStateBuilder bitBoardStateBuilder = new BitBoardStateBuilder();
        bitBoardStateBuilder
                .enPassantSquare(lastRankAndStateInfo.get(3).equals("-")? -1: getAllSquaresToIndices().get(lastRankAndStateInfo.get(3)))
                .sideToMove(lastRankAndStateInfo.get(1).equals(new String(new char[]{'w'})) ? Side.WHITE: Side.BLACK)
                .halfMoveCounter(Integer.parseInt(lastRankAndStateInfo.get(4)))
                .fullMoveCounter(Integer.parseInt(lastRankAndStateInfo.get(5)));

//        Collections.reverse(ranks); // not needed
        int counter = 0;
        long tempWhiteOccupancy = 0;
        long tempBlackOccupancy = 0;
        long[] tempBitBoards = new long[12];
        int tempAllCastlingRights = 0;
        for (String rank : ranks) {
            for (char c: rank.trim().toCharArray()) {
                if (Character.isLetter(c)) {
                    tempBitBoards[getCharsToIndices().get(c)] = setBit(tempBitBoards[getCharsToIndices().get(c)], counter);
                    if (getCharsToIndices().get(c) < 6) {
                        tempWhiteOccupancy |= tempBitBoards[getCharsToIndices().get(c)];
                    }
                    else {
                        tempBlackOccupancy |= tempBitBoards[getCharsToIndices().get(c)];
                    }
                    counter++;
                }
                else {
                    counter += Character.getNumericValue(c);
                }
            }
        }

        bitBoardStateBuilder
                .bitBoards(tempBitBoards) // forgot to put thish here lol
                .blackOccupancy(tempBlackOccupancy)
                .whiteOccupancy(tempWhiteOccupancy)
                .allOccupancy(tempWhiteOccupancy | tempBlackOccupancy);

        for (char c: castlingRight.trim().toCharArray()) {
            if (c == 'K') {
                tempAllCastlingRights |= getWk().getType();
            } else if (c == 'Q') {
                tempAllCastlingRights |= getWq().getType();
            } else if (c == 'k') {
                tempAllCastlingRights |= getBk().getType();
            } else if (c == 'q') {
                tempAllCastlingRights |= getBq().getType();
            }
        }
        bitBoardStateBuilder.allCastlingRights(tempAllCastlingRights);
        return bitBoardStateBuilder.build();
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
                SideThatMoved == that.SideThatMoved;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(whiteOccupancy, blackOccupancy, allOccupancy, allCastlingRights, SideThatMoved, enPassantSquare);
        result = 31 * result + Arrays.hashCode(bitBoards);
        result = 31 * result + Arrays.hashCode(occupancies);
        return result;
    }
    public static class BitBoardStateBuilder {

        private long[] bitBoards;
        private long[] occupancies;
        private long whiteOccupancy;
        private long blackOccupancy;
        private long allOccupancy; // read allPieces
        private int allCastlingRights;
        private int halfMoveCounter;
        private int fullMoveCounter;
        private Side side;
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

        public BitBoardStateBuilder halfMoveCounter(int halfMoveCounter) {
            this.halfMoveCounter = halfMoveCounter;
            return this;
        }

        public BitBoardStateBuilder fullMoveCounter(int fullMoveCounter) {
            this.fullMoveCounter = fullMoveCounter;
            return this;
        }

        public BitBoardStateBuilder sideToMove(Side side) {
            this.side = side;
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
