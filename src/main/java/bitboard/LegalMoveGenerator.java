package bitboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static bitboard.BitBoardUtils.*;
import static bitboard.Piece.*;

/* TODO: make this a LegalMoveGenerator!
    to be done:
    finish tests for this class (every scenario tested, high coverage).
    checks are not visible?
 */

public class LegalMoveGenerator implements MoveGenerator{

    private final PreCalculatedData calculatedData;
    public static long perftStart = 0;
    public static int captures = 0;
    public static int castle = 0;
    public static int checks = 0;
    public static int notChecks = 0;
    public static int enpas = 0;
    public static int[] squareMoveCounter = new int[64];
    public LegalMoveGenerator(PreCalculatedData calculatedData) {
        this.calculatedData = calculatedData;
    }

    public void perftDriver(final int depth, final BitBoardState state) {
        if (depth == 0) {
            perftStart++;
            return;
        }

        else {
            List<Move> moves = generateMoves(state);
//            System.out.println(moves.size());
            for (Move move: moves) {
//                if (isNotInCheck(state)) {
//                    notChecks++;
//                }
                if (move.getCapture() != 0) {
                    captures++;
                }
                if (move.getCastling() != 0) {
                    castle++;
                }
                if (move.getEnPassant() != 0) {
                    enpas++;
                }

                BitBoardState anotherState = moveToState(move, state);
                if (!isNotInCheck(anotherState)) {
                    checks++;
//                    System.out.println(new BitBoard(anotherState, this).toBoardString());
                }
//                System.out.println(move);
                squareMoveCounter[move.getSourceSquare()] += 1;
//                System.out.println(move.getSourceSquare());
//                System.out.println(new BitBoard(anotherState, new LegalMoveGenerator(this.calculatedData)).toBoardString());
//                System.out.println("in between: "+depth+" "+state.getAllOccupancy());
                perftDriver(depth - 1, anotherState);
            }
        }
    }

    public boolean isSelfChecked(BitBoardState bitBoardState) {
        if (bitBoardState.getSideToMove() == Side.WHITE) { // if the side that moved is black, check whether
            return !isSquareAttacked(bitScanForwardDeBruijn64(bitBoardState.getBitBoards()[BLACK_KING]), 0, bitBoardState);
        }
        else if (bitBoardState.getSideToMove() == Side.BLACK)
            return !isSquareAttacked(bitScanForwardDeBruijn64(bitBoardState.getBitBoards()[WHITE_KING]), 1, bitBoardState);
        return true;
    }

    
    public BitBoardState moveToState(Move move, final BitBoardState bitBoardState) { // this mutates the bitBoardState but why?
        BitBoardState.BitBoardStateBuilder stateBuilder = new BitBoardState.BitBoardStateBuilder();
        long[] tempBitBoards = bitBoardState.getBitBoards();
        tempBitBoards[move.getPieceType()] = popBit(setBit(bitBoardState.getBitBoards()[move.getPieceType()], move.getTargetSquare()), move.getSourceSquare());
        Side sideToPlay = bitBoardState.getSideToMove();

        if (move.getCapture() != 0) {

            int startIndex = bitBoardState.getSideToMove() == Side.WHITE ? WHITE_PAWN: BLACK_PAWN;
            // bitBoardState.getCurrentSide() == SideToMove.WHITE  <---> sideToPlay == SideToMove.BLACK  IS EQUIVALENT TO THE ABOVE STATEMENT
            // MAYBE REFACTOR LATER
            int endIndex = bitBoardState.getSideToMove() == Side.WHITE ? BLACK_PAWN: BLACK_KING+1;

            for (int i = startIndex; i< endIndex; i++) {
                if (getBit(bitBoardState.getBitBoards()[i], move.getTargetSquare()) != 0) {
                    tempBitBoards[move.getPieceType()] = popBit(bitBoardState.getBitBoards()[move.getPieceType()], move.getTargetSquare());
                    break;
                }
            }
        }

        if (move.getPromoted() != 0) {
            int currentPawn = sideToPlay == Side.WHITE? WHITE_PAWN : BLACK_PAWN;
            tempBitBoards[currentPawn] = popBit(bitBoardState.getBitBoards()[currentPawn], move.getTargetSquare());
            tempBitBoards[move.getPromoted()] = setBit(bitBoardState.getBitBoards()[move.getPromoted()], move.getTargetSquare());
        }

        if (move.getEnPassant() != -1 ) {
            int pawnToBeRemoved = sideToPlay == Side.WHITE? BLACK_PAWN: WHITE_PAWN;
            int offset = bitBoardState.getSideToMove() == Side.WHITE? -8: 8;
            tempBitBoards[pawnToBeRemoved] = popBit(bitBoardState.getBitBoards()[pawnToBeRemoved], move.getTargetSquare() + offset);
        }

        stateBuilder.enPassantSquare(-1);

        if (move.getDouble() != 0) {
            stateBuilder.enPassantSquare(bitBoardState.getSideToMove() == Side.WHITE ? move.getTargetSquare() - 8: move.getTargetSquare() + 8);
        }

        if (move.getCastling() != 0) {
            switch (move.getTargetSquare()) {
                case (6):
                    tempBitBoards[WHITE_ROOK] = setBit(popBit(bitBoardState.getBitBoards()[WHITE_ROOK],
                            getAllSquaresToIndices().get("H1")),
                            getAllSquaresToIndices().get("F1"));
                    break;
                case (2):
                    tempBitBoards[WHITE_ROOK] = setBit(popBit(bitBoardState.getBitBoards()[WHITE_ROOK],
                            getAllSquaresToIndices().get("A1")),
                            getAllSquaresToIndices().get("D1"));
                    break;
                case (62):
                    tempBitBoards[BLACK_ROOK] = setBit(popBit(bitBoardState.getBitBoards()[BLACK_ROOK],
                            getAllSquaresToIndices().get("H8")),
                            getAllSquaresToIndices().get("F8"));
                    break;
                case (58):
                    tempBitBoards[BLACK_ROOK] = setBit(popBit(bitBoardState.getBitBoards()[BLACK_ROOK],
                            getAllSquaresToIndices().get("A8")),
                            getAllSquaresToIndices().get("D8"));
                    break;
                default:
                    break;
            }
        }


        stateBuilder.allCastlingRights((bitBoardState.getAllCastlingRights() & getCastlingSquares()[move.getSourceSquare()])& getCastlingSquares()[move.getTargetSquare()]);

        long tempWhiteOccupancy = 0L;
        long tempBlackOccupancy = 0L; // bug here with occupancies?

        for (int i = 0; i < 12; i++) {
            if (i > 5) {
                tempBlackOccupancy |= tempBitBoards[i];
            }
            else {
                tempWhiteOccupancy |= tempBitBoards[i];
            }
        }

        if ((move.getCapture() == 0 ) || (move.getPieceType() == WHITE_PAWN) || (move.getPieceType() == BLACK_PAWN)) {
            stateBuilder.halfMoveCounter(bitBoardState.getHalfMoveCounter() + 1);
        }

        else {
            stateBuilder.halfMoveCounter(0);
        }

        stateBuilder.whiteOccupancy(tempWhiteOccupancy)
                .blackOccupancy(tempBlackOccupancy)
                .allOccupancy((tempWhiteOccupancy | tempBlackOccupancy))
                .occupancies(new long[]{tempWhiteOccupancy, tempBlackOccupancy, tempWhiteOccupancy | tempBlackOccupancy})
                .bitBoards(tempBitBoards)
                .halfMoveCounter(sideToPlay == Side.BLACK? bitBoardState.getFullMoveCounter() + 1: bitBoardState.getFullMoveCounter())
                .sideToMove(sideToPlay == Side.BLACK? Side.WHITE: Side.BLACK); // confusing since this is actually the side that played but okay.

        return stateBuilder.build();
    }

    public boolean isNotInCheck(BitBoardState state) {
        if (state.getSideToMove() == Side.BLACK) { // if the side that moved is black, check whether
            return !isSquareAttacked(bitScanForwardDeBruijn64(state.getBitBoards()[BLACK_KING]), 0, state);
        }
        else if (state.getSideToMove() == Side.WHITE)
            return !isSquareAttacked(bitScanForwardDeBruijn64(state.getBitBoards()[WHITE_KING]), 1, state);
        return true;
    }
    // square attacked by side that is passed (IS square ATTACKED BY THE SIDE THAT IS GIVEN?)
    public boolean isSquareAttacked(int square, int side, BitBoardState state) {
        if (side == 0 && ((calculatedData.getAllPawnAttacks()[side][square] & state.getBitBoards()[WHITE_PAWN]) != 0L)) {
            return true;
        }
        if (side == 1 && ((calculatedData.getAllPawnAttacks()[side][square] & state.getBitBoards()[BLACK_PAWN]) != 0L)) {
            return true;
        }
        if ((calculatedData.getBishopAttacks(square, state.getAllOccupancy()) &
                (side == 0 ? state.getBitBoards()[WHITE_BISHOP]:state.getBitBoards()[BLACK_BISHOP])) != 0L) {
            return true;
        }
        if ((calculatedData.getRookAttacks(square, state.getAllOccupancy()) &
                (side == 0 ? state.getBitBoards()[WHITE_ROOK]:state.getBitBoards()[BLACK_ROOK])) != 0L) {
            return true;
        }
        if ((calculatedData.getQueenAttacks(square, state.getAllOccupancy()) &
                (side == 0 ? state.getBitBoards()[WHITE_QUEEN]:state.getBitBoards()[BLACK_QUEEN])) != 0L) {
            return true;
        }
        return (calculatedData.getKingAttacks()[square] & (side == 0 ? state.getBitBoards()[WHITE_KING] : state.getBitBoards()[BLACK_KING])) != 0L;
    }


    public List<Move> generateMoves(BitBoardState state) {

        List<Move> kingMoves = generatePseudoLegalKingMoves(state).stream().filter(x-> this.isSelfChecked(this.moveToState(x, state))).collect(Collectors.toList());

        // First check the moves of the king, if king is in check other moves should not be calculated! // this is not working since one can also block with a piece!
//        if (!isNotInCheck(state)) {
//
//            return kingMoves;
//        }
//        else {
        List<List<Move>> trialMoveLists = List.of(
                generatePseudoLegalPawnMoves(state),
                generatePseudoLegalCastlingMoves(state),
                kingMoves,
                generatePseudoLegalBishopMoves(state),
                generatePseudoLegalRookMoves(state),
                generatePseudoLegalQueenMoves(state),
                generatePseudoLegalKnightMoves(state)
                );

        return trialMoveLists.stream()
                .flatMap(Collection::stream)
                .filter(move-> isSelfChecked(moveToState(move, new BitBoardState.BitBoardStateBuilder().of(state).build())))
                .collect(Collectors.toList());
//        }
    }

    @Deprecated
    private List<List<Move>> generatePseudoLegalMoves(BitBoardState bitBoardState) {

       return List.of(generatePseudoLegalPawnMoves(bitBoardState),
                generatePseudoLegalCastlingMoves(bitBoardState),
                generatePseudoLegalKingMoves(bitBoardState),
                generatePseudoLegalBishopMoves(bitBoardState),
                generatePseudoLegalRookMoves(bitBoardState),
                generatePseudoLegalQueenMoves(bitBoardState),
                generatePseudoLegalKnightMoves(bitBoardState));
    }

    private List<Move> generatePseudoLegalPawnMoves(final BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        Side currentSide = bitBoardState.getSideToMove();
        List<Move> pawnMoves = new ArrayList<>();
        if (currentSide == Side.WHITE) {
            // calculate white pawn moves
            currentBitBoard = bitBoardState.getBitBoards()[WHITE_PAWN];
            while (currentBitBoard != 0L) {
                originSquare = bitScanForwardDeBruijn64(currentBitBoard);
                targetSquare = originSquare + 8;
                if (targetSquare > getAllSquaresToIndices().get("A1") && getBit(bitBoardState.getAllOccupancy(), targetSquare) == 0) {
                    if (originSquare >= getAllSquaresToIndices().get("A7")
                            && originSquare <= getAllSquaresToIndices().get("H7")) {
                        // pawn promotion
                        pawnMoves.add(new Move.MoveBuilder(originSquare, targetSquare).pieceType(WHITE_PAWN).promoted(1).capture(0).setDouble(0).enPassant(0).castling(0)
                                .encodeMove().build());
                        pawnMoves.add(new Move.MoveBuilder(originSquare, targetSquare).pieceType(WHITE_PAWN).promoted(2).capture(0).setDouble(0).enPassant(0).castling(0).encodeMove().build());
                        pawnMoves.add(new Move.MoveBuilder(originSquare, targetSquare).pieceType(WHITE_PAWN).promoted(3).capture(0).setDouble(0).enPassant(0).castling(0).encodeMove().build());
                        pawnMoves.add(new Move.MoveBuilder(originSquare, targetSquare).pieceType(WHITE_PAWN).promoted(4).capture(0).setDouble(0).enPassant(0).castling(0).encodeMove().build());

                    }
                    else {
                        // single pawn push
                        pawnMoves.add(new Move(originSquare, targetSquare, WHITE_PAWN, 0,0,0,0,0));


                        // double pawn push
                        if (originSquare >= getAllSquaresToIndices().get("A2")
                                && originSquare <= getAllSquaresToIndices().get("H2")
                                && getBit(bitBoardState.getAllOccupancy(), targetSquare + 8) == 0) {
                            pawnMoves.add(new Move(originSquare, targetSquare+8, 0, 0,0,1,0,0));

                        }
                    }
                }
                currentAttacks = calculatedData.getAllPawnAttacks()[0][originSquare] & bitBoardState.getBlackOccupancy();
                while (currentAttacks != 0L) {
                    targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                    if (originSquare >= getAllSquaresToIndices().get("A7") && originSquare <= getAllSquaresToIndices().get("H7")) {
                        pawnMoves.add(new Move(originSquare, targetSquare, WHITE_PAWN, 4,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, WHITE_PAWN, 3,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, WHITE_PAWN, 2,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, WHITE_PAWN, 1,1,0,0,0));

                    }
                    else {
                        // normal capture
                        pawnMoves.add(new Move(originSquare, targetSquare, WHITE_PAWN, 0,1,0,0,0));

                    }
                    currentAttacks = popBit(currentAttacks, targetSquare);
                }
                if (bitBoardState.getEnPassantSquare() != -1) {
                    long enPassantAttacks = calculatedData.getAllPawnAttacks()[0][originSquare] & (1L << bitBoardState.getEnPassantSquare());
                    if (enPassantAttacks != 0L) {
                        int enPassantTarget = bitScanForwardDeBruijn64(enPassantAttacks);
                        pawnMoves.add(new Move(originSquare, enPassantTarget, WHITE_PAWN, 0,1,0,1,0));

                    }
                }
                currentBitBoard = popBit(currentBitBoard, originSquare);
            }
        }
        else {
            // calculate black pawn moves
            currentBitBoard = bitBoardState.getBitBoards()[BLACK_PAWN];
            while (currentBitBoard != 0L) {
                originSquare = bitScanForwardDeBruijn64(currentBitBoard);
                targetSquare = originSquare - 8;
                if (targetSquare < getAllSquaresToIndices().get("H8") && getBit(bitBoardState.getAllOccupancy(), targetSquare) == 0) {
                    if (originSquare >= getAllSquaresToIndices().get("A2")
                            && originSquare <= getAllSquaresToIndices().get("H2")) {
                        // pawn promotion
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 7,0,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 8,0,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 9,0,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 10,0,0,0,0));

                    }
                    else {
                        // single pawn push
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 0, 0, 0, 0, 0));

                        // double pawn push
                        if (originSquare >= getAllSquaresToIndices().get("A7")
                                && originSquare <= getAllSquaresToIndices().get("H7")
                                && getBit(bitBoardState.getAllOccupancy(), targetSquare - 8) == 0) {
                            pawnMoves.add(new Move(originSquare, targetSquare - 8, 6, 0,0,1,0,0));
                        }
                    }
                }
                currentAttacks = calculatedData.getAllPawnAttacks()[1][originSquare] & bitBoardState.getWhiteOccupancy();
                while (currentAttacks != 0L) {
                    targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                    if (originSquare >= getAllSquaresToIndices().get("A2") && originSquare <= getAllSquaresToIndices().get("H2")) {
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 7,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 8,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 9,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 10,1,0,0,0));
                    }
                    else {
                        // normal capture
                        pawnMoves.add(new Move(originSquare, targetSquare, 6, 0,1,0,0,0));

                    }
                    currentAttacks = popBit(currentAttacks, targetSquare);
                }
                if (bitBoardState.getEnPassantSquare() != -1) {
                    long enPassantAttacks = calculatedData.getAllPawnAttacks()[1][originSquare] & (1L << bitBoardState.getEnPassantSquare());
                    if (enPassantAttacks != 0L) {
                        int enPassantTarget = bitScanForwardDeBruijn64(enPassantAttacks);
                        pawnMoves.add(new Move(originSquare, enPassantTarget, 6, 0,1,0,1,0));

                    }
                }
                currentBitBoard = popBit(currentBitBoard, originSquare);
            }
        }
        return pawnMoves;
    }

    public List<Move> generatePseudoLegalCastlingMoves(BitBoardState bitBoardState) { // rename to oldState
        List<Move> castlingMoves = new ArrayList<>();
        Side currentSide = bitBoardState.getSideToMove();
        if (currentSide == Side.WHITE) {
            // white king side castling
            // check whether the squares to the king side are occupied
            // they can't be attacked!
            // add the castling move as a valid move!
            if ((bitBoardState.getAllCastlingRights() & getWk().getType()) != 0 && getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("F1")) == 0
                    && getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("G1")) == 0
                    && !isSquareAttacked(getAllSquaresToIndices().get("E1"), 1, bitBoardState)
                    && !isSquareAttacked(getAllSquaresToIndices().get("F1"), 1, bitBoardState)) {
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E1"), getAllSquaresToIndices().get("G1"), WHITE_KING, 0,0,0,0, 1));

            }

            if ((bitBoardState.getAllCastlingRights() & getWq().getType()) != 0 && getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("D1")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("C1")) == 0
                    && getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("B1")) == 0
                    && !isSquareAttacked(getAllSquaresToIndices().get("E1"), 1, bitBoardState)
                    && !isSquareAttacked(getAllSquaresToIndices().get("D1"), 1, bitBoardState)) {
                // add the castling move as a valid move!
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E1"), getAllSquaresToIndices().get("C1"), WHITE_KING, 0,0,0,0, 1));
            }

        }
        else {
//            System.out.println("getting here");
            // check whether the squares to the king side are occupied
            // they can't be attacked!
            // add the castling move as a valid move!
            if ((bitBoardState.getAllCastlingRights() & getBk().getType()) != 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("F8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("G8")) == 0 &&
                    !isSquareAttacked(getAllSquaresToIndices().get("E8"), 0, bitBoardState) &&
                    !isSquareAttacked(getAllSquaresToIndices().get("F8"), 0, bitBoardState)) {
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E8"), getAllSquaresToIndices().get("G8"), BLACK_KING, 0,0,0,0, 1));
//                System.out.println("getting here");
            }

            if ((bitBoardState.getAllCastlingRights() & getBq().getType()) != 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("D8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("C8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("B8")) == 0 &&
                    !isSquareAttacked(getAllSquaresToIndices().get("E8"), 0, bitBoardState) &&
                    !isSquareAttacked(getAllSquaresToIndices().get("D8"), 0, bitBoardState)) {
                // add the castling move as a valid move!
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E8"), getAllSquaresToIndices().get("C8"), BLACK_KING, 0,0,0,0, 1));

            }
        }
        return castlingMoves;
    }

    private List<Move> generatePseudoLegalKnightMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        Side side = bitBoardState.getSideToMove();
        List<Move> knightMoves = new ArrayList<>();
        currentBitBoard = side == Side.WHITE? bitBoardState.getBitBoards()[WHITE_KNIGHT] :
                bitBoardState.getBitBoards()[BLACK_KNIGHT];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getAllKnightAttacks()[originSquare] & (side == Side.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(side == Side.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    knightMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? BLACK_KNIGHT : WHITE_KNIGHT, 0,0,0,0, 0));
                }
                else {
                    knightMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? BLACK_KNIGHT : WHITE_KNIGHT, 0,1,0,0, 0));

                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return knightMoves;
    }

    private List<Move> generatePseudoLegalKingMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        Side side = bitBoardState.getSideToMove();
        List<Move> kingMoves = new ArrayList<>();
        currentBitBoard = side == Side.WHITE? bitBoardState.getBitBoards()[WHITE_KING] :
                bitBoardState.getBitBoards()[BLACK_KING];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getAllKingAttacks()[originSquare] & (side == Side.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(side == Side.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    kingMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? 11 : 5, 0,0,0,0, 0));

                }
                else {
                    kingMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? 11 : 5, 0,1,0,0, 0));
                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return kingMoves;
    }

    public List<Move> generatePseudoLegalBishopMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        Side side = bitBoardState.getSideToMove();
        List<Move> bishopMoves = new ArrayList<>();
        currentBitBoard = side == Side.WHITE? bitBoardState.getBitBoards()[WHITE_BISHOP] :
                bitBoardState.getBitBoards()[BLACK_BISHOP];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getBishopAttacks(originSquare, bitBoardState.getAllOccupancy()) & (side == Side.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(side == Side.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    bishopMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? BLACK_BISHOP: WHITE_BISHOP, 0,0,0,0, 0));

                }
                else {
                    bishopMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? BLACK_BISHOP: WHITE_BISHOP, 0,1,0,0, 0));

                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return bishopMoves;
    }

    private List<Move> generatePseudoLegalRookMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        Side side = bitBoardState.getSideToMove();
        List<Move> rookMoves = new ArrayList<>();
        currentBitBoard = side == Side.WHITE? bitBoardState.getBitBoards()[WHITE_ROOK] :
                bitBoardState.getBitBoards()[BLACK_ROOK];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getRookAttacks(originSquare, bitBoardState.getAllOccupancy()) & (side == Side.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(side == Side.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    rookMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? BLACK_ROOK : WHITE_ROOK, 0,0,0,0, 0));

                }
                else {
                    rookMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? BLACK_ROOK : WHITE_ROOK, 0,1,0,0, 0));

                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return rookMoves;
    }

    private List<Move> generatePseudoLegalQueenMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        Side side = bitBoardState.getSideToMove();// == Side.BLACK? Side.WHITE: Side.BLACK; // if fenstring has w then w needs to move
        List<Move> queenMoves = new ArrayList<>();
        currentBitBoard = side == Side.WHITE? bitBoardState.getBitBoards()[WHITE_QUEEN] :
                bitBoardState.getBitBoards()[BLACK_QUEEN];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getQueenAttacks(originSquare, bitBoardState.getAllOccupancy()) & (side == Side.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(side == Side.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    queenMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? BLACK_QUEEN : WHITE_QUEEN, 0,0,0,0, 0));

                }
                else {
                    queenMoves.add(new Move(originSquare, targetSquare, side == Side.BLACK? BLACK_QUEEN : WHITE_QUEEN, 0,1,0,0, 0));
                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return queenMoves;
    }
}
