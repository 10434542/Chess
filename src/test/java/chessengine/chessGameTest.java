package chessengine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class chessGameTest {

    @Test
    void BoardExistTest() {
        ChessBoard chessBoard = new ChessBoard().addAllPieces();
        ChessGame chessGame = new ChessGame(chessBoard);
        Assertions.assertNotNull(chessBoard);
    }

    @Test
    void getFirstPlayerColor() {
        ChessGame chessGame = new ChessGame(new ChessBoard().addAllPieces());
        PlayerColor white = chessGame.getCurrentPlayerColor();
    }

    @Test
    void getNextPlayer() {
        ChessBoard chessBoard = new ChessBoard().addAllPieces();
        ChessGame chessGame = new ChessGame(chessBoard);
        chessGame.addMove("A2", "A3");
        PlayerColor black = chessGame.getCurrentPlayerColor();
        Assertions.assertEquals(PlayerColor.BLACK, black, "After white has made a move, it should be black's turn");
    }

    @Test
    void updateMoveHistory() {
        ChessBoard chessBoard = new ChessBoard().addAllPieces();
        ChessGame chessGame = new ChessGame(chessBoard);
        chessGame.addMove("A2", "A3");
        List<Move> lastMove = chessGame.getMoveHistory();
        List<String> expected = Arrays.asList("A1", "A2");
        Move a1ToA2 = new Move("A1", "A2");
        Assertions.assertEquals(expected, lastMove, "When a move is made, the move history should be updated");
    }



//    @Test not like this
//    void firstRankExist() {
//        ChessBoard someBoard = new ChessBoard();
//        List<Square> firstFile = someBoard.getFile("A");
//        Assertions.assertEquals(8, firstFile.size());
//
//    }
}
