package bitboard;

import java.util.Arrays;

import static bitboard.BitBoardUtils.bitScanForwardDeBruijn64;
import static bitboard.BitBoardUtils.setBit;

public class PreCalculatedData {

    //<editor-fold desc="ranks and files">
    // sorted ranks, files,  diagonals AND anti-diagonals based on Little-Endian convention:
    public static final long[] ranks = {
            255L, 65280L, 16711680L, 4278190080L, 1095216660480L, 280375465082880L, 71776119061217280L, -72057594037927936L
    };


    public static final long[] files = {
            72340172838076673L, 144680345676153346L, 289360691352306692L, 578721382704613384L, 1157442765409226768L,
            2314885530818453536L, 4629771061636907072L, -9187201950435737472L
    };

    public static final long[] diagonals = {
            -9205322385119247871L, 4620710844295151872L, 2310355422147575808L, 1155177711073755136L, 577588855528488960L, 288794425616760832L,
            144396663052566528L, 72057594037927936L, -9205322385119247871L, 128L, 32832L, 8405024L, 2151686160L, 550831656968L, 141012904183812L, 36099303471055874L
    };

    public static final long[] antiDiagonals ={
            72624976668147840L, 283691315109952L, 1108169199648L, 4328785936L, 16909320L, 66052L, 258L, 1L, 72624976668147840L, -9223372036854775808L,
            4647714815446351872L, 2323998145211531264L, 1161999622361579520L, 580999813328273408L, 290499906672525312L, 145249953336295424L
    };
    //</editor-fold>

    //<editor-fold desc="magic number creation related stuff">
    public final int[] relevantBishopOccupancyBits = {
            6,5,5,5,5,5,5,6,
            5,5,5,5,5,5,5,5,
            5,5,7,7,7,7,5,5,
            5,5,7,9,9,7,5,5,
            5,5,7,9,9,7,5,5,
            5,5,7,7,7,7,5,5,
            5,5,5,5,5,5,5,5,
            6,5,5,5,5,5,5,6
    };
    public final int[] relevantRookOccupancyBits = {
            12,11,11,11,11,11,11,12,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            12,11,11,11,11,11,11,12
    };
    long[] bishopMagics = {
            0x40040822862081L,
            0x40810a4108000L,
            0x2008008400920040L,
            0x61050104000008L,
            0x8282021010016100L,
            0x41008210400a0001L,
            0x3004202104050c0L,
            0x22010108410402L,
            0x60400862888605L,
            0x6311401040228L,
            0x80801082000L,
            0x802a082080240100L,
            0x1860061210016800L,
            0x401016010a810L,
            0x1000060545201005L,
            0x21000c2098280819L,
            0x2020004242020200L,
            0x4102100490040101L,
            0x114012208001500L,
            0x108000682004460L,
            0x7809000490401000L,
            0x420b001601052912L,
            0x408c8206100300L,
            0x2231001041180110L,
            0x8010102008a02100L,
            0x204201004080084L,
            0x410500058008811L,
            0x480a040008010820L,
            0x2194082044002002L,
            0x2008a20001004200L,
            0x40908041041004L,
            0x881002200540404L,
            0x4001082002082101L,
            0x8110408880880L,
            0x8000404040080200L,
            0x200020082180080L,
            0x1184440400114100L,
            0xc220008020110412L,
            0x4088084040090100L,
            0x8822104100121080L,
            0x100111884008200aL,
            0x2844040288820200L,
            0x90901088003010L,
            0x1000a218000400L,
            0x1102010420204L,
            0x8414a3483000200L,
            0x6410849901420400L,
            0x201080200901040L,
            0x204880808050002L,
            0x1001008201210000L,
            0x16a6300a890040aL,
            0x8049000441108600L,
            0x2212002060410044L,
            0x100086308020020L,
            0x484241408020421L,
            0x105084028429c085L,
            0x4282480801080cL,
            0x81c098488088240L,
            0x1400000090480820L,
            0x4444000030208810L,
            0x1020142010820200L,
            0x2234802004018200L,
            0xc2040450820a00L,
            0x2101021090020L
    };
    long[] rookMagics = {
            0x8a80104000800020L,
            0x140002000100040L,
            0x2801880a0017001L,
            0x100081001000420L,
            0x200020010080420L,
            0x3001c0002010008L,
            0x8480008002000100L,
            0x2080088004402900L,
            0x800098204000L,
            0x2024401000200040L,
            0x100802000801000L,
            0x120800800801000L,
            0x208808088000400L,
            0x2802200800400L,
            0x2200800100020080L,
            0x801000060821100L,
            0x80044006422000L,
            0x100808020004000L,
            0x12108a0010204200L,
            0x140848010000802L,
            0x481828014002800L,
            0x8094004002004100L,
            0x4010040010010802L,
            0x20008806104L,
            0x100400080208000L,
            0x2040002120081000L,
            0x21200680100081L,
            0x20100080080080L,
            0x2000a00200410L,
            0x20080800400L,
            0x80088400100102L,
            0x80004600042881L,
            0x4040008040800020L,
            0x440003000200801L,
            0x4200011004500L,
            0x188020010100100L,
            0x14800401802800L,
            0x2080040080800200L,
            0x124080204001001L,
            0x200046502000484L,
            0x480400080088020L,
            0x1000422010034000L,
            0x30200100110040L,
            0x100021010009L,
            0x2002080100110004L,
            0x202008004008002L,
            0x20020004010100L,
            0x2048440040820001L,
            0x101002200408200L,
            0x40802000401080L,
            0x4008142004410100L,
            0x2060820c0120200L,
            0x1001004080100L,
            0x20c020080040080L,
            0x2935610830022400L,
            0x44440041009200L,
            0x280001040802101L,
            0x2100190040002085L,
            0x80c0084100102001L,
            0x4024081001000421L,
            0x20030a0244872L,
            0x12001008414402L,
            0x2006104900a0804L,
            0x1004081002402L
    };

    //    long seed = System.nanoTime();
    int cSeed =  1804289383;
    long seed = cSeed&0xffffffffL;


    public long randomNumber() {
        long seedInt = seed;
        seedInt ^= (seedInt << 13);
        seedInt &= 0xffffffffL; // mask due to overflowing bits from 32 onward!
        seedInt ^= (seedInt >>> 17);
        seedInt &= 0xffffffffL;
        seedInt ^= (seedInt << 5);
        seedInt &= 0xffffffffL;
        seed = seedInt;
        return seedInt;
    }

    public long pseudoRandomNumber() {
        long first = (randomNumber() & 0xFFFF);
        long second = (randomNumber() & 0xFFFF);
        long third = (randomNumber() & 0xFFFF);
        long fourth = (randomNumber() & 0xFFFF);
        return first | (second << 16) | (third << 32) | (fourth << 48);
    }

    public long magicNumberCandidate() {
        return pseudoRandomNumber() & pseudoRandomNumber() & pseudoRandomNumber();
    }

    public long findMagicNumbers(int square, int numberOfBits, SliderType rookOrBishop) {
        long[] occupancies = new long[4096];
        long[] attacks = new long[4096];
        long[] usedAttacks;
        long attackMask = rookOrBishop == SliderType.BISHOP? maskBishopAttacks(square): maskRookAttacks(square);
        int occupancyIndex = (1 << numberOfBits);

        for (int i = 0; i < occupancyIndex; i++) {
            occupancies[i] = generateSetOccupancies(i, numberOfBits, attackMask);
            attacks[i] = rookOrBishop == SliderType.BISHOP? generateBishopAttacksOnTheFly(square, occupancies[i]):
                    generateRookAttacksOnTheFly(square, occupancies[i]);
        }

        for (int i = 0; i < 100000000; i++) {
            long magicNumber = magicNumberCandidate();
            usedAttacks = new long[4096];
            if ((Long.bitCount((attackMask * magicNumber) & 0xFF00000000000000L) < 6)) {
                continue;
            }
            int fail = 0;
            for (int index = 0; index < occupancyIndex; index++) {
                if (fail == 1) {
                    break;
                }
                int magicIndex = (int) ((occupancies[index] * magicNumber) >>> (64 - numberOfBits));
                if (usedAttacks[magicIndex] == 0L) {
                    usedAttacks[magicIndex] = attacks[index];
                }
                else if (usedAttacks[magicIndex] != attacks[index]) {
                    fail = 1;
                }
            }

            if (fail == 0) {
                return magicNumber;
            }
        }
        System.out.println("no magic number found m'dude");
        return 0L;
    }
    //</editor-fold>

    //<editor-fold desc="pre calculated data">
    private final long[][] allPawnAttacks = pawnAttacks();
    private final long[] allKnightAttacks = knightAttacks();

    private final long[] allKingAttacks = getKingAttacks();
    private final long[] bishopMasksCalculated = getBishopMasks();
    private final long[] rookMasksCalculated = getRookMasks();

    private final long[][] bishopSliderAttacks = getBishopSliders();

    private final long[][] rookSliderAttacks = getRookSliders();
    //</editor-fold>

    //<editor-fold desc="getters for attacks">
    public long[][] getAllPawnAttacks() {
        return Arrays.copyOf(allPawnAttacks, allPawnAttacks.length);
    }

    public long[] getAllKnightAttacks() {
        return Arrays.copyOf(allKnightAttacks, allKnightAttacks.length);
    }

    public long[] getAllKingAttacks() {
        return Arrays.copyOf(allKingAttacks, allKingAttacks.length);
    }

    public long getBishopAttacks(int square, long occupancy) {
        occupancy &= bishopMasksCalculated[square];
        occupancy *= bishopMagics[square];
        occupancy >>>=  (64 - relevantBishopOccupancyBits[square]);
        return bishopSliderAttacks[square][(int)occupancy];
    }

    public long getRookAttacks(int square, long occupancy) {
        occupancy &= rookMasksCalculated[square];
        occupancy *= rookMagics[square];
        occupancy >>>= (64 - relevantRookOccupancyBits[square]);
        return rookSliderAttacks[square][(int)occupancy];
    }

    public long getQueenAttacks(int square, long occupancy) {
        return this.getBishopAttacks(square, occupancy) | this.getRookAttacks(square, occupancy);
    }

    //</editor-fold>\

    //<editor-fold desc="leaper attacks: King, Knight, Pawn">
    public long maskKnightAttacks(int square) {

        long attacks = 0L;
        long bitBoard = setBit(0L, square);

        if (((bitBoard) & (~files[0])) >> (square) != 0L) {
            attacks |= (bitBoard << 15);
        }
        if (((bitBoard) & (~files[7])) >> square != 0L) {
            attacks |= (bitBoard << 17);
        }
        if (((bitBoard) & (~files[6] & ~files[7])) >> square != 0L) {
            attacks |= (bitBoard << 10);
        }
        if (((bitBoard) & (~files[0] & ~files[1])) >> (square) != 0L) {
            attacks |= (bitBoard << 6);
        }

        if (((bitBoard) & (~files[7])) >> (square) != 0L) { // >> for square = 63 turns on otherwise turned off bits
            attacks |= (bitBoard >>> 15);
        }
        if (((bitBoard) & (~files[0])) >> square != 0L) {
            attacks |= (bitBoard >>> 17);
        }
        if (((bitBoard) & (~files[0] & ~files[1])) >> (square) != 0L) {
            attacks |= (bitBoard >>> 10);
        }
        if (((bitBoard) & (~files[6] & ~files[7])) >> (square) != 0L) {
            attacks |= (bitBoard >>> 6);
        }
        return attacks;
    }

    public long[] knightAttacks(){
        long[] attacks = new long[64];
        for (int i = 0; i < 64; i++) {
            attacks[i] = maskKnightAttacks(i);
        }
        return attacks;
    }

    public long maskPawnAttacks(int square, int side) {
        long attacks = 0L;
        long bitBoard = setBit(0L, square);
        // white
        if (side == 0) {
            if (((bitBoard << 7) & (~files[7])) >> (square + 7) != 0L) { // long cannot be interpreted as boolean
                attacks |= (bitBoard << 7);
            }
            if (((bitBoard << 9) & (~files[0])) >> (square + 9) != 0L) {
                attacks |= (bitBoard << 9);
            }
        }
        // black
        else {
            if (((bitBoard >> 7) & ~files[0]) >> (square - 7) != 0L) {
                attacks |= (bitBoard >> 7);
            }
            if (((bitBoard >> 9) & ~files[7]) >> (square  -9) != 0L) {
                attacks |= (bitBoard) >> 9;
            }
        }
        return attacks;

    }

    public long[][] pawnAttacks() {
        long[][] attacks = new long[2][64];
        for (int i=0; i<64; i++) {
            attacks[0][i] = maskPawnAttacks(i, 0);
            attacks[1][i] = maskPawnAttacks(i, 1);
        }
        return attacks;
    }

    public long maskKingAttacks(int square) {
        long bitBoard = setBit(0L, square);
        long attacks = 0L;
        attacks |= (bitBoard >>> 8);
        attacks |= (bitBoard << 8);
        if ((bitBoard & (~files[0])) >> square != 0L) {
            attacks |= (bitBoard >>> 9);
            attacks |= (bitBoard >>> 1);
            attacks |= (bitBoard << 7);
        }
        if ((bitBoard & (~files[7])) >> square != 0L) {
            attacks |= (bitBoard << 9);
            attacks |= (bitBoard << 1);
            attacks |= (bitBoard >>> 7);
        }

        return attacks;
    }

    public long[] getKingAttacks() {
        long[] attacks = new long[64];
        for (int i = 0; i < 64; i++) {
            attacks[i] = maskKingAttacks(i);
        }
        return Arrays.copyOf(attacks, attacks.length);
    }

    //</editor-fold>

    //<editor-fold desc="Slider pieces such as Rook, Bishop and the Queen">

    public long maskBishopAttacks(int square) {
        int targetRank = square / 8;
        int targetFile = square % 8;
        long attacks = 0L;
        for (int rank = targetRank + 1, file = targetFile + 1; rank<=6 && file <= 6 ; rank++, file++) {
            attacks |= (1L << (rank*8+file));
        }
        for (int rank = targetRank - 1, file = targetFile + 1; rank>=1 && file <= 6 ; rank--, file++) {
            attacks |= (1L << (rank*8+file));
        }
        for (int rank = targetRank + 1, file = targetFile - 1; rank<=6 && file >= 1 ; rank++, file--) {
            attacks |= (1L << (rank*8+file));
        }
        for (int rank = targetRank - 1, file = targetFile - 1; rank>=1 && file >=1 ; rank--, file--) {
            attacks |= (1L << (rank*8+file));
        }
        return attacks;
    }

    public long[] getBishopMasks() {
        long[] bishopMasks = new long[64];
        for (int square = 0; square < 64; square++) {
            bishopMasks[square] = maskBishopAttacks(square);
        }
        return bishopMasks;
    }

    public long maskRookAttacks(int square) {
        int targetRank = square / 8;
        int targetFile = square % 8;
        long attacks = 0L;
        for (int rank = targetRank + 1; rank <= 6; rank++) {
            attacks |= (1L << (rank*8 + targetFile));
        }
        for (int rank = targetRank - 1; rank >= 1; rank--) {
            attacks |= (1L << (rank*8 + targetFile));
        }
        for (int file = targetFile + 1; file <= 6; file++) {
            attacks |= (1L << (targetRank*8 + file));
        }
        for (int file = targetFile - 1; file >= 1; file--) {
            attacks |= (1L << (targetRank*8 + file));
        }
        return attacks;

    }

    public long[] getRookMasks() {
        long[] rookMasks = new long[64];
        for (int square = 0; square < 64; square++) {
            rookMasks[square] = maskRookAttacks(square);
        }
        return rookMasks;
    }

    public long generateRookAttacksOnTheFly(int square, long blockers) {
        long attacked = 0L;
        int targetRank = square / 8;
        int targetFile = square % 8;
        for (int rank = targetRank + 1; rank <= 7; rank++) {
            attacked |= (1L << (rank*8 + targetFile));
            if ((1L << (rank*8 + targetFile) & blockers) >>> (rank*8 + targetFile) != 0L) {
                break;
            }
        }
        for (int rank = targetRank - 1; rank >= 0; rank--) {
            attacked |= (1L << (rank*8 + targetFile));
            if ((1L << (rank*8 + targetFile) & blockers) >>> (rank*8 + targetFile) != 0L) {
                break;
            }
        }
        for (int file = targetFile + 1; file <= 7; file++) {
            attacked |= (1L << (targetRank*8 + file));
            if ((1L << (targetRank*8 + file) & blockers) >>> (targetRank*8 + file) != 0L) {
                break;
            }
        }
        for (int file = targetFile - 1; file >= 0; file--) {
            attacked |= (1L << (targetRank*8 + file));
            if ((1L << (targetRank*8 + file) & blockers) >>> (targetRank*8 + file) != 0L) {
                break;
            }
        }
        return attacked;

    }

    public long generateBishopAttacksOnTheFly(int square, long blockers) {
        long attacked = 0L;
        int targetRank = square / 8;
        int targetFile = square % 8;

        for (int rank = targetRank + 1, file = targetFile + 1; rank<=7 && file <= 7 ; rank++, file++) {
            attacked |= (1L << (rank*8+file));
            if ((1L << (rank*8 + file) & blockers) >>> (rank*8 + file) != 0L) {
                break;
            }
        }
        for (int rank = targetRank - 1, file = targetFile + 1; rank>=0 && file <= 7 ; rank--, file++) {
            attacked |= (1L << (rank*8+file));
            if ((1L << (rank*8 + file) & blockers) >>> (rank*8 + file) != 0L) {
                break;
            }
        }
        for (int rank = targetRank + 1, file = targetFile - 1; rank<=7 && file >= 0 ; rank++, file--) {
            attacked |= (1L << (rank*8+file));
            if ((1L << (rank*8 + file) & blockers) >>> (rank*8 + file) != 0L) {
                break;
            }
        }
        for (int rank = targetRank - 1, file = targetFile - 1; rank>=0 && file >=0 ; rank--, file--) {
            attacked |= (1L << (rank*8+file));
            if ((1L << (rank*8 + file) & blockers) >>> (rank*8 + file) != 0L) {
                break;
            }
        }
        return attacked;
    }

    private long[][] getBishopSliders() {
        long[][] bishopSliders = new long[64][512];
        for (int square = 0; square < 64; square++) {
            long attackMask = bishopMasksCalculated[square];
            int numberOfBitsAttackMask = Long.bitCount(attackMask);
            int occupancyIndices = (1 << numberOfBitsAttackMask);

            for (int occupancyIndex = 0; occupancyIndex < occupancyIndices; occupancyIndex++) {
                long occupancy = generateSetOccupancies(occupancyIndex, numberOfBitsAttackMask, attackMask);
                int magicIndex = (int) ((occupancy * bishopMagics[square]) >>> (64 - relevantBishopOccupancyBits[square]));
                bishopSliders[square][magicIndex] = generateBishopAttacksOnTheFly(square, occupancy);
            }
        }
        return bishopSliders;
    }

    private long[][] getRookSliders() {
        long[][] rookSliders = new long[64][4096];
        for (int square = 0; square < 64; square++) {
            long attackMask = rookMasksCalculated[square];
            int numberOfBitsAttackMask = Long.bitCount(attackMask);
            int occupancyIndices = (1 << numberOfBitsAttackMask);

            for (int occupancyIndex = 0; occupancyIndex < occupancyIndices; occupancyIndex++) {
                long occupancy = generateSetOccupancies(occupancyIndex, numberOfBitsAttackMask, attackMask);
                int magicIndex = (int) ((occupancy * rookMagics[square]) >>> (64 - relevantRookOccupancyBits[square]));
                rookSliders[square][magicIndex] = generateRookAttacksOnTheFly(square, occupancy);
            }
        }
        return rookSliders;
    }



    public long generateSetOccupancies(int index, int numberOfBits, long attackMask) {
        long occupancy = 0L;
        long tempAttackMask = attackMask;
        for (int i = 0; i < numberOfBits; i++) {
            int square = bitScanForwardDeBruijn64(tempAttackMask);
            tempAttackMask &= (tempAttackMask -1);
            if ((index & (1L << i)) != 0L) {
                occupancy |= (1L << square);
            }
        }
        return occupancy;
    }

    //</editor-fold>

}
