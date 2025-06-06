package net.jueb.usb_hid_tool.enums;

/**
 * HID Usage Tables 1.5_hut1_5.pdf 17页UsagePages
 */
public enum UsagePage {
    UNDEFINED(0x00, "Undefined", "未定义"),
    GENERIC_DESKTOP_PAGE(0x01, "Generic Desktop Page", "通用桌面设备"),
    SIMULATION_CONTROLS_PAGE(0x02, "Simulation Controls Page", "模拟控制设备"),
    VR_CONTROLS_PAGE(0x03, "VR Controls Page", "虚拟现实设备"),
    SPORT_CONTROLS_PAGE(0x04, "Sport Controls Page", "运动控制设备"),
    GAME_CONTROLS_PAGE(0x05, "Game Controls Page", "游戏控制设备"),
    GENERIC_DEVICE_CONTROLS_PAGE(0x06, "Generic Device Controls Page", "通用设备控制"),
    KEYBOARD_KEYPAD_PAGE(0x07, "Keyboard/Keypad Page", "键盘设备"),
    LED_PAGE(0x08, "LED Page", "LED设备"),
    BUTTON_PAGE(0x09, "Button Page", "按钮设备"),
    ORDINAL_PAGE(0x0A, "Ordinal Page", "序数设备"),
    TELEPHONY_DEVICE_PAGE(0x0B, "Telephony Device Page", "电话设备"),
    CONSUMER_PAGE(0x0C, "Consumer Page", "消费者设备"),
    DIGITIZERS_PAGE(0x0D, "Digitizers Page", "数字化设备"),
    HAPTICS_PAGE(0x0E, "Haptics Page", "触觉设备"),
    PHYSICAL_INPUT_DEVICE_PAGE(0x0F, "Physical Input Device Page", "物理输入设备"),
    UNICODE_PAGE(0x10, "Unicode Page", "Unicode字符"),
    SOC_PAGE(0x11, "SoC Page", "系统芯片设备"),
    EYE_AND_HEAD_TRACKERS_PAGE(0x12, "Eye and Head Trackers Page", "眼睛和头部跟踪设备"),
    RESERVED_13(0x13, "Reserved", "保留"),
    AUXILIARY_DISPLAY_PAGE(0x14, "Auxiliary Display Page", "辅助显示设备"),
    RESERVED_15_1F(0x15, 0x1F, "Reserved", "保留"),
    SENSORS_PAGE(0x20, "Sensors Page", "传感器设备"),
    RESERVED_21_3F(0x21, 0x3F, "Reserved", "保留"),
    MEDICAL_INSTRUMENT_PAGE(0x40, "Medical Instrument Page", "医疗仪器设备"),
    BRAILLE_DISPLAY_PAGE(0x41, "Braille Display Page", "盲文显示设备"),
    RESERVED_42_58(0x42, 0x58, "Reserved", "保留"),
    LIGHTING_AND_ILLUMINATION_PAGE(0x59, "Lighting And Illumination Page", "照明设备"),
    RESERVED_5A_7F(0x5A, 0x7F, "Reserved", "保留"),
    MONITOR_PAGE(0x80, "Monitor Page", "监视器设备"),
    MONITOR_ENUMERATED_PAGE(0x81, "Monitor Enumerated Page", "监视器枚举设备"),
    VESA_VIRTUAL_CONTROLS_PAGE(0x82, "VESA Virtual Controls Page", "VESA虚拟设备"),
    RESERVED_83(0x83, "Reserved", "保留"),
    POWER_PAGE(0x84, "Power Page", "电源设备"),
    BATTERY_SYSTEM_PAGE(0x85, "Battery System Page", "电池系统"),
    RESERVED_86_8B(0x86, 0x8B, "Reserved", "保留"),
    BARCODE_SCANNER_PAGE(0x8C, "Barcode Scanner Page", "条码扫描仪"),
    SCALES_PAGE(0x8D, "Scales Page", "秤设备"),
    MAGNETIC_STRIPE_READER_PAGE(0x8E, "Magnetic Stripe Reader Page", "磁条阅读器"),
    RESERVED_8F(0x8F, "Reserved", "保留"),
    CAMERA_CONTROL_PAGE(0x90, "Camera Control Page", "相机设备"),
    ARCADE_PAGE(0x91, "Arcade Page", "街机设备"),
    GAMING_DEVICE_PAGE(0x92, "Gaming Device Page", "游戏设备"),
    RESERVED_93_F1CF(0x93, 0xF1CF, "Reserved", "保留"),
    FIDO_ALLIANCE_PAGE(0xF1D0, "FIDO Alliance Page", "FIDO联盟设备"),
    RESERVED_F1D1_FEFF(0xF1D1, 0xFEFF, "Reserved", "保留"),
    VENDOR_DEFINED(0xFF00, 0xFFFF, "Vendor-defined", "供应商定义");

    private final int start;
    private final int end;
    private final String name;
    private final String desc;

    UsagePage(int start, String name, String desc) {
        this(start, start, name, desc);
    }

    UsagePage(int start, int end, String name, String desc) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.desc = desc;
    }

    public static UsagePage valueOf(int pageId) {
        for (UsagePage page : values()) {
            if (page.start <= pageId && pageId <= page.end) {
                return page;
            }
        }
        return RESERVED_93_F1CF; // Default to RESERVED if no match is found
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
