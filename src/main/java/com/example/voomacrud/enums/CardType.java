package com.example.voomacrud.enums;

import java.util.HashMap;
import java.util.Map;

public enum CardType {

    CREDIT((byte) 0), DEBIT((byte) 1), PREPAID((byte) 2), WALLET((byte) 3);
    private final byte cardTypeIntValue;
    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<Byte, CardType> lookup = new HashMap<Byte, CardType>();

    static {
        for (CardType d : CardType.values()) {
            lookup.put(d.getIntValue(), d);
        }
    }
    CardType(byte i) {
        this.cardTypeIntValue = i;
    }

    private byte getIntValue() {
        return cardTypeIntValue;
    }
    public byte getIntVal() {
        return cardTypeIntValue;
    }
    public static CardType getByIntValue(byte intValue) {
        return lookup.get(intValue);
    }
}
