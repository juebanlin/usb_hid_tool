package net.jueb.usb_hid_tool.enums.tag;

public enum MainTag {
    INPUT(8, "Input"),
    OUTPUT(9, "Output"),
    COLLECTION(10, "Collection"),
    FEATURE(11, "Feature"),
    END_COLLECTION(12, "End Collection"),
    UNKNOWN(-1, "Unknown");

    private final int value;
    private final String description;

    MainTag(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static MainTag fromValue(int value) {
        for (MainTag type : MainTag.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}