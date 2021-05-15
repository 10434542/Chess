package bitboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static bitboard.BitBoardUtils.*;
import static bitboard.Piece.*;

/* TODO: make this a LegalMoveGenerator!
    to be done:
    - make a method that tries the move and gives a bitBoardState
    - make a getBitBoardState method that creates a new bitBoardState with a move (BitBoardState)
    - make a validateMove method that takes either a move or a BitBoardState and validates the state (boolean)
    - Adjust sideToMove within the generatePseudoLegalMove methods to NOT be current SideToMove since
 */
public class LegalMoveGenerator implements MoveGenerator{

    private final PreCalculatedData calculatedData;

    public LegalMoveGenerator(PreCalculatedData calculatedData) {
        this.calculatedData = calculatedData;
    }

    public BitBoardState tryMove(Move move, BitBoardState bitBoardState) {
        BitBoardState.BitBoardStateBuilder stateBuilder = new BitBoardState.BitBoardStateBuilder();
        long[] tempBitBoards = bitBoardState.getBitBoards();
        tempBitBoards[move.getPieceType()] = popBit(setBit(bitBoardState.getBitBoards()[move.getPieceType()], move.getTargetSquare()), move.getSourceSquare());

        if (move.getCapture() != 0) {

            int startIndex = bitBoardState.getCurrentSide() == SideToMove.WHITE ? WHITE_PAWN: BLACK_PAWN;
            int endIndex = bitBoardState.getCurrentSide() == SideToMove.WHITE ? BLACK_PAWN: BLACK_KING+1;

            for (int i = startIndex; i< endIndex; i++) {
                if (getBit(bitBoardState.getBitBoards()[i], move.getTargetSquare()) != 0) {
                    tempBitBoards[move.getPieceType()] = popBit(bitBoardState.getBitBoards()[move.getPieceType()], move.getTargetSquare());
                    break;
                }
            }
        }

        if (move.getPromoted() != 0) {
            int currentPawn = bitBoardState.getCurrentSide() == SideToMove.WHITE? WHITE_PAWN : BLACK_PAWN;
            tempBitBoards[currentPawn] = popBit(bitBoardState.getBitBoards()[currentPawn], move.getTargetSquare());
            tempBitBoards[move.getPromoted()] = setBit(bitBoardState.getBitBoards()[move.getPromoted()], move.getTargetSquare());
        }

        if (move.getEnPassant() != 0) {
            int pawnToBeRemoved = bitBoardState.getCurrentSide() == SideToMove.WHITE? BLACK_PAWN: WHITE_PAWN;
            int offset = bitBoardState.getCurrentSide() == SideToMove.WHITE? -8: 8;
            tempBitBoards[pawnToBeRemoved] = popBit(bitBoardState.getBitBoards()[pawnToBeRemoved], move.getTargetSquare() + offset);
        }

        stateBuilder.enPassantSquare(-1);

        if (move.getDouble() != 0) {
            stateBuilder.enPassantSquare(bitBoardState.getCurrentSide() == SideToMove.WHITE ? move.getTargetSquare() - 8: move.getTargetSquare() + 8);
        }

        // TODO : castling + further building the BitBoardState

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


        stateBuilder.allCastlingRights(bitBoardState.getAllCastlingRights() & getCastlingSquares()[move.getSourceSquare()])
                .allCastlingRights(bitBoardState.getAllCastlingRights() & getCastlingSquares()[move.getTargetSquare()]);

        long tempWhiteOccupancy = 0L;
        long tempBlackOccupancy = 0L;
        for (int i = 0; i < 12; i++) {
            if (i > 6) {
                tempBlackOccupancy |=bitBoardState.getBitBoards()[i];
            }
            else {
                tempWhiteOccupancy |= bitBoardState.getBitBoards()[i];
            }
        }

        stateBuilder.whiteOccupancy(tempWhiteOccupancy).blackOccupancy(tempBlackOccupancy).allOccupancy((tempWhiteOccupancy | tempBlackOccupancy));
        stateBuilder.bitBoards(tempBitBoards).turnCounter(bitBoardState.getCurrentSide() == SideToMove.WHITE ? bitBoardState.getTurnCounter():)
        stateBuilder.sideToMove(bitBoardState.getCurrentSide() == SideToMove.WHITE ? SideToMove.BLACK: SideToMove.WHITE);
        return stateBuilder.build();
    }

    public boolean isNotInCheck(BitBoardState state) {
        if (state.getCurrentSide() == SideToMove.BLACK) {
            return !isSquareAttacked(bitScanForwardDeBruijn64(state.getBitBoards()[BLACK_KING]), 1, state);
        }
        else if (state.getCurrentSide() == SideToMove.WHITE)
            return !isSquareAttacked(bitScanForwardDeBruijn64(state.getBitBoards()[WHITE_KING]), 0, state);
        return true;
    }

    public boolean isSquareAttacked(int square, int side, BitBoardState state) {
        if (side == 0 && ((calculatedData.getAllPawnAttacks()[1][square] & state.getBitBoards()[WHITE_PAWN]) != 0L)) {
            return true;
        }
        if (side == 1 && ((calculatedData.getAllPawnAttacks()[0][square] & state.getBitBoards()[BLACK_PAWN]) != 0L)) {
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

        List<Move> kingMoves = generatePseudoLegalKingMoves(state).stream().filter(x->this.isNotInCheck(this.tryMove(x, state))).collect(Collectors.toList());

        // First check the moves of the king, if king is in check other moves should not be calculated!
        if (!isNotInCheck(state)) {
            return kingMoves;
        }
        else {
            List<List<Move>> trialMoveLists = List.of(generatePseudoLegalPawnMoves(state),
                    generatePseudoLegalCastlingMoves(state),
                    kingMoves,
                    generatePseudoLegalBishopMoves(state),
                    generatePseudoLegalRookMoves(state),
                    generatePseudoLegalQueenMoves(state),
                    generatePseudoLegalKnightMoves(state));

            return trialMoveLists.stream()
                    .flatMap(Collection::stream)
                    .filter(x->this.isNotInCheck(this.tryMove(x, state)))
                    .collect(Collectors.toList());
        }
    }

    public List<List<Move>> generatePseudoLegalMoves(BitBoardState bitBoardState) {

       return List.of(generatePseudoLegalPawnMoves(bitBoardState),
                generatePseudoLegalCastlingMoves(bitBoardState),
                generatePseudoLegalKingMoves(bitBoardState),
                generatePseudoLegalBishopMoves(bitBoardState),
                generatePseudoLegalRookMoves(bitBoardState),
                generatePseudoLegalQueenMoves(bitBoardState),
                generatePseudoLegalKnightMoves(bitBoardState));//.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<Move> generatePseudoLegalPawnMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        SideToMove currentSide = bitBoardState.getCurrentSide();
        List<Move> pawnMoves = new ArrayList<>();
        if (currentSide == SideToMove.WHITE) {
            // calculate white pawn moves
            currentBitBoard = bitBoardState.getBitBoards()[WHITE_PAWN];
            while (currentBitBoard != 0L) {
                originSquare = bitScanForwardDeBruijn64(currentBitBoard);
                targetSquare = originSquare + 8;
                if (targetSquare > getAllSquaresToIndices().get("A1") && getBit(bitBoardState.getAllOccupancy(), targetSquare) == 0) {
                    if (originSquare >= getAllSquaresToIndices().get("A7")
                            && originSquare <= getAllSquaresToIndices().get("H7")) {
                        // pawn promotion
                        pawnMoves.add(new Move.MoveBuilder(originSquare, targetSquare).pieceType(0).promoted(1).capture(0).setDouble(0).enPassant(0).castling(0).encodeMove().build());
                        pawnMoves.add(new Move.MoveBuilder(originSquare, targetSquare).pieceType(0).promoted(2).capture(0).setDouble(0).enPassant(0).castling(0).encodeMove().build());
                        pawnMoves.add(new Move.MoveBuilder(originSquare, targetSquare).pieceType(0).promoted(3).capture(0).setDouble(0).enPassant(0).castling(0).encodeMove().build());
                        pawnMoves.add(new Move.MoveBuilder(originSquare, targetSquare).pieceType(0).promoted(4).capture(0).setDouble(0).enPassant(0).castling(0).encodeMove().build());

                    }
                    else {
                        // single pawn push
                        pawnMoves.add(new Move(originSquare, targetSquare, 0, 0,0,0,0,0));


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
                        pawnMoves.add(new Move(originSquare, targetSquare, 0, 4,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 0, 3,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 0, 2,1,0,0,0));
                        pawnMoves.add(new Move(originSquare, targetSquare, 0, 1,1,0,0,0));

                    }
                    else {
                        // normal capture
                        pawnMoves.add(new Move(originSquare, targetSquare, 0, 0,1,0,0,0));

                    }
                    currentAttacks = popBit(currentAttacks, targetSquare);
                }
                if (bitBoardState.getEnPassantSquare() != -1) {
                    long enPassantAttacks = calculatedData.getAllPawnAttacks()[0][originSquare] & (1L << bitBoardState.getEnPassantSquare());
                    if (enPassantAttacks != 0L) {
                        int enPassantTarget = bitScanForwardDeBruijn64(enPassantAttacks);
                        pawnMoves.add(new Move(originSquare, enPassantTarget, 0, 0,1,0,1,0));

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
                if (targetSquare > getAllSquaresToIndices().get("H8") && getBit(bitBoardState.getAllOccupancy(), targetSquare) == 0) {
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
        SideToMove currentSide = bitBoardState.getCurrentSide();

        if (currentSide == SideToMove.WHITE) {
            // white king side castling
            // check whether the squares to the king side are occupied
            // they can't be attacked!
            // add the castling move as a valid move!
            if ((bitBoardState.getAllCastlingRights() & getWk().getType()) != 0 && getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("F1")) == 0
                    && getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("G1")) == 0
                    && !isSquareAttacked(getAllSquaresToIndices().get("E1"), 0, bitBoardState)
                    && !isSquareAttacked(getAllSquaresToIndices().get("F1"), 0, bitBoardState)) {
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E1"), getAllSquaresToIndices().get("G1"), 5, 0,0,0,0, 1));

            }

            if ((bitBoardState.getAllCastlingRights() & getWq().getType()) != 0 && getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("D1")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("C1")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("B1")) == 0 && !isSquareAttacked(getAllSquaresToIndices().get("E1"), 0, bitBoardState)
                    && !isSquareAttacked(getAllSquaresToIndices().get("D1"), 0, bitBoardState)) {
                // add the castling move as a valid move!
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E1"), getAllSquaresToIndices().get("C1"), 5, 0,0,0,0, 1));
            }

        }
        else {
            // check whether the squares to the king side are occupied
            // they can't be attacked!
            // add the castling move as a valid move!
            if ((bitBoardState.getAllCastlingRights() & getBk().getType()) != 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("F8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("G8")) == 0 &&
                    !isSquareAttacked(getAllSquaresToIndices().get("E8"), 1, bitBoardState) &&
                    !isSquareAttacked(getAllSquaresToIndices().get("F8"), 1, bitBoardState)) {
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E8"), getAllSquaresToIndices().get("G8"), 11, 0,0,0,0, 1));

            }

            if ((bitBoardState.getAllCastlingRights() & getBq().getType()) != 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("D8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("C8")) == 0 &&
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("B8")) == 0 &&
                    !isSquareAttacked(getAllSquaresToIndices().get("E8"), 1, bitBoardState) &&
                    !isSquareAttacked(getAllSquaresToIndices().get("D8"), 1, bitBoardState)) {
                // add the castling move as a valid move!
                castlingMoves.add(new Move(getAllSquaresToIndices().get("E8"), getAllSquaresToIndices().get("C8"), 11, 0,0,0,0, 1));

            }
        }
        return castlingMoves;
    }

    public List<Move> generatePseudoLegalKnightMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        SideToMove sideToMove = bitBoardState.getCurrentSide();
        List<Move> knightMoves = new ArrayList<>();
        currentBitBoard = sideToMove == SideToMove.WHITE? bitBoardState.getBitBoards()[WHITE_KNIGHT] :
                bitBoardState.getBitBoards()[BLACK_KNIGHT];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getAllKnightAttacks()[originSquare] & (sideToMove == SideToMove.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(sideToMove == SideToMove.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    knightMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 7 : 1, 0,0,0,0, 0));
                }
                else {
                    knightMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 7 : 1, 0,1,0,0, 0));

                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return knightMoves;
    }

    public List<Move> generatePseudoLegalKingMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        SideToMove sideToMove = bitBoardState.getCurrentSide();
        List<Move> kingMoves = new ArrayList<>();
        currentBitBoard = sideToMove == SideToMove.WHITE? bitBoardState.getBitBoards()[WHITE_KING] :
                bitBoardState.getBitBoards()[BLACK_KING];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getAllKingAttacks()[originSquare] & (sideToMove == SideToMove.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(sideToMove == SideToMove.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    kingMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 11 : 5, 0,0,0,0, 0));

                }
                else {
                    kingMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 11 : 5, 0,1,0,0, 0));
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
        SideToMove sideToMove = bitBoardState.getCurrentSide();
        List<Move> bishopMoves = new ArrayList<>();
        currentBitBoard = sideToMove == SideToMove.WHITE? bitBoardState.getBitBoards()[WHITE_BISHOP] :
                bitBoardState.getBitBoards()[BLACK_BISHOP];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getBishopAttacks(originSquare, bitBoardState.getAllOccupancy()) & (sideToMove == SideToMove.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(sideToMove == SideToMove.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    bishopMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 8 : 2, 0,0,0,0, 0));

                }
                else {
                    bishopMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 8 : 2, 0,1,0,0, 0));

                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return bishopMoves;
    }

    public List<Move> generatePseudoLegalRookMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        SideToMove sideToMove = bitBoardState.getCurrentSide();
        List<Move> rookMoves = new ArrayList<>();
        currentBitBoard = sideToMove == SideToMove.WHITE? bitBoardState.getBitBoards()[WHITE_ROOK] :
                bitBoardState.getBitBoards()[BLACK_ROOK];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getRookAttacks(originSquare, bitBoardState.getAllOccupancy()) & (sideToMove == SideToMove.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(sideToMove == SideToMove.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    rookMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 9 : 3, 0,0,0,0, 0));

                }
                else {
                    rookMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 9 : 3, 0,1,0,0, 0));

                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return rookMoves;
    }

    public List<Move> generatePseudoLegalQueenMoves(BitBoardState bitBoardState) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
        SideToMove sideToMove = bitBoardState.getCurrentSide();
        List<Move> queenMoves = new ArrayList<>();
        currentBitBoard = sideToMove == SideToMove.WHITE? bitBoardState.getBitBoards()[WHITE_BISHOP] :
                bitBoardState.getBitBoards()[BLACK_BISHOP];
        while (currentBitBoard != 0L) {
            originSquare = bitScanForwardDeBruijn64(currentBitBoard);
            currentAttacks = calculatedData.getBishopAttacks(originSquare, bitBoardState.getAllOccupancy()) & (sideToMove == SideToMove.BLACK? ~bitBoardState.getBlackOccupancy():~bitBoardState.getWhiteOccupancy());
            while (currentAttacks != 0L) {
                targetSquare = bitScanForwardDeBruijn64(currentAttacks);
                if (getBit(sideToMove == SideToMove.WHITE ? bitBoardState.getBlackOccupancy(): bitBoardState.getWhiteOccupancy(), targetSquare) == 0L) {
                    // if bit not turned on it must be a quiet move
                    queenMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 10 : 4, 0,0,0,0, 0));

                }
                else {
                    queenMoves.add(new Move(originSquare, targetSquare, sideToMove == SideToMove.BLACK? 10 : 4, 0,1,0,0, 0));
                }
                currentAttacks = popBit(currentAttacks, targetSquare);
            }
            currentBitBoard = popBit(currentBitBoard, originSquare);
        }
        return queenMoves;
    }
}
