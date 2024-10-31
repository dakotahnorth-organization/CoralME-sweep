package com.coralblocks.coralme;

import com.coralblocks.coralme.util.CharEnum;
import com.coralblocks.coralme.util.CharMap;

public enum CancelRejectReason implements CharEnum {
    NOT_FOUND('F');

    private final char b;
    public static final CharMap<CancelRejectReason> ALL = new CharMap<CancelRejectReason>();

    static {
        for (CancelRejectReason crr : CancelRejectReason.values()) {
            if (ALL.put(crr.getChar(), crr) != null)
                throw new IllegalStateException("Duplicate: " + crr);
        }
    }

    private CancelRejectReason(char b) {
        this.b = b;
    }

    @Override
    public final char getChar() {
        return b;
    }
}
