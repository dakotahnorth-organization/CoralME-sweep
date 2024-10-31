package com.coralblocks.coralme;

import com.coralblocks.coralme.util.CharEnum;
import com.coralblocks.coralme.util.CharMap;

public enum ReduceRejectReason implements CharEnum {
    ZERO('Z'),
    NEGATIVE('N'),
    INCREASE('I'),
    SUPERFLUOUS('S'),
    NOT_FOUND('F');

    private final char b;
    public static final CharMap<ReduceRejectReason> ALL = new CharMap<ReduceRejectReason>();

    static {
        for (ReduceRejectReason rrr : ReduceRejectReason.values()) {
            if (ALL.put(rrr.getChar(), rrr) != null)
                throw new IllegalStateException("Duplicate: " + rrr);
        }
    }

    private ReduceRejectReason(char b) {
        this.b = b;
    }

    @Override
    public final char getChar() {
        return b;
    }
}
