package net.jueb.usb_hid_tool.enums.tag;

public enum LocalTag {
    USAGE(0, "Usage"),
    USAGE_MINIMUM(1, "Usage Minimum"),
    USAGE_MAXIMUM(2, "Usage Maximum"),
    DESIGNATOR_INDEX(3, "Designator Index"),
    DESIGNATOR_MINIMUM(4, "Designator Minimum"),
    DESIGNATOR_MAXIMUM(5, "Designator Maximum"),
    STRING_INDEX(7, "String Index"),
    STRING_MINIMUM(8, "String Minimum"),
    STRING_MAXIMUM(9, "String Maximum"),
    DELIMITER(10, "Delimiter"),
    UNKNOWN(-1, "Unknown");

    private final int value;
    private final String description;

    LocalTag(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static LocalTag fromValue(int value) {
        for (LocalTag type : LocalTag.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}