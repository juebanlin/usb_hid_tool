package net.jueb.usb_hid_tool.enums;

public enum CollectionType {
    PHYSICAL(0x00, "Physical"),
    APPLICATION(0x01, "Application"),
    LOGICAL(0x02, "Logical"),
    REPORT(0x03, "Report"),
    NAMED_ARRAY(0x04, "Named Array"),
    USAGE_SWITCH(0x05, "Usage Switch"),
    USAGE_MODIFIER(0x06, "Usage Modifier"),
    UNKNOWN(-1, "Unknown");

    private final int value;
    private final String description;

    CollectionType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static CollectionType fromValue(int value) {
        for (CollectionType type : CollectionType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}