package com.coralblocks.coralme;

import com.coralblocks.coralme.util.CharEnum;
import com.coralblocks.coralme.util.CharMap;
import com.coralblocks.coralme.util.StringUtils;

public enum ExecuteSide implements CharEnum {
    TAKER('T', "Y"),
    MAKER('M', "N");

    private final char b;
    private final String fixCode;
    public static final CharMap<ExecuteSide> ALL = new CharMap<ExecuteSide>();

    static {
        for (ExecuteSide es : ExecuteSide.values()) {
            if (ALL.put(es.getChar(), es) != null)
                throw new IllegalStateException("Duplicate: " + es);
        }
    }

    private ExecuteSide(char b, String fixCode) {
        this.b = b;
        this.fixCode = fixCode;
    }

    public static final ExecuteSide fromFixCode(CharSequence sb) {
        for (ExecuteSide s : ExecuteSide.values()) {
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
