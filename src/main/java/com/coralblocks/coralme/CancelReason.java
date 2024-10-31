package com.coralblocks.coralme;

import com.coralblocks.coralme.util.CharEnum;
import com.coralblocks.coralme.util.CharMap;

public enum CancelReason implements CharEnum {
    MISSED('M'),
    USER('U'),
    NO_LIQUIDITY('L'),
    PRICE('E'),
    CROSSED('C'),
    PURGED('P'),
    EXPIRED('D'),
    ROLLED('R');

    private final char b;
    public static final CharMap<CancelReason> ALL = new CharMap<CancelReason>();

    static {
        for (CancelReason cr : CancelReason.values()) {
            if (ALL.put(cr.getChar(), cr) != null)
                throw new IllegalStateException("Duplicate: " + cr);
        }
    }

    private CancelReason(char b) {
        this.b = b;
    }

    @Override
    public final char getChar() {
        return b;
    }
}
