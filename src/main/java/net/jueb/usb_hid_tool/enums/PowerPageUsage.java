package net.jueb.usb_hid_tool.enums;

public enum PowerPageUsage {
    UNDEFINED(0x00, "Undefined", "未定义", ""),
    I_NAME(0x01, "iName", "名称", "SV"),
    PRESENT_STATUS(0x02, "PresentStatus", "当前状态", "CL"),
    CHANGED_STATUS(0x03, "ChangedStatus", "状态改变", "CL"),
    UPS(0x04, "UPS", "不间断电源", "CA"),
    POWER_SUPPLY(0x05, "PowerSupply", "电源供应", "CA"),
    RESERVED_06_0F(0x06, 0x0F, "Reserved", "保留", ""),
    BATTERY_SYSTEM(0x10, "BatterySystem", "电池系统", "CP"),
    BATTERY_SYSTEM_ID(0x11, "BatterySystemId", "电池系统ID", "SV"),
    BATTERY(0x12, "Battery", "电池", "CP"),
    BATTERY_ID(0x13, "BatteryId", "电池ID", "SV"),
    CHARGER(0x14, "Charger", "充电器", "CP"),
    CHARGER_ID(0x15, "ChargerId", "充电器ID", "SV"),
    POWER_CONVERTER(0x16, "PowerConverter", "电源转换器", "CP"),
    POWER_CONVERTER_ID(0x17, "PowerConverterId", "电源转换器ID", "SV"),
    OUTLET_SYSTEM(0x18, "OutletSystem", "插座系统", "CP"),
    OUTLET_SYSTEM_ID(0x19, "OutletSystemId", "插座系统ID", "SV"),
    INPUT(0x1A, "Input", "输入", "CP"),
    INPUT_ID(0x1B, "InputId", "输入ID", "SV"),
    OUTPUT(0x1C, "Output", "输出", "CP"),
    OUTPUT_ID(0x1D, "OutputId", "输出ID", "SV"),
    FLOW(0x1E, "Flow", "流量", "CP"),
    FLOW_ID(0x1F, "FlowId", "流量ID", "SV"),
    OUTLET(0x20, "Outlet", "插座", "CP"),
    OUTLET_ID(0x21, "OutletId", "插座ID", "SV"),
    GANG(0x22, "Gang", "组合", "CL/CP"),
    GANG_ID(0x23, "GangId", "组合ID", "SV"),
    POWER_SUMMARY(0x24, "PowerSummary", "电源摘要", "CL/CP"),
    POWER_SUMMARY_ID(0x25, "PowerSummaryId", "电源摘要ID", "SV"),
    RESERVED_26_2F(0x26, 0x2F, "Reserved", "保留", ""),
    VOLTAGE(0x30, "Voltage", "电压", "DV"),
    CURRENT(0x31, "Current", "电流", "DV"),
    FREQUENCY(0x32, "Frequency", "频率", "DV"),
    APPARENT_POWER(0x33, "ApparentPower", "视在功率", "DV"),
    ACTIVE_POWER(0x34, "ActivePower", "有功功率", "DV"),
    PERCENT_LOAD(0x35, "PercentLoad", "负载百分比", "DV"),
    TEMPERATURE(0x36, "Temperature", "温度", "DV"),
    HUMIDITY(0x37, "Humidity", "湿度", "DV"),
    BAD_COUNT(0x38, "BadCount", "错误计数", "DV"),
    RESERVED_39_3F(0x39, 0x3F, "Reserved", "保留", ""),
    CONFIG_VOLTAGE(0x40, "ConfigVoltage", "配置电压", "SV/DV"),
    CONFIG_CURRENT(0x41, "ConfigCurrent", "配置电流", "SV/DV"),
    CONFIG_FREQUENCY(0x42, "ConfigFrequency", "配置频率", "SV/DV"),
    CONFIG_APPARENT_POWER(0x43, "ConfigApparentPower", "配置视在功率", "SV/DV"),
    CONFIG_ACTIVE_POWER(0x44, "ConfigActivePower", "配置有功功率", "SV/DV"),
    CONFIG_PERCENT_LOAD(0x45, "ConfigPercentLoad", "配置负载百分比", "SV/DV"),
    CONFIG_TEMPERATURE(0x46, "ConfigTemperature", "配置温度", "SV/DV"),
    CONFIG_HUMIDITY(0x47, "ConfigHumidity", "配置湿度", "SV/DV"),
    RESERVED_48_4F(0x48, 0x4F, "Reserved", "保留", ""),
    SWITCH_ON_CONTROL(0x50, "SwitchOnControl", "开关开启控制", "DV"),
    SWITCH_OFF_CONTROL(0x51, "SwitchOffControl", "开关关闭控制", "DV"),
    TOGGLE_CONTROL(0x52, "ToggleControl", "切换控制", "DV"),
    LOW_VOLTAGE_TRANSFER(0x53, "LowVoltageTransfer", "低电压传输", "DV"),
    HIGH_VOLTAGE_TRANSFER(0x54, "HighVoltageTransfer", "高电压传输", "DV"),
    DELAY_BEFORE_REBOOT(0x55, "DelayBeforeReboot", "重启前延迟", "DV"),
    DELAY_BEFORE_STARTUP(0x56, "DelayBeforeStartup", "启动前延迟", "DV"),
    DELAY_BEFORE_SHUTDOWN(0x57, "DelayBeforeShutdown", "关机前延迟", "DV"),
    TEST(0x58, "Test", "测试", "DV"),
    MODULE_RESET(0x59, "ModuleReset", "模块重置", "DV"),
    AUDIBLE_ALARM_CONTROL(0x5A, "AudibleAlarmControl", "声音报警控制", "DV"),
    RESERVED_5B_5F(0x5B, 0x5F, "Reserved", "保留", ""),
    PRESENT(0x60, "Present", "存在", "DF"),
    GOOD(0x61, "Good", "良好", "DF"),
    INTERNAL_FAILURE(0x62, "InternalFailure", "内部故障", "DF"),
    VOLTAGE_OUT_OF_RANGE(0x63, "VoltageOutOfRange", "电压超出范围", "DF"),
    FREQUENCY_OUT_OF_RANGE(0x64, "FrequencyOutOfRange", "频率超出范围", "DF"),
    OVERLOAD(0x65, "Overload", "过载", "DF"),
    OVERCHARGED(0x66, "OverCharged", "过充", "DF"),
    OVER_TEMPERATURE(0x67, "OverTemperature", "过温", "DF"),
    SHUTDOWN_REQUESTED(0x68, "ShutdownRequested", "请求关机", "DF"),
    SHUTDOWN_IMMINENT(0x69, "ShutdownImminent", "即将关机", "DF"),
    RESERVED_6A(0x6A, 0x6A, "Reserved", "保留", ""),
    SWITCH_ON_OFF(0x6B, "SwitchOn/Off", "开关开启/关闭", "DF"),
    SWITCHABLE(0x6C, "Switchable", "可切换", "DF"),
    USED(0x6D, "Used", "已使用", "DF"),
    BOOST(0x6E, "Boost", "提升", "DF"),
    BUCK(0x6F, "Buck", "降压", "DF"),
    INITIALIZED(0x70, "Initialized", "已初始化", "DF"),
    TESTED(0x71, "Tested", "已测试", "DF"),
    AWAITING_POWER(0x72, "AwaitingPower", "等待电源", "DF"),
    COMMUNICATION_LOST(0x73, "CommunicationLost", "通信丢失", "DF"),
    RESERVED_74_FC(0x74, 0xFC, "Reserved", "保留", ""),
    I_MANUFACTURER(0xFD, "iManufacturer", "制造商", "SV"),
    I_PRODUCT(0xFE, "iProduct", "产品", "SV"),
    I_SERIAL_NUMBER(0xFF, "iSerialNumber", "序列号", "SV"),
    RESERVED_100_FFFF(0x100, 0xFFFF, "Reserved", "保留", "");

    private final int start;
    private final int end;
    private final String name;
    private final String desc;
    private final String usageTypes;

    PowerPageUsage(int start, String name, String desc, String usageTypes) {
        this(start, start, name, desc, usageTypes);
    }

    PowerPageUsage(int start, int end, String name, String desc, String usageTypes) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.desc = desc;
        this.usageTypes = usageTypes;
    }

    public static PowerPageUsage valueOf(int pageId) {
        for (PowerPageUsage page : values()) {
            if (page.start <= pageId && pageId <= page.end) {
                return page;
            }
        }
        return RESERVED_100_FFFF; // Default to RESERVED if no match is found
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

    public String getUsageTypes() {
        return usageTypes;
    }
}
