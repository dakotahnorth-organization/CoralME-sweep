package com.coralblocks.coralme;

import com.coralblocks.coralme.util.CharEnum;
import com.coralblocks.coralme.util.CharMap;
import com.coralblocks.coralme.util.StringUtils;

public enum TimeInForce implements CharEnum {
    GTC('T', "1"),
    IOC('I', "3"),
    DAY('D', "0");

    private final char b;
    private final String fixCode;
    public static final CharMap<TimeInForce> ALL = new CharMap<TimeInForce>();

    static {
        for (TimeInForce tif : TimeInForce.values()) {
            if (ALL.put(tif.getChar(), tif) != null)
                throw new IllegalStateException("Duplicate: " + tif);
        }
    }

    private TimeInForce(char b, String fixCode) {
        this.b = b;
        this.fixCode = fixCode;
    }

    public static final TimeInForce fromFixCode(CharSequence sb) {
        for (TimeInForce s : TimeInForce.values()) {
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
}
