package bitboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static bitboard.BitBoardUtils.*;
import static bitboard.Piece.*;

/* TODO: make this a LegalMoveGenerator!
    done
 */

public class LegalMoveGenerator implements MoveGenerator{

    private final PreCalculatedData calculatedData;
    public static int[] squareMoveCounter = new int[64];
    public LegalMoveGenerator(PreCalculatedData calculatedData) {
        this.calculatedData = calculatedData;
    }

    public long[] perftStarter(final int depth, final BitBoardState state) {
        long[] someMetrics = new long[10];
        perftDriver(depth, null, state, someMetrics);
        return someMetrics;
    }

    private void perftDriver(final int depth, final Move currentMove, final BitBoardState state, long[] metrics) {
        if (depth == 0) {
            metrics[0]++;
            if (currentMove.getCapture() != 0) {
                metrics[1]++;
            }
            if (currentMove.getCastling() != 0) {
                metrics[2]++;
            }
            if (currentMove.getEnPassant() != 0) {
                metrics[3]++;
            }
            if (currentMove.getPromoted() != 0) {
                metrics[4]++;
            }
            if (!isNotInCheck(state)) {
                metrics[5]++;
            }
        }
        else {
            List<Move> moves = generateMoves(state);
            for (Move move: moves) {
                BitBoardState anotherState = moveToState(move, state);
                squareMoveCounter[move.getSourceSquare()] += 1;
                perftDriver(depth - 1, move, anotherState, metrics);
            }
        }
    }

    private boolean isSelfChecked(BitBoardState bitBoardState) {
        if (bitBoardState.getSideToMove() == Side.WHITE) { // if the side that moved is black, check whether
            return !isSquareAttacked(bitScanForwardDeBruijn64(bitBoardState.getBitBoards()[BLACK_KING]), 0, bitBoardState);
        }
        else if (bitBoardState.getSideToMove() == Side.BLACK)
            return !isSquareAttacked(bitScanForwardDeBruijn64(bitBoardState.getBitBoards()[WHITE_KING]), 1, bitBoardState);
        return true;
    }

    public BitBoardState moveToState(Move move, final BitBoardState bitBoardState) {
        BitBoardState.BitBoardStateBuilder stateBuilder = new BitBoardState.BitBoardStateBuilder();
        long[] tempBitBoards = bitBoardState.getBitBoards();
        tempBitBoards[move.getPieceType()] = popBit(setBit(bitBoardState.getBitBoards()[move.getPieceType()], move.getTargetSquare()), move.getSourceSquare());
        Side sideToPlay = bitBoardState.getSideToMove();

        if (move.getCapture() != 0) {
            int startIndex = bitBoardState.getSideToMove() == Side.WHITE ? BLACK_PAWN: WHITE_PAWN;
            int endIndex = bitBoardState.getSideToMove() == Side.WHITE ? BLACK_KING+1: BLACK_PAWN;
            for (int i = startIndex; i< endIndex; i++) {
                if (getBit(bitBoardState.getBitBoards()[i], move.getTargetSquare()) != 0) {
                    tempBitBoards[i] = popBit(tempBitBoards[i], move.getTargetSquare());
                    break;
                }
            }
        }

        if (move.getPromoted() != 0) {
            int currentPawn = sideToPlay == Side.WHITE? WHITE_PAWN : BLACK_PAWN;
            tempBitBoards[currentPawn] = popBit(bitBoardState.getBitBoards()[currentPawn], move.getTargetSquare());
            tempBitBoards[move.getPromoted()] = setBit(bitBoardState.getBitBoards()[move.getPromoted()], move.getTargetSquare());
        }

        if (move.getEnPassant() != 0 ) {
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

    private boolean isNotInCheck(BitBoardState state) {
        if (state.getSideToMove() == Side.BLACK) { // if the side that moved is black, check whether
            return !isSquareAttacked(bitScanForwardDeBruijn64(state.getBitBoards()[BLACK_KING]), 0, state);
        }
        else if (state.getSideToMove() == Side.WHITE)
            return !isSquareAttacked(bitScanForwardDeBruijn64(state.getBitBoards()[WHITE_KING]), 1, state);
        return true;
    }
    // square attacked by side that is passed (IS square ATTACKED BY THE SIDE THAT IS GIVEN?)
    private boolean isSquareAttacked(int square, int side, BitBoardState state) {
        if (side == 0 && ((calculatedData.getAllPawnAttacks()[side+1][square] & state.getBitBoards()[WHITE_PAWN]) != 0L)) {
            return true;
        }
        if (side == 1 && ((calculatedData.getAllPawnAttacks()[side-1][square] & state.getBitBoards()[BLACK_PAWN]) != 0L)) {
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
        if (((calculatedData.getAllKnightAttacks()[square]) & (side == 0? state.getBitBoards()[WHITE_KNIGHT]:state.getBitBoards()[BLACK_KNIGHT])) != 0L){
            return true;
        }
        return (calculatedData.getKingAttacks()[square] & (side == 0 ? state.getBitBoards()[WHITE_KING] : state.getBitBoards()[BLACK_KING])) != 0L;
    }

    public List<Move> generateMoves(BitBoardState state) {

        List<List<Move>> trialMoveLists = List.of(
                generatePseudoLegalPawnMoves(state),
                generatePseudoLegalCastlingMoves(state),
                generatePseudoLegalKingMoves(state),
                generatePseudoLegalBishopMoves(state),
                generatePseudoLegalRookMoves(state),
                generatePseudoLegalQueenMoves(state),
                generatePseudoLegalKnightMoves(state)
                );

        return trialMoveLists.stream()
                .flatMap(Collection::stream)
                .filter(move-> isSelfChecked(moveToState(move, new BitBoardState.BitBoardStateBuilder().of(state).build())))
                .collect(Collectors.toList());
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

    private List<Move> generatePseudoLegalCastlingMoves(BitBoardState bitBoardState) { // rename to oldState
        List<Move> castlingMoves = new ArrayList<>();
        Side currentSide = bitBoardState.getSideToMove();
        if (currentSide == Side.WHITE) {
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
            if ((bitBoardState.getAllCastlingRights() & getBk().getType()) != 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("F8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("G8")) == 0 &&
                    !isSquareAttacked(getAllSquaresToIndices().get("E8"), 0, bitBoardState) &&
                    !isSquareAttacked(getAllSquaresToIndices().get("F8"), 0, bitBoardState)) {
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E8"), getAllSquaresToIndices().get("G8"), BLACK_KING, 0,0,0,0, 1));
            }
            if ((bitBoardState.getAllCastlingRights() & getBq().getType()) != 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("D8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("C8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("B8")) == 0 &&
                    !isSquareAttacked(getAllSquaresToIndices().get("E8"), 0, bitBoardState) &&
                    !isSquareAttacked(getAllSquaresToIndices().get("D8"), 0, bitBoardState)) {
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

    private List<Move> generatePseudoLegalBishopMoves(BitBoardState bitBoardState) {
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
