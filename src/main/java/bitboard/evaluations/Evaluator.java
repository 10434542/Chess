package bitboard.evaluations;

import bitboard.engine.BitBoardState;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static bitboard.utils.BitBoardUtils.popBit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Evaluator {

    public static int Evaluate(BitBoardState state) {
        int piece = 0;
        int square = 0;
        int score = 0;
        long[] bitBoards = state.getBitBoards();
        for (int currentPiece = 0; currentPiece < 12; currentPiece++) {
//            while (bitBoard != 0L) {
//
//                bitBoard = popBit(bitBoard, square);
//            }
            continue;
        }
        return score;
    }
}
