package net.jueb.usb_hid_tool.reportdesc.enums;

public enum BatterySystemPageUsage {
    UNDEFINED(0x00, "Undefined", "未定义", ""),
    SMART_BATTERY_BATTERY_MODE(0x01, "SmartBatteryBatteryMode", "智能电池模式", "CL"),
    SMART_BATTERY_BATTERY_STATUS(0x02, "SmartBatteryBatteryStatus", "智能电池状态", "NAry"),
    SMART_BATTERY_ALARM_WARNING(0x03, "SmartBatteryAlarmWarning", "智能电池报警", "NAry"),
    SMART_BATTERY_CHARGER_MODE(0x04, "SmartBatteryChargerMode", "智能电池充电器模式", "CL"),
    SMART_BATTERY_CHARGER_STATUS(0x05, "SmartBatteryChargerStatus", "智能电池充电器状态", "CL"),
    SMART_BATTERY_CHARGER_SPEC_INFO(0x06, "SmartBatteryChargerSpecInfo", "智能电池充电器规格信息", "CL"),
    SMART_BATTERY_SELECTOR_STATE(0x07, "SmartBatterySelectorState", "智能电池选择器状态", "CL"),
    SMART_BATTERY_SELECTOR_PRESETS(0x08, "SmartBatterySelectorPresets", "智能电池选择器预设", "CL"),
    SMART_BATTERY_SELECTOR_INFO(0x09, "SmartBatterySelectorInfo", "智能电池选择器信息", "CL"),
    RESERVED_0A_0F(0x0A, 0x0F, "Reserved", "保留", ""),
    OPTIONAL_MFG_FUNCTION1(0x10, "OptionalMfgFunction1", "可选制造功能1", "DV"),
    OPTIONAL_MFG_FUNCTION2(0x11, "OptionalMfgFunction2", "可选制造功能2", "DV"),
    OPTIONAL_MFG_FUNCTION3(0x12, "OptionalMfgFunction3", "可选制造功能3", "DV"),
    OPTIONAL_MFG_FUNCTION4(0x13, "OptionalMfgFunction4", "可选制造功能4", "DV"),
    OPTIONAL_MFG_FUNCTION5(0x14, "OptionalMfgFunction5", "可选制造功能5", "DV"),
    CONNECTION_TO_SMBUS(0x15, "ConnectionToSMBus", "连接到SMBus", "DF"),
    OUTPUT_CONNECTION(0x16, "OutputConnection", "输出连接", "DF"),
    CHARGER_CONNECTION(0x17, "ChargerConnection", "充电器连接", "DF"),
    BATTERY_INSERTION(0x18, "BatteryInsertion", "电池插入", "DF"),
    USE_NEXT(0x19, "UseNext", "使用下一个", "DF"),
    OK_TO_USE(0x1A, "OKToUse", "可以使用", "DF"),
    BATTERY_SUPPORTED(0x1B, "BatterySupported", "支持电池", "DF"),
    SELECTOR_REVISION(0x1C, "SelectorRevision", "选择器修订", "DF"),
    CHARGING_INDICATOR(0x1D, "ChargingIndicator", "充电指示器", "DF"),
    RESERVED_1E_27(0x1E, 0x27, "Reserved", "保留", ""),
    MANUFACTURER_ACCESS(0x28, "ManufacturerAccess", "制造商访问", "DV"),
    REMAINING_CAPACITY_LIMIT(0x29, "RemainingCapacityLimit", "剩余容量限制", "DV"),
    REMAINING_TIME_LIMIT(0x2A, "RemainingTimeLimit", "剩余时间限制", "DV"),
    AT_RATE(0x2B, "AtRate", "速率", "DV"),
    CAPACITY_MODE(0x2C, "CapacityMode", "容量模式", "DV"),
    BROADCAST_TO_CHARGER(0x2D, "BroadcastToCharger", "广播到充电器", "DV"),
    PRIMARY_BATTERY(0x2E, "PrimaryBattery", "主电池", "DV"),
    CHARGE_CONTROLLER(0x2F, "ChargeController", "充电控制器", "DV"),
    RESERVED_30_3F(0x30, 0x3F, "Reserved", "保留", ""),
    TERMINATE_CHARGE(0x40, "TerminateCharge", "终止充电", "Sel"),
    TERMINATE_DISCHARGE(0x41, "TerminateDischarge", "终止放电", "Sel"),
    BELOW_REMAINING_CAPACITY_LIMIT(0x42, "BelowRemainingCapacityLimit", "低于剩余容量限制", "Sel"),
    REMAINING_TIME_LIMIT_EXPIRED(0x43, "RemainingTimeLimitExpired", "剩余时间限制已过", "Sel"),
    CHARGING(0x44, "Charging", "充电中", "Sel"),
    DISCHARGING(0x45, "Discharging", "放电中", "Sel"),
    FULLY_CHARGED(0x46, "FullyCharged", "完全充电", "Sel"),
    FULLY_DISCHARGED(0x47, "FullyDischarged", "完全放电", "Sel"),
    CONDITIONING_FLAG(0x48, "ConditioningFlag", "调节标志", "DF"),
    AT_RATE_OK(0x49, "AtRateOK", "速率正常", "DF"),
    SMART_BATTERY_ERROR_CODE(0x4A, "SmartBatteryErrorCode", "智能电池错误代码", "DV"),
    NEED_REPLACEMENT(0x4B, "NeedReplacement", "需要更换", "DF"),
    RESERVED_4C_5F(0x4C, 0x5F, "Reserved", "保留", ""),
    AT_RATE_TIME_TO_FULL(0x60, "AtRateTimeToFull", "速率下充满时间", "DV"),
    AT_RATE_TIME_TO_EMPTY(0x61, "AtRateTimeToEmpty", "速率下放空时间", "DV"),
    AVERAGE_CURRENT(0x62, "AverageCurrent", "平均电流", "DV"),
    MAX_ERROR(0x63, "MaxError", "最大误差", "DV"),
    RELATIVE_STATE_OF_CHARGE(0x64, "RelativeStateOfCharge", "相对充电状态", "DV"),
    ABSOLUTE_STATE_OF_CHARGE(0x65, "AbsoluteStateOfCharge", "绝对充电状态", "DV"),
    REMAINING_CAPACITY(0x66, "RemainingCapacity", "剩余容量", "DV"),
    FULL_CHARGE_CAPACITY(0x67, "FullChargeCapacity", "满充容量", "DV"),
    RUN_TIME_TO_EMPTY(0x68, "RunTimeToEmpty", "放空运行时间", "DV"),
    AVERAGE_TIME_TO_EMPTY(0x69, "AverageTimeToEmpty", "平均放空时间", "DV"),
    AVERAGE_TIME_TO_FULL(0x6A, "AverageTimeToFull", "平均充满时间", "DV"),
    CYCLE_COUNT(0x6B, "CycleCount", "循环计数", "DV"),
    RESERVED_6C_7F(0x6C, 0x7F, "Reserved", "保留", ""),
    BATTERY_PACK_MODEL_LEVEL(0x80, "BatteryPackModelLevel", "电池包模型级别", "SV"),
    INTERNAL_CHARGE_CONTROLLER(0x81, "InternalChargeController", "内部充电控制器", "SF"),
    PRIMARY_BATTERY_SUPPORT(0x82, "PrimaryBatterySupport", "主电池支持", "SF"),
    DESIGN_CAPACITY(0x83, "DesignCapacity", "设计容量", "SV"),
    SPECIFICATION_INFO(0x84, "SpecificationInfo", "规格信息", "SV"),
    MANUFACTURE_DATE(0x85, "ManufactureDate", "生产日期", "SV"),
    SERIAL_NUMBER(0x86, "SerialNumber", "序列号", "SV"),
    I_MANUFACTURER_NAME(0x87, "iManufacturerName", "制造商名称", "SV"),
    I_DEVICE_NAME(0x88, "iDeviceName", "设备名称", "SV"),
    I_DEVICE_CHEMISTRY(0x89, "iDeviceChemistry", "设备化学成分", "SV"),
    MANUFACTURER_DATA(0x8A, "ManufacturerData", "制造商数据", "SV"),
    RECHARGEABLE(0x8B, "Rechargable", "可充电", "SV"),
    WARNING_CAPACITY_LIMIT(0x8C, "WarningCapacityLimit", "警告容量限制", "SV"),
    CAPACITY_GRANULARITY1(0x8D, "CapacityGranularity1", "容量粒度1", "SV"),
    CAPACITY_GRANULARITY2(0x8E, "CapacityGranularity2", "容量粒度2", "SV"),
    I_OEM_INFORMATION(0x8F, "iOEMInformation", "OEM信息", "SV"),
    RESERVED_90_BF(0x90, 0xBF, "Reserved", "保留", ""),
    INHIBIT_CHARGE(0xC0, "InhibitCharge", "禁止充电", "DF"),
    ENABLE_POLLING(0xC1, "EnablePolling", "启用轮询", "DF"),
    RESET_TO_ZERO(0xC2, "ResetToZero", "重置为零", "DF"),
    RESERVED_C3_CF(0xC3, 0xCF, "Reserved", "保留", ""),
    AC_PRESENT(0xD0, "ACPresent", "交流电存在", "DV"),
    BATTERY_PRESENT(0xD1, "BatteryPresent", "电池存在", "DV"),
    POWER_FAIL(0xD2, "PowerFail", "电源故障", "DV"),
    ALARM_INHIBITED(0xD3, "AlarmInhibited", "报警禁止", "DV"),
    THERMISTOR_UNDER_RANGE(0xD4, "ThermistorUnderRange", "热敏电阻低于范围", "DV"),
    THERMISTOR_HOT(0xD5, "ThermistorHot", "热敏电阻过热", "DV"),
    THERMISTOR_COLD(0xD6, "ThermistorCold", "热敏电阻过冷", "DV"),
    THERMISTOR_OVER_RANGE(0xD7, "ThermistorOverRange", "热敏电阻超出范围", "DV"),
    VOLTAGE_OUT_OF_RANGE(0xD8, "VoltageOutOfRange", "电压超出范围", "DV"),
    CURRENT_OUT_OF_RANGE(0xD9, "CurrentOutOfRange", "电流超出范围", "DV"),
    CURRENT_NOT_REGULATED(0xDA, "CurrentNotRegulated", "电流未调节", "DV"),
    VOLTAGE_NOT_REGULATED(0xDB, "VoltageNotRegulated", "电压未调节", "DV"),
    MASTER_MODE(0xDC, "MasterMode", "主模式", "DV"),
    RESERVED_DD_EF(0xDD, 0xEF, "Reserved", "保留", ""),
    CHARGER_SELECTOR_SUPPORT(0xF0, "ChargerSelectorSupport", "充电器选择器支持", "SF"),
    CHARGER_SPEC(0xF1, "ChargerSpec", "充电器规格", "SV"),
    LEVEL2(0xF2, "Level2", "级别2", "SF"),
    LEVEL3(0xF3, "Level3", "级别3", "SF"),
    RESERVED_F4_FFFF(0xF4, 0xFFFF, "Reserved", "保留", "");

    private final int start;
    private final int end;
    private final String name;
    private final String desc;
    private final String usageTypes;

    BatterySystemPageUsage(int start, String name, String desc, String usageTypes) {
        this(start, start, name, desc, usageTypes);
    }

    BatterySystemPageUsage(int start, int end, String name, String desc, String usageTypes) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.desc = desc;
        this.usageTypes = usageTypes;
    }

    public static BatterySystemPageUsage valueOf(int pageId) {
        for (BatterySystemPageUsage page : values()) {
            if (page.start <= pageId && pageId <= page.end) {
                return page;
            }
        }
        return RESERVED_F4_FFFF; // Default to RESERVED if no match is found
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
