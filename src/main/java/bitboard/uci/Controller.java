package bitboard.uci;

import bitboard.engine.BitBoard;
import bitboard.moves.Move;
import org.jetbrains.annotations.Nullable;

import static bitboard.utils.BitBoardUtils.*;
import static bitboard.constants.Piece.*;

import java.util.*;
import java.util.concurrent.*;

import static bitboard.engine.BitBoardState.isValidFen;

public class Controller {
    private final BitBoard board;

    public Controller(BitBoard board) {
        this.board = board;
    }

    // parses the position command and everything that it allows as arguments
    private void parsePosition(String command) {
        if (command.length() == 0) {
            return;
        }
        // position [fen <fenstring> | startpos ]  moves <move1> .... <movei>
        // so either position fen fenstring + moves
        // or position startpost + moves
        List<String> elements = List.of(command.trim().split(" "));
        if (elements.get(0).equalsIgnoreCase("startpos")) {
            this.board.setStateFromFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            if (elements.get(1).equalsIgnoreCase("moves") && elements.size() > 2) {
                List<String> movesToParse = elements.subList(2, elements.size());
                movesToParse.stream().map(this::parseMove).filter(Objects::nonNull).forEach(board::makeMove);
                System.out.println("processing moves");
            }
        }
        else if (elements.get(0).equalsIgnoreCase("fen")) {
            // if the list of elements is larger than 2 and contains a fenstring
            if (elements.size()>7) {
                String fen = String.join(" ", elements.subList(1, 7));
                if (isValidFen(fen)) {
                    board.setStateFromFenString(fen);
                }
                else {
                    board.setStateFromFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                }
            }
            else {
                board.setStateFromFenString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            }
            if (elements.get(7).equalsIgnoreCase("moves") && elements.size() > 8) {
                List<String> movesToParse = elements.subList(8, elements.size());
                movesToParse.stream().map(this::parseMove).filter(Objects::nonNull).forEach(board::makeMove);
                System.out.println("processing moves");
            }
        }
        System.out.println(this.board.toBoardString()+"\n"+board.getFenString());
    }

    private Thread parseGo(String command) {
        if (command.isEmpty()) {
            return null;
        }
        List<String> elements = List.of(command.split(" "));
        return null;
    }

    private Move parseMove(String move) {
        String source = move.substring(0,2).toUpperCase();
        String target = move.substring(2,4).toUpperCase();
        String promotionIndicator = move.length() == 5? move.substring(4): ";";

        if (getAllSquaresToIndices().containsKey(source) && getAllSquaresToIndices().containsKey(target)) {
            int sourceSquare = getAllSquaresToIndices().get(source);
            int targetSquare = getAllSquaresToIndices().get(target);
            List<Move> allowedMoves = board.getMoves();
            for (Move allowedMove : allowedMoves) {
                Move allowedMove1 = getMove(promotionIndicator, sourceSquare, targetSquare, allowedMove);
                if (allowedMove1 != null) return allowedMove1;
            }
        }
        return null;
    }

    @Nullable
    private Move getMove(String promotionIndicator, int sourceSquare, int targetSquare, Move allowedMove) {
        if (allowedMove.getSourceSquare() == sourceSquare && allowedMove.getTargetSquare() == targetSquare) {
            // found a move, check for promotion
            if (!promotionIndicator.equals(";")) {
                if (promotionIndicator.equalsIgnoreCase("n") && (allowedMove.getPromoted() == WHITE_KNIGHT || allowedMove.getPromoted() == BLACK_KNIGHT)) {
                    return allowedMove;
                }
                if (promotionIndicator.equalsIgnoreCase("q") && (allowedMove.getPromoted() == WHITE_QUEEN || allowedMove.getPromoted() == BLACK_QUEEN)) {
                    return allowedMove;
                }
                if (promotionIndicator.equalsIgnoreCase("b") && (allowedMove.getPromoted() == WHITE_BISHOP || allowedMove.getPromoted() == BLACK_BISHOP)) {
                    return allowedMove;
                }
                if (promotionIndicator.equalsIgnoreCase("r") && (allowedMove.getPromoted() == WHITE_ROOK || allowedMove.getPromoted() == BLACK_ROOK)) {
                    return allowedMove;
                }
            }
            else {
                return allowedMove;
            }
        }
        return null;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter a command");
        String currentCommand = scanner.nextLine();
        if (currentCommand.length() == 0) {
            return;
        }
        while(!currentCommand.equals("quit")) {
            if (currentCommand.startsWith("position")) {
                System.out.println("parsing position");
                parsePosition(currentCommand.substring(8));
            }
            if (currentCommand.startsWith("go")) {
                System.out.println("parsing go");
                parsePosition(currentCommand.substring(2));
            }
            if (currentCommand.equals("ucinewgame")) {
                parsePosition("position startposition");
            }
            if (currentCommand.equals("uci")) {
                System.out.println("id name JBitEngine"+"\n"+"id name Klootviool"+"\n"+"uciok");
            }
            if (currentCommand.equals(""))
            System.out.println("enter a command");
            currentCommand = scanner.nextLine();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        //         this is for future project, hyper threading
//        Controller controller = new Controller(new BitBoard());
////        controller.start();
////        ExecutorService serviceTwo = CustomFutureReturningExecutor<>(1);
//        ExecutorService service = Executors.newFixedThreadPool(10);
//        Future<?> task =
//                service.submit(
//                        () -> new LegalMoveGenerator().perftStarter(5, fenStringToBitBoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKKNR w KQkq - 0 1"))
//                );
//
//        Thread.sleep(1000);
//        System.out.println(Thread.currentThread().getId());
//        task.cancel(true);
//        System.out.println(Thread.currentThread().getId());
//        service.shutdown();
//        System.out.println(Thread.currentThread().getId());
        int position = 0;
        byte index = (byte)(((byte)(position + 56)) - (byte)((byte)(position / 8) * 16));
    }
}
