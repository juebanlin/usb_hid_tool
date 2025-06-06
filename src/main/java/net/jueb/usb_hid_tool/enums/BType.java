package net.jueb.usb_hid_tool.enums;

public enum BType {
    MAIN(0),
    GLOBAL(1),
    LOCAL(2),
    RESERVED(3);

    private final int value;

    BType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BType fromValue(int value) {
        for (BType type : BType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return RESERVED;
    }
}