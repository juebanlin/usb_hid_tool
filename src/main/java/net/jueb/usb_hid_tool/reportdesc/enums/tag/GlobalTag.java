package net.jueb.usb_hid_tool.reportdesc.enums.tag;

public enum GlobalTag {
    USAGE_PAGE(0, "Usage Page"),
    LOGICAL_MINIMUM(1, "Logical Minimum"),
    LOGICAL_MAXIMUM(2, "Logical Maximum"),
    PHYSICAL_MINIMUM(3, "Physical Minimum"),
    PHYSICAL_MAXIMUM(4, "Physical Maximum"),
    UNIT_EXPONENT(5, "Unit Exponent"),
    UNIT(6, "Unit"),
    REPORT_SIZE(7, "Report Size"),
    REPORT_ID(8, "Report ID"),
    REPORT_COUNT(9, "Report Count"),
    PUSH(10, "Push"),
    POP(11, "Pop"),
    UNKNOWN(-1, "Unknown");

    private final int value;
    private final String description;

    GlobalTag(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static GlobalTag fromValue(int value) {
        for (GlobalTag type : GlobalTag.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}