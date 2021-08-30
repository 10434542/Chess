package bitboard.ai;

import bitboard.engine.BitBoardState;
import bitboard.generators.LegalMoveGenerator;
import bitboard.moves.Move;

import java.util.List;

public class MoveEvaluator {
    private static final LegalMoveGenerator generator = new LegalMoveGenerator();

    public MoveEvaluator() {

    }

//    int negaMax( int depth ) {
//        if ( depth == 0 ) return evaluate();
//        int max = -oo;
//        for ( all moves)  {
//            score = -negaMax( depth - 1 );
//            if( score > max )
//                max = score;
//        }
//        return max;
//    }

    private int evaluateMove() {
        return 0;
    }

    public int alphaBeta(int alpha, int beta, int depthLeft, BitBoardState state) {
        if (depthLeft == 0) {
            return quiescense(alpha, beta, state);
        }
        List<Move> moves = generator.generateMoves(state);
        for (Move move : moves) {
            int score = -alphaBeta(-alpha, -beta, depthLeft - 1, generator.moveToState(move, state));
            if (score >= beta) {
                return score;
            }
            if (score > alpha) {
                alpha = score;
            }
        }
        return alpha;
    }

    public int quiescense( int alpha, int beta, BitBoardState state) {
        int stand_pat = Evaluate();
        if( stand_pat >= beta )
            return beta;
        if( alpha < stand_pat )
            alpha = stand_pat;

        until( every_capture_has_been_examined )  {
            MakeCapture();
            score = -quiesce( -beta, -alpha, state);
            TakeBackMove();

            if( score >= beta )
                return beta;
            if( score > alpha )
                alpha = score;
        }
        return alpha;
    }

    private int quiescense(int alpha, int beta) {
        return 0;
    }

    public  static int Evaluate() {
        return 0;
    }
}
