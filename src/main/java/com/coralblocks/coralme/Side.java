package com.coralblocks.coralme;

import com.coralblocks.coralme.util.CharEnum;
import com.coralblocks.coralme.util.CharMap;
import com.coralblocks.coralme.util.StringUtils;

public enum Side implements CharEnum {
    BUY('B', "1", 0),
    SELL('S', "2", 1);

    private final char b;
    private final String fixCode;
    private final int index;
    public static final CharMap<Side> ALL = new CharMap<Side>();

    static {
        for (Side s : Side.values()) {
            if (ALL.put(s.getChar(), s) != null) throw new IllegalStateException("Duplicate: " + s);
        }

        if (ALL.size() != 2) {
            throw new IllegalStateException("Side must have only two values: BUY and SELL!");
        }
    }

    private Side(char b, String fixCode, int index) {
        this.b = b;
        this.fixCode = fixCode;
        this.index = index;
    }

    public static final Side fromFixCode(CharSequence sb) {
        for (Side s : Side.values()) {
            if (StringUtils.equals(s.getFixCode(), sb)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public final char getChar() {
        return b;
    }

    public final String getFixCode() {
        return fixCode;
    }

    public final int index() {
        return index;
    }

    public final int invertedIndex() {
        return this == BUY ? SELL.index() : BUY.index();
    }

    public final boolean isBuy() {
        return this == BUY;
    }

    public final boolean isSell() {
        return this == SELL;
    }

    public final boolean isOutside(long price, long market) {
        return this == BUY ? price < market : price > market;
    }

    public final boolean isInside(long price, long market) {
        return this == BUY ? price >= market : price <= market;
    }
}
