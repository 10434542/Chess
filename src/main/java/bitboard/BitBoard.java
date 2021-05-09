package bitboard;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static bitboard.BitBoardUtils.*;

/* TODO: it is a work in progress but I want the bitBoard to be a functional class in the sense that state is not
    tracked anywhere in the class except in the historyList of states which are all final states to simplify make and unmake move functions
    and even making a legalMove generator
*/
public class BitBoard implements IBoard{

    private @Getter List<BitBoardState> history;
    private @Getter BitBoardState currentState;

    public BitBoard() {
       this.currentState = parseFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ");
    }

    public BitBoard(String fenString) {
        this.currentState = parseFenString(fenString);
    }

    public BitBoard(BitBoardState bitBoardState) {
        this.currentState = bitBoardState;
    }

    public String toBoardString() {
        List<Long> tempBitBoards = new ArrayList<>();
        List<String> allSquaresEncoded = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            allSquaresEncoded.add(".");
        }
        for(long l: this.currentState.getBitBoards()) {
            tempBitBoards.add(l);
        }
        for (int i = 0; i < tempBitBoards.size(); i++) {
            List<Integer> currentIndices = getSquareIndices(tempBitBoards.get(i));
            for (Integer currentIndex : currentIndices) {
                allSquaresEncoded.set(currentIndex, getUnicodePieces().get(i));
            }
        }

        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < allSquaresEncoded.size(); i++) {
            if (i != 0 && i%8==0) {
                boardString.append("\n");
            }
            boardString.append(allSquaresEncoded.get(i));

        }
        // reverse string somewhere here
        return boardString.toString();
    }

    //<editor-fold desc="FEN string parser and getter">
    public BitBoardState parseFenString(String fen) {
        List<String> splitString = Arrays.asList(fen.split("/"));
        List<String> lastRankAndStateInfo = Arrays.asList(splitString.get(7).split(" "));
        List<String> ranks = new ArrayList<>(splitString.subList(0, 7));
        ranks.add(lastRankAndStateInfo.get(0));
        String castlingRight = lastRankAndStateInfo.get(2);
        BitBoardState.BitBoardStateBuilder bitBoardStateBuilder = new BitBoardState.BitBoardStateBuilder(new long[12]);
        bitBoardStateBuilder
                .enPassantSquare(lastRankAndStateInfo.get(3).equals("-")? -1: getAllSquaresToIndices().get(lastRankAndStateInfo.get(3)))
                .sideToMove(lastRankAndStateInfo.get(1).equals(new String(new char[]{'w'})) ? SideToMove.WHITE: SideToMove.BLACK)
                .turnCounter(new int[]{Integer.parseInt(lastRankAndStateInfo.get(4)),Integer.parseInt(lastRankAndStateInfo.get(5))});

        Collections.reverse(ranks); // else board is flipped
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

    public String toFenString() {
        String allOccupancyString = toBitBoardRepresentation(currentState.getAllOccupancy());
        return "0";
    }
    //</editor-fold>

    @Override
    public BitBoardState makeMove() {
        return null;
    }

    @Override
    public BitBoardState unMakeMove() {
        return null;
    }

    @Override
    public List<Move> getMoves() {
        return null;
    }

    public static void main(String[] args) {
        String startPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ";
        String testAttackSquares = "8/8/4r3/3B4/8/8/8/8 w - - 0-1";
        BitBoard another = new BitBoard(startPosition);

        Move anotherMoveTwo =  new Move(getAllSquaresToIndices().get("D7"), getAllSquaresToIndices().get("D8"), 0, 4, 0,0,0,0);
        System.out.println(anotherMoveTwo);
    }

    public BitBoardState getBitBoardState() {
        return null;
    }
}
