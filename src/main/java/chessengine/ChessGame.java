package chessengine;

import java.util.Arrays;
import java.util.List;

public class ChessGame {
    private final ChessBoard chessBoard;

    private Player currentPlayer;
    private final Player white;
    private final Player black;
    private List<Move> moveHistory;
    public ChessGame(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        this.white = new Player(PlayerColor.WHITE);
        this.black = new Player(PlayerColor.BLACK);
        this.currentPlayer = white;
    }

    public ChessGame(ChessGame chessGame) {
        this.chessBoard = chessGame.getChessBoard();
        this.currentPlayer = chessGame.getCurrentPlayer();
        this.white = chessGame.getWhite();
        this.black = chessGame.getBlack();
        this.moveHistory = chessGame.getMoveHistory();
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public PlayerColor getCurrentPlayerColor() {
        return this.currentPlayer.getColor();
    }

    private Player getNextPlayer() {
        return currentPlayer.equals(white) ? black: white;
    }

    public void addMove(String start, String end) {
//        this.moveHistory.add(Arrays.asList(start, end));
        // TODO: do actual move lol;
        this.currentPlayer = getNextPlayer();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getWhite() {
        return white;
    }

    public Player getBlack() {
        return black;
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }
}
