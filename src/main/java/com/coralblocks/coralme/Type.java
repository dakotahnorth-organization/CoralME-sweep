package com.coralblocks.coralme;

import com.coralblocks.coralme.util.CharEnum;
import com.coralblocks.coralme.util.CharMap;
import com.coralblocks.coralme.util.StringUtils;

public enum Type implements CharEnum {
    MARKET('M', "1"),
    LIMIT('L', "2");

    private final char b;
    private final String fixCode;
    public static final CharMap<Type> ALL = new CharMap<Type>();

    static {
        for (Type t : Type.values()) {
            if (ALL.put(t.getChar(), t) != null) throw new IllegalStateException("Duplicate: " + t);
        }
    }

    private Type(char b, String fixCode) {
        this.b = b;
        this.fixCode = fixCode;
    }

    public static final Type fromFixCode(CharSequence sb) {
        for (Type s : Type.values()) {
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
