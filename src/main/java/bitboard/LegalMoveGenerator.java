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
 */
public class LegalMoveGenerator implements MoveGenerator{

    private final PreCalculatedData calculatedData;
    private final BitBoardState bitBoardState;

    public LegalMoveGenerator(PreCalculatedData calculatedData, BitBoardState bitBoardState) {
        this.calculatedData = calculatedData;
        this.bitBoardState = bitBoardState;
    }

    public BitBoardState getBitBoardState(Move move, BitBoardState state) {
        BitBoardState.BitBoardStateBuilder stateBuilder = new BitBoardState.BitBoardStateBuilder()
                .bitBoards()
                .allCastlingRights() // TODO
    }

    public boolean validateMove(BitBoardState state, SideToMove sideToMove) {
        if (sideToMove == SideToMove.BLACK) {
            return isSquareAttacked(state.getBitBoards()) // TODO
        }
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

    public List<Move> generateMoves(SideToMove sideToMove) {
        List<Move> trialMoves = generatePseudoLegalMoves(sideToMove);
        // First check the moves of the king, if king is in check other moves should not be calculated!
//        if (bitBoard.getBitBoardState())
        return trialMoves;
    }

    public List<Move> generatePseudoLegalMoves(SideToMove sideToMove) {

       return List.of(generatePseudoLegalPawnMoves(sideToMove),
                generatePseudoLegalCastlingMoves(sideToMove),
                generatePseudoLegalKingMoves(sideToMove),
                generatePseudoLegalBishopMoves(sideToMove),
                generatePseudoLegalRookMoves(sideToMove),
                generatePseudoLegalQueenMoves(sideToMove),
                generatePseudoLegalKnightMoves(sideToMove)).stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<Move> generatePseudoLegalPawnMoves(SideToMove currentSide) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
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

    public List<Move> generatePseudoLegalCastlingMoves(SideToMove currentSide) {
        List<Move> castlingMoves = new ArrayList<>();
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
                    getBit(bitBoardState.getAllOccupancy(), getAllSquaresToIndices().get("B1")) == 0 && !isSquareAttacked(getAllSquaresToIndices().get("E1"), 0, this.bitBoardState)
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

    public List<Move> generatePseudoLegalKnightMoves(SideToMove sideToMove) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
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

    public List<Move> generatePseudoLegalKingMoves(SideToMove sideToMove) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
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

    public List<Move> generatePseudoLegalBishopMoves(SideToMove sideToMove) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
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

    public List<Move> generatePseudoLegalRookMoves(SideToMove sideToMove) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
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

    public List<Move> generatePseudoLegalQueenMoves(SideToMove sideToMove) {
        int originSquare;
        int targetSquare;
        long currentBitBoard;
        long currentAttacks;
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
