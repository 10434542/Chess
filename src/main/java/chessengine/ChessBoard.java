package chessengine;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;

public class ChessBoard {
    private final List<String> fileNames = List.of("A", "B", "C", "D", "E", "F", "G", "H");
    private Map<String, Map<Integer, Square>> allFiles;
    private Piece lastMovedPiece;
    private Set<Square> blackAttackingSquares = new HashSet<>();
    private Set<Square> whiteAttackingSquares = new HashSet<>();
    private Map<Square, Piece> blackPieceSet = new HashMap<>();
    private Map<Square, Piece> whitePieceSet = new HashMap<>();
    private MutablePair<Square, King> blackKing = new MutablePair<>();
    private MutablePair<Square, King> whiteKing = new MutablePair<>();

    public ChessBoard(List<ImmutablePair<String, Piece>> squaresAndPieces) {
        assembleBoard();
        addPiecesFromPairs(squaresAndPieces);

    }
    public ChessBoard() {
        assembleBoard();
    }

    private void assembleBoard() {
        this.allFiles = new HashMap<>();
        for (int x = 1; x < fileNames.size()+1; x++) {
            allFiles.put(fileNames.get(x-1), new HashMap<>());
            for (int y = 1; y < 9; y++) {
                Square square = new Square(x,y);
                allFiles.get(fileNames.get(x-1)).put(y, square);
            }
        }
    }

    public ChessBoard addAllPieces() {
        this.allFiles.keySet().stream()
                .map(i -> this.getSquareAt(i, 2))
                .collect(Collectors.toList())
                .forEach(x -> x.setCurrentPiece(new Pawn(PlayerColor.WHITE)));
        this.allFiles.keySet().stream()
                .map(i -> this.getSquareAt(i, 7))
                .collect(Collectors.toList())
                .forEach(x -> x.setCurrentPiece(new Pawn(PlayerColor.BLACK)));

        this.allFiles.get("A").get(1).setCurrentPiece(new Rook(PlayerColor.WHITE));
        this.allFiles.get("B").get(1).setCurrentPiece(new Knight(PlayerColor.WHITE));
        this.allFiles.get("C").get(1).setCurrentPiece(new Bishop(PlayerColor.WHITE));
        this.allFiles.get("D").get(1).setCurrentPiece(new Queen(PlayerColor.WHITE));
        this.allFiles.get("E").get(1).setCurrentPiece(new King(PlayerColor.WHITE));
        this.allFiles.get("F").get(1).setCurrentPiece(new Bishop(PlayerColor.WHITE));
        this.allFiles.get("G").get(1).setCurrentPiece(new Knight(PlayerColor.WHITE));
        this.allFiles.get("H").get(1).setCurrentPiece(new Rook(PlayerColor.WHITE));

        this.allFiles.get("A").get(8).setCurrentPiece(new Rook(PlayerColor.BLACK));
        this.allFiles.get("B").get(8).setCurrentPiece(new Knight(PlayerColor.BLACK));
        this.allFiles.get("C").get(8).setCurrentPiece(new Bishop(PlayerColor.BLACK));
        this.allFiles.get("D").get(8).setCurrentPiece(new Queen(PlayerColor.BLACK));
        this.allFiles.get("E").get(8).setCurrentPiece(new King(PlayerColor.BLACK));
        this.allFiles.get("F").get(8).setCurrentPiece(new Bishop(PlayerColor.BLACK));
        this.allFiles.get("G").get(8).setCurrentPiece(new Knight(PlayerColor.BLACK));
        this.allFiles.get("H").get(8).setCurrentPiece(new Rook(PlayerColor.BLACK));
        // compute attacking squares
        setPieceSets();
        this.blackAttackingSquares = setAttackingSquares(PlayerColor.BLACK);
        this.whiteAttackingSquares = setAttackingSquares(PlayerColor.WHITE);
        return this;
    }

    public ChessBoard addAPiece(ImmutablePair<String, Piece> squareAndPiece) {
        Square toPlacePieceAt = this.getSquareAt(squareAndPiece.getLeft());
        toPlacePieceAt.setCurrentPiece(squareAndPiece.getRight());
        return this;
    }

    @NotNull
    public ChessBoard addPiecesFromPairs(List<ImmutablePair<String, Piece>> piecesToAdd) {
        for (ImmutablePair<String, Piece> pair : piecesToAdd) {
            Square currentSquare = this.getSquareAt(pair.getLeft());
            currentSquare.setCurrentPiece(pair.getRight());
            if (pair.getRight().getColor().equals(PlayerColor.WHITE)) {
                this.whitePieceSet.put(this.getSquareAt(pair.getLeft()), pair.getRight());
                if (pair.getRight() instanceof King) {
                    whiteKing.setRight((King) pair.getRight());
                    whiteKing.setLeft(currentSquare);
                }
            }
            else {
                this.blackPieceSet.put(this.getSquareAt(pair.getLeft()), pair.getRight());
                if (pair.getRight() instanceof King) {
                    blackKing.setRight((King) pair.getRight());
                    blackKing.setLeft(currentSquare);
                }
            }
        }
//        setPieceSets(); //
        this.blackAttackingSquares = setAttackingSquares(PlayerColor.BLACK);
        this.whiteAttackingSquares = setAttackingSquares(PlayerColor.WHITE);
        return this;
    }

    @NotNull
    public Square getSquareAt(String a, int i) {
        return allFiles.get(a).get(i);
    }

    public Square getSquareAt(String squareName) {
        String file = squareName.substring(0,1);
        Integer rank = Integer.parseInt(squareName.substring(1));
        return this.allFiles.get(file).get(rank);
    }

    public Square getSquareAt(int x, int y) {
        return this.allFiles.get(fileNames.get(x-1)).get(y);
    }

    @NotNull
    public Piece removePiece(String squareName) {
        Square square = this.getSquareAt(squareName);
        Piece piece = square.getCurrentPiece();
        square.removePiece();
        return piece;
    }

    private Set<Square> setAttackingSquares(PlayerColor color) {
        Map<Square, Piece> currentMap = color.equals(PlayerColor.BLACK)? blackPieceSet: whitePieceSet;
        Set<Square> attackingSquares = new HashSet<>();
        for (Square currentSquare: currentMap.keySet()) {
            attackingSquares.addAll(currentSquare.getCurrentPiece().getPossibleMoves(this, currentSquare.getPositionX(), currentSquare.getPositionY()));
        }
        return attackingSquares;
    }

    public Set<Square> getAttackingSquares(PlayerColor color) {
        return color.equals(PlayerColor.BLACK)?  this.blackAttackingSquares: this.whiteAttackingSquares;
    }

    public Set<Square> getAttackingSquaresOpponent(PlayerColor color) {
        return color.equals(PlayerColor.BLACK)? this.whiteAttackingSquares: this.blackAttackingSquares;
    }

    private void setPieceSets() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                Square currentSquare = getSquareAt(i, j);
                if (currentSquare.getCurrentPiece() != null) {
                    Piece currentPiece = currentSquare.getCurrentPiece();
                    if (currentPiece.getColor().equals(PlayerColor.BLACK)) {
                        blackPieceSet.put(currentSquare, currentPiece);
                    }
                    else if (currentPiece.getColor().equals(PlayerColor.WHITE)) {
                        whitePieceSet.put(currentSquare, currentPiece);
                    }
                    if (currentPiece instanceof King) {
                        if (currentPiece.getColor().equals(PlayerColor.BLACK)) {
                            this.blackKing.right = (King) currentPiece;
                            this.blackKing.setLeft(currentSquare);
                        }
                        else {
                            this.whiteKing.setRight((King) currentPiece);
                            this.whiteKing.setLeft(currentSquare);
                        }
                    }
                }
            }
        }
    }

    public Move move(String origin, String destination) throws IllegalMoveException {
        // TODO: refactor this shit
        Piece piece = getSquareAt(origin).getCurrentPiece();
        Square currentSquare = getSquareAt(origin);
        Square destinationSquare = getSquareAt(destination);
        Map<Square, Piece> currentPieceSet = piece.getColor().equals(PlayerColor.BLACK)? blackPieceSet: whitePieceSet;
        Map<Square, Piece> opponentsPieceSet = piece.getColor().equals(PlayerColor.BLACK)? whitePieceSet: blackPieceSet;
        PlayerColor colorOpponent = piece.getColor().equals(PlayerColor.BLACK)? PlayerColor.WHITE: PlayerColor.BLACK;
        Set<Square> ownAttackingSquares = getAttackingSquares(piece.getColor());
        Set<Square> opponentsAttackingSquares = getAttackingSquaresOpponent(piece.getColor());

        // If black moved, black can't move again. Likewise for white
        if (lastMovedPiece != null && lastMovedPiece.getColor().equals(piece.getColor())) {
            String errorMessage = "not " + piece.getColor() + "'s "+ "turn to make a move";
            throw new IllegalMoveException(errorMessage);
        }

        List<Square> legalMoves = piece.getPossibleMoves(this, currentSquare.getPositionX(), currentSquare.getPositionY());
        MutablePair<Square, King> ownKing = piece.getColor().equals(PlayerColor.BLACK)? blackKing: whiteKing;
        MutablePair<Square, King> otherKing = piece.getColor().equals(PlayerColor.BLACK)? whiteKing: blackKing;

        // en passant
        if (piece instanceof Pawn && Math.abs(currentSquare.getPositionY() - destinationSquare.getPositionY()) == 2) {
            ((Pawn) piece).setEnPassantCapture(true);
        }

        if (legalMoves.contains(destinationSquare)) {
            // first check that you cannot check yourself!
            // update pieceSet THEN update attackingSquares
            Piece pieceOnDestination = destinationSquare.getCurrentPiece();
            destinationSquare.setCurrentPiece(piece);
            currentSquare.removePiece();
            currentPieceSet.remove(currentSquare);
            currentPieceSet.put(destinationSquare, piece);



            // update own attacking squares
            ownAttackingSquares = this.setAttackingSquares(piece.getColor());

            // check for self check!
            opponentsAttackingSquares = this.setAttackingSquares(colorOpponent);

            if (opponentsAttackingSquares.contains(ownKing.getLeft())) {
                // undo move and throw illegalMoveException
                if (pieceOnDestination != null) {
                    destinationSquare.setCurrentPiece(pieceOnDestination);
                }
                currentSquare.setCurrentPiece(piece);
                currentPieceSet.put(currentSquare, piece);
                currentPieceSet.remove(destinationSquare, piece);
                throw new IllegalMoveException("Piece is pinned mate");
            }

            // check for check on opponents king
            if (ownAttackingSquares.contains(otherKing.getLeft())) {
                otherKing.getRight().setChecked(true);
            }


            if (lastMovedPiece instanceof Pawn && ((Pawn) lastMovedPiece).getEnPassantCapture()) {
                ((Pawn) lastMovedPiece).setEnPassantCapture(false); // unset eligibility for en passant capture to prevent weird captures
            }

            lastMovedPiece = piece;
            return new Move(origin, destination);
        }

        else if (piece instanceof Pawn && lastMovedPiece instanceof Pawn) {
            int offset = piece.getColor().equals(PlayerColor.BLACK) ? 1: -1;
            if (currentSquare.getPositionY() == (piece.getColor().equals(PlayerColor.BLACK)? 4 : 5)) { // we know we can cast to pawn else legalMoves would not contain this move.
                Square square = getSquareAt(destinationSquare.getPositionX(), destinationSquare.getPositionY() + offset);
                Piece pawnMightBeCaptured = square.getCurrentPiece();
                if (pawnMightBeCaptured.getColor() != piece.getColor() &&
                        pawnMightBeCaptured instanceof Pawn &&
                        ((Pawn) pawnMightBeCaptured).getEnPassantCapture() &&
                        !destinationSquare.isContested()) { // maybe redundant check since pawns can only be eligible for en passant capture when last move was a pawn double move

                    // check for self check!
                    square.removePiece();
                    destinationSquare.setCurrentPiece(piece);
                    currentPieceSet.remove(currentSquare);
                    currentPieceSet.put(destinationSquare, piece);
                    opponentsPieceSet.remove(square);
                    opponentsAttackingSquares = this.setAttackingSquares(colorOpponent);
                    if (opponentsAttackingSquares.contains(ownKing.getLeft())) {
                        System.out.println("got here");
                        square.setCurrentPiece(pawnMightBeCaptured);
                        currentSquare.setCurrentPiece(piece);
                        currentPieceSet.put(currentSquare, piece);
                        currentPieceSet.remove(destinationSquare, piece);
                        opponentsPieceSet.put(square, pawnMightBeCaptured);
                        destinationSquare.removePiece();
                        throw new IllegalMoveException("Check yourself before you check yourself");
                    }
                }
            }

            lastMovedPiece = piece;
            currentSquare.removePiece();
            // add check on enemy king check.
            return new Move(origin, destination);
        }

        else {

            String errorMessage = piece.getClass()
                    .toString() +
                    " " +
                    origin +
                    " " +
                    destination;
            throw new IllegalMoveException(errorMessage);
        }
    }
}
