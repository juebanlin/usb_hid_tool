package net.jueb.usb_hid_tool.enums;

public enum BTag {

    INPUT(BType.MAIN,8, "Input"),
    OUTPUT(BType.MAIN,9, "Output"),
    COLLECTION(BType.MAIN,10, "Collection"),
    FEATURE(BType.MAIN,11, "Feature"),
    END_COLLECTION(BType.MAIN,12, "End Collection"),

    USAGE_PAGE(BType.GLOBAL,0, "Usage Page"),
    LOGICAL_MINIMUM(BType.GLOBAL,1, "Logical Minimum"),
    LOGICAL_MAXIMUM(BType.GLOBAL,2, "Logical Maximum"),
    PHYSICAL_MINIMUM(BType.GLOBAL,3, "Physical Minimum"),
    PHYSICAL_MAXIMUM(BType.GLOBAL,4, "Physical Maximum"),
    UNIT_EXPONENT(BType.GLOBAL,5, "Unit Exponent"),
    UNIT(BType.GLOBAL,6, "Unit"),
    REPORT_SIZE(BType.GLOBAL,7, "Report Size"),
    REPORT_ID(BType.GLOBAL,8, "Report ID"),
    REPORT_COUNT(BType.GLOBAL,9, "Report Count"),
    PUSH(BType.GLOBAL,10, "Push"),
    POP(BType.GLOBAL,11, "Pop"),

    USAGE(BType.LOCAL,0, "Usage"),
    USAGE_MINIMUM(BType.LOCAL,1, "Usage Minimum"),
    USAGE_MAXIMUM(BType.LOCAL,2, "Usage Maximum"),
    DESIGNATOR_INDEX(BType.LOCAL,3, "Designator Index"),
    DESIGNATOR_MINIMUM(BType.LOCAL,4, "Designator Minimum"),
    DESIGNATOR_MAXIMUM(BType.LOCAL,5, "Designator Maximum"),
    STRING_INDEX(BType.LOCAL,7, "String Index"),
    STRING_MINIMUM(BType.LOCAL,8, "String Minimum"),
    STRING_MAXIMUM(BType.LOCAL,9, "String Maximum"),
    DELIMITER(BType.LOCAL,10, "Delimiter"),

    RESERVED(BType.RESERVED,-1, "RESERVED");

    private final BType bType;
    private final int value;
    private final String description;

    BTag(BType bType,int value, String description) {
        this.bType = bType;
        this.value = value;
        this.description = description;
    }

    public BType getbType() {
        return bType;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static BTag fromValue(BType bType,int tagValue) {
        for (BTag tag : BTag.values()) {
            if (tag.bType == bType && tag.value==tagValue) {
                return tag;
            }
        }
        return RESERVED;
    }
}
