package com.coralblocks.coralme;

import com.coralblocks.coralme.util.CharEnum;
import com.coralblocks.coralme.util.CharMap;

public enum RejectReason implements CharEnum {
    MISSING_FIELD('1'),
    BAD_TYPE('2'),
    BAD_TIF('3'),
    BAD_SIDE('4'),
    BAD_SYMBOL('5'),

    BAD_PRICE('P'),
    BAD_SIZE('S'),
    TRADING_HALTED('H'),
    BAD_LOT('L'),
    UNKNOWN_SYMBOL('U'),
    DUPLICATE_EXCHANGE_ORDER_ID('E'),
    DUPLICATE_CLIENT_ORDER_ID('C');

    private final char b;
    public static final CharMap<RejectReason> ALL = new CharMap<RejectReason>();

    static {
        for (RejectReason rr : RejectReason.values()) {
            if (ALL.put(rr.getChar(), rr) != null)
                throw new IllegalStateException("Duplicate: " + rr);
        }
    }

    private RejectReason(char b) {
        this.b = b;
    }

    @Override
    public final char getChar() {
        return b;
    }
}
