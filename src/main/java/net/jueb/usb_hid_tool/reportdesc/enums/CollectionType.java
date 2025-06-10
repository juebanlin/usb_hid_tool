package net.jueb.usb_hid_tool.reportdesc.enums;

/**
 * Device Class Definition for HID 1.11_hid1_11.pdf
 * Device Class Definition for Human Interface Devices (HID) Version 1.11 page 28
 * @return
 */
public enum CollectionType {
    PHYSICAL(0x00, "Physical"),
    APPLICATION(0x01, "Application"),
    LOGICAL(0x02, "Logical"),
    REPORT(0x03, "Report"),
    NAMED_ARRAY(0x04, "Named Array"),
    USAGE_SWITCH(0x05, "Usage Switch"),
    USAGE_MODIFIER(0x06, "Usage Modifier"),
    Reserved(0x07,0x7F, "Reserved"),
    VENDOR_DEFINED(0x80,0xFF, "Vendor-defined");

    private final int start;
    private final int end;
    private final String description;

    CollectionType(int value, String description) {
        this(value,value,description);
    }
    CollectionType(int start,int end, String description) {
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static CollectionType fromValue(int value) {
        for (CollectionType type : CollectionType.values()) {
            if (value>= type.start && value<= type.end) {
                return type;
            }
        }
        return Reserved;
    }
}