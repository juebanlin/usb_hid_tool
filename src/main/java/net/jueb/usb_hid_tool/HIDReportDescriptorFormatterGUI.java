package net.jueb.usb_hid_tool;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class HIDReportDescriptorFormatterGUI extends JFrame {
    private JTextArea inputArea;
    private JButton parseButton;
    private JTextArea outputArea;

    // 当前解析到的Usage Page（全局变量，支持嵌套Push/Pop）
    private Deque<Integer> usagePageStack = new ArrayDeque<>();

    public HIDReportDescriptorFormatterGUI() {
        setTitle("USB HID报告描述符解析器（带缩进和注释）");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);

        inputArea = new JTextArea(5, 60);
        JScrollPane inputScroll = new JScrollPane(inputArea);

        parseButton = new JButton("解析");

        outputArea = new JTextArea(30, 80);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        parseButton.addActionListener(e -> parseDescriptor());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("输入USB HID报告描述符(十六进制, 空格或逗号分隔):"), BorderLayout.NORTH);
        topPanel.add(inputScroll, BorderLayout.CENTER);
        topPanel.add(parseButton, BorderLayout.SOUTH);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(outputScroll, BorderLayout.CENTER);
    }

    private void parseDescriptor() {
        String text = inputArea.getText().trim();
        if (text.isEmpty()) return;

        String[] tokens = text.replaceAll("[,]", " ").split("\\s+");
        ArrayList<Byte> bytesList = new ArrayList<>();
        try {
            for (String token : tokens) {
                if (!token.isEmpty()) {
                    bytesList.add((byte) Integer.parseInt(token.replace("0x", ""), 16));
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "输入格式错误，请确保输入为十六进制字节，用空格或逗号分隔。");
            return;
        }
        byte[] data = new byte[bytesList.size()];
        for (int i = 0; i < bytesList.size(); i++) data[i] = bytesList.get(i);

        String formatted = formatHIDReportDescriptor(data);
        outputArea.setText(formatted);
    }

    private String formatHIDReportDescriptor(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int indent = 0;
        usagePageStack.clear();
        usagePageStack.push(0); // 默认Usage Page为0

        while (i < data.length) {
            //Device Class Definition for HID 1.11_hid1_11.pdf -> 6.2.2.2 Short Items 文档解析
            int prefix = data[i] & 0xFF;//0000 00 00  bTag bType bSize
            int bSize = prefix & 0x03;
            int bType = (prefix >> 2) & 0x03;//0 main 1 global 2 local 3 reserved
            int bTag = (prefix >> 4) & 0x0F;

            int dataLen = (bSize == 3) ? 4 : bSize;
            if (i + dataLen >= data.length) break;

            // 取数据
            int value = 0;
            StringBuilder valueHex = new StringBuilder();
            for (int j = 0; j < dataLen; j++) {
                int b = data[i + 1 + j] & 0xFF;
                value |= (b << (8 * j));
                if (j > 0) valueHex.append(", ");
                valueHex.append(String.format("0x%02X", b));
            }

            // 如果是End Collection，先减少缩进
            if (bType == 0 && bTag == 12) { // End Collection
                if (indent > 0) indent--;
            }

            // 处理缩进
            String indentStr = "    ".repeat(Math.max(0, indent));
            StringBuilder line = new StringBuilder();
            line.append(indentStr);

            // 字节显示
            line.append(String.format("0x%02X", prefix));
            if (dataLen > 0) line.append(", ").append(valueHex);

            // 对齐注释
            int pad = 36 - line.length();
            if (pad < 1) pad = 1;
            line.append(" ".repeat(pad));

            // 注释
            String comment = getDescriptorComment(bType, bTag, value, valueHex.toString());
            line.append("// ").append(comment);

            // 输出行内容
            sb.append(line).append("\n");

            // 如果是Collection，增加缩进
            if (bType == 0 && bTag == 10) { // Collection
                indent++;
            }

            // 处理Usage Page嵌套
            if (bType == 1 && bTag == 0) { // Usage Page
                usagePageStack.pop();
                usagePageStack.push(value);
            } else if (bType == 1 && bTag == 10) { // Push
                usagePageStack.push(usagePageStack.peek());
            } else if (bType == 1 && bTag == 11) { // Pop
                if (usagePageStack.size() > 1) usagePageStack.pop();
            }

            i += 1 + dataLen;
        }
        return sb.toString();
    }

    private String getDescriptorComment(int typeValue, int tagValue, int value, String valueHex) {
        BType type = BType.fromValue(typeValue);
        switch (type) {
            case MAIN:
                MainTag mainTag = MainTag.fromValue(tagValue);
                switch (mainTag) {
                    case INPUT:
                        return "Input (" + getInputOutputFeature(value) + ")";
                    case OUTPUT:
                        return "Output (" + getInputOutputFeature(value) + ")";
                    case COLLECTION:
                        return "Collection (" + CollectionType.fromValue(value).getDescription() + ")";
                    case FEATURE:
                        return "Feature (" + getInputOutputFeature(value) + ")";
                    case END_COLLECTION:
                        return "End Collection";
                    default:
                        return "Main Item (tag=" + tagValue + ")";
                }
            case GLOBAL:
                GlobalTag globalTag = GlobalTag.fromValue(tagValue);
                switch (globalTag) {
                    case USAGE_PAGE:
                        return "Usage Page (" + getUsagePage(value) + ")";
                    case LOGICAL_MINIMUM:
                        return "Logical Minimum (" + value + ")";
                    case LOGICAL_MAXIMUM:
                        return "Logical Maximum (" + value + ")";
                    case PHYSICAL_MINIMUM:
                        return "Physical Minimum (" + value + ")";
                    case PHYSICAL_MAXIMUM:
                        return "Physical Maximum (" + value + ")";
                    case UNIT_EXPONENT:
                        return "Unit Exponent (" + value + ")";
                    case UNIT:
                        return "Unit (" + getUnitDescription(value) + ")";
                    case REPORT_SIZE:
                        return "Report Size (" + value + ")";
                    case REPORT_ID:
                        return "Report ID (" + value + ")";
                    case REPORT_COUNT:
                        return "Report Count (" + value + ")";
                    case PUSH:
                        return "Push";
                    case POP:
                        return "Pop";
                    default:
                        return "Global Item (tag=" + tagValue + ")";
                }
            case LOCAL:
                LocalTag localTag = LocalTag.fromValue(tagValue);
                switch (localTag) {
                    case USAGE:
                        return "Usage (" + getUsage(usagePageStack.peek(), value) + ")";
                    case USAGE_MINIMUM:
                        return "Usage Minimum (" + value + ")";
                    case USAGE_MAXIMUM:
                        return "Usage Maximum (" + value + ")";
                    case DESIGNATOR_INDEX:
                        return "Designator Index (" + value + ")";
                    case DESIGNATOR_MINIMUM:
                        return "Designator Minimum (" + value + ")";
                    case DESIGNATOR_MAXIMUM:
                        return "Designator Maximum (" + value + ")";
                    case STRING_INDEX:
                        return "String Index (" + value + ")";
                    case STRING_MINIMUM:
                        return "String Minimum (" + value + ")";
                    case STRING_MAXIMUM:
                        return "String Maximum (" + value + ")";
                    case DELIMITER:
                        return "Delimiter (" + value + ")";
                    default:
                        return "Local Item (tag=" + tagValue + ")";
                }
            case RESERVED:
            default:
                return "Reserved/Unknown";
        }
    }

    /**
     * //Device Class Definition for HID 1.11_hid1_11.pdf -> page 37
     * @param value
     * @return
     */
    private String getUnitDescription(int value) {
        StringBuilder sb = new StringBuilder();
        // 解析单位系统
        int system = value & 0x0F; // 低4位用于单位系统
        String systemDescription;
        switch (system) {
            case 0x0: systemDescription = "None"; break;
            case 0x1: systemDescription = "SI Linear"; break;
            case 0x2: systemDescription = "SI Rotation"; break;
            case 0x3: systemDescription = "English Linear"; break;
            case 0x4: systemDescription = "English Rotation"; break;
            default: systemDescription = "Reserved"; break;
        }
        if (systemDescription.equals("None")) {
            return "None";
        }

        sb.append("System: ").append(systemDescription);

        // 解析每个维度的指数
        int lengthExponent = (value >> 4) & 0x0F; // 第5到8位用于长度单位
        int massExponent = (value >> 8) & 0x0F; // 第9到12位用于质量单位
        int timeExponent = (value >> 12) & 0x0F; // 第13到16位用于时间单位
        int temperatureExponent = (value >> 16) & 0x0F; // 第17到20位用于温度单位
        int currentExponent = (value >> 20) & 0x0F; // 第21到24位用于电流单位
        int luminousIntensityExponent = (value >> 24) & 0x0F; // 第25到28位用于光强度单位

        // 添加每个维度的描述根据指数的值
        String lengthUnit = getLengthUnit(lengthExponent);
        if (!lengthUnit.equals("None")) {
            sb.append(", Length: ").append(lengthUnit);
        }

        String massUnit = getMassUnit(massExponent);
        if (!massUnit.equals("None")) {
            sb.append(", Mass: ").append(massUnit);
        }

        String timeUnit = getTimeUnit(timeExponent);
        if (!timeUnit.equals("None")) {
            sb.append(", Time: ").append(timeUnit);
        }

        String temperatureUnit = getTemperatureUnit(temperatureExponent);
        if (!temperatureUnit.equals("None")) {
            sb.append(", Temperature: ").append(temperatureUnit);
        }

        String currentUnit = getCurrentUnit(currentExponent);
        if (!currentUnit.equals("None")) {
            sb.append(", Current: ").append(currentUnit);
        }

        String luminousIntensityUnit = getLuminousIntensityUnit(luminousIntensityExponent);
        if (!luminousIntensityUnit.equals("None")) {
            sb.append(", Luminous Intensity: ").append(luminousIntensityUnit);
        }

        return sb.toString();
    }

    private String getLengthUnit(int exponent) {
        switch (exponent) {
            case 0x0: return "None";
            case 0x1: return "Centimeter";
            case 0x2: return "Radians";
            case 0x3: return "Inch";
            case 0x4: return "Degrees";
            default: return "None";
        }
    }

    private String getMassUnit(int exponent) {
        switch (exponent) {
            case 0x0: return "None";
            case 0x1: return "Gram";
            default: return "None";
        }
    }

    private String getTimeUnit(int exponent) {
        switch (exponent) {
            case 0x0: return "None";
            default: return "Seconds";
        }
    }

    private String getTemperatureUnit(int exponent) {
        switch (exponent) {
            case 0x0: return "None";
            case 0x1: return "Kelvin";
            default: return "None";
        }
    }

    private String getCurrentUnit(int exponent) {
        switch (exponent) {
            case 0x0: return "None";
            default: return "Ampere";
        }
    }

    private String getLuminousIntensityUnit(int exponent) {
        switch (exponent) {
            case 0x0: return "None";
            default: return "Candela";
        }
    }



    // 常见Usage Page
    private String getUsagePage(int value) {
        return UsagePage.fromValue(value).getDescription();
    }


    // Usage名称查找
    private String getUsage(int page, int usage) {
        return HidUsageTables.getUsage(page, usage);
    }

    // Input/Output/Feature属性
    private String getInputOutputFeature(int value) {
        StringBuilder sb = new StringBuilder();
        // Bit 0: Data (0) | Constant (1)
        sb.append((value & 0x01) == 0 ? "Data, " : "Constant, ");
        // Bit 1: Array (0) | Variable (1)
        sb.append((value & 0x02) == 0 ? "Array, " : "Variable, ");
        // Bit 2: Absolute (0) | Relative (1)
        sb.append((value & 0x04) == 0 ? "Absolute, " : "Relative, ");
        // Bit 3: No Wrap (0) | Wrap (1)
        sb.append((value & 0x08) == 0 ? "No Wrap, " : "Wrap, ");
        // Bit 4: Linear (0) | Non Linear (1)
        sb.append((value & 0x10) == 0 ? "Linear, " : "Non Linear, ");
        // Bit 5: Preferred State (0) | No Preferred (1)
        sb.append((value & 0x20) == 0 ? "Preferred State, " : "No Preferred, ");
        // Bit 6: No Null position (0) | Null state (1)
        sb.append((value & 0x40) == 0 ? "No Null position, " : "Null state, ");
        // Bit 7: Non Volatile (0) | Volatile (1)
        sb.append((value & 0x80) == 0 ? "Non Volatile, " : "Volatile, ");
        // Bit 8: Bit Field (0) | Buffered Bytes (1)
        sb.append((value & 0x100) == 0 ? "Bit Field" : "Buffered Bytes");
        return sb.toString();
    }

    // 内部静态类，常量表
    public static class HidUsageTables {
        public static final int POWER_PAGE = 0x84;
        public static final int BATTERY_SYSTEM_PAGE = 0x85;

        public static final Map<Integer, Map<Integer, String>> USAGE_TABLES = new HashMap<>();

        static {
            // PowerPage (0x84)
            Map<Integer, String> powerPage = new HashMap<>();
            powerPage.put(0x00, "Undefined");
            powerPage.put(0x01, "iName");
            powerPage.put(0x02, "PresentStatus");
            powerPage.put(0x03, "ChangedStatus");
            powerPage.put(0x04, "UPS");
            powerPage.put(0x05, "PowerSupply");
            // ... 0x06-0x0F Reserved
            powerPage.put(0x10, "BatterySystem");
            powerPage.put(0x11, "BatterySystemId");
            powerPage.put(0x12, "Battery");
            powerPage.put(0x13, "BatteryId");
            powerPage.put(0x14, "Charger");
            powerPage.put(0x15, "ChargerId");
            powerPage.put(0x16, "PowerConverter");
            powerPage.put(0x17, "PowerConverterId");
            powerPage.put(0x18, "OutletSystem");
            powerPage.put(0x19, "OutletSystemId");
            powerPage.put(0x1A, "Input");
            powerPage.put(0x1B, "InputId");
            powerPage.put(0x1C, "Output");
            powerPage.put(0x1D, "OutputId");
            powerPage.put(0x1E, "Flow");
            powerPage.put(0x1F, "FlowId");
            powerPage.put(0x20, "Outlet");
            powerPage.put(0x21, "OutletId");
            powerPage.put(0x22, "Gang");
            powerPage.put(0x23, "GangId");
            powerPage.put(0x24, "PowerSummary");
            powerPage.put(0x25, "PowerSummaryId");
            // ... 0x26-0x2F Reserved
            powerPage.put(0x30, "Voltage");
            powerPage.put(0x31, "Current");
            powerPage.put(0x32, "Frequency");
            powerPage.put(0x33, "ApparentPower");
            powerPage.put(0x34, "ActivePower");
            powerPage.put(0x35, "PercentLoad");
            powerPage.put(0x36, "Temperature");
            powerPage.put(0x37, "Humidity");
            powerPage.put(0x38, "BadCount");
            // ... 0x39-0x3F Reserved
            powerPage.put(0x40, "ConfigVoltage");
            powerPage.put(0x41, "ConfigCurrent");
            powerPage.put(0x42, "ConfigFrequency");
            powerPage.put(0x43, "ConfigApparentPower");
            powerPage.put(0x44, "ConfigActivePower");
            powerPage.put(0x45, "ConfigPercentLoad");
            powerPage.put(0x46, "ConfigTemperature");
            powerPage.put(0x47, "ConfigHumidity");
            // ... 0x48-0x4F Reserved
            powerPage.put(0x50, "SwitchOnControl");
            powerPage.put(0x51, "SwitchOffControl");
            powerPage.put(0x52, "ToggleControl");
            powerPage.put(0x53, "LowVoltageTransfer");
            powerPage.put(0x54, "HighVoltageTransfer");
            powerPage.put(0x55, "DelayBeforeReboot");
            powerPage.put(0x56, "DelayBeforeStartup");
            powerPage.put(0x57, "DelayBeforeShutdown");
            powerPage.put(0x58, "Test");
            powerPage.put(0x59, "ModuleReset");
            powerPage.put(0x5A, "AudibleAlarmControl");
            // ... 0x5B-0x5F Reserved
            powerPage.put(0x60, "Present");
            powerPage.put(0x61, "Good");
            powerPage.put(0x62, "InternalFailure");
            powerPage.put(0x63, "VoltagOutOfRange");
            powerPage.put(0x64, "FrequencyOutOfRange");
            powerPage.put(0x65, "Overload");
            powerPage.put(0x66, "OverCharged");
            powerPage.put(0x67, "OverTemperature");
            powerPage.put(0x68, "ShutdownRequested");
            powerPage.put(0x69, "ShutdownImminent");
            // ... 0x6A Reserved
            powerPage.put(0x6B, "SwitchOn/Off");
            powerPage.put(0x6C, "Switchable");
            powerPage.put(0x6D, "Used");
            powerPage.put(0x6E, "Boost");
            powerPage.put(0x6F, "Buck");
            powerPage.put(0x70, "Initialized");
            powerPage.put(0x71, "Tested");
            powerPage.put(0x72, "AwaitingPower");
            powerPage.put(0x73, "CommunicationLost");
            // ... 0x74-0xFC Reserved
            powerPage.put(0xFD, "iManufacturer");
            powerPage.put(0xFE, "iProduct");
            powerPage.put(0xFF, "iSerialNumber");
            // ... 0x100-0xFFFF Reserved
            USAGE_TABLES.put(POWER_PAGE, powerPage);

            // BatterySystemPage (0x85)
            Map<Integer, String> batteryPage = new HashMap<>();
            batteryPage.put(0x00, "Undefined");
            batteryPage.put(0x01, "SmartBatteryBatteryMode");
            batteryPage.put(0x02, "SmartBatteryBatteryStatus");
            batteryPage.put(0x03, "SmartBatteryAlarmWarning");
            batteryPage.put(0x04, "SmartBatteryChargerMode");
            batteryPage.put(0x05, "SmartBatteryChargerStatus");
            batteryPage.put(0x06, "SmartBatteryChargerSpecInfo");
            batteryPage.put(0x07, "SmartBatterySelectorState");
            batteryPage.put(0x08, "SmartBatterySelectorPresets");
            batteryPage.put(0x09, "SmartBatterySelectorInfo");
            // ... 0x0A-0x0F Reserved
            batteryPage.put(0x10, "OptionalMfgFunction1");
            batteryPage.put(0x11, "OptionalMfgFunction2");
            batteryPage.put(0x12, "OptionalMfgFunction3");
            batteryPage.put(0x13, "OptionalMfgFunction4");
            batteryPage.put(0x14, "OptionalMfgFunction5");
            batteryPage.put(0x15, "ConnectionToSMBus");
            batteryPage.put(0x16, "OutputConnection");
            batteryPage.put(0x17, "ChargerConnection");
            batteryPage.put(0x18, "BatteryInsertion");
            batteryPage.put(0x19, "UseNext");
            batteryPage.put(0x1A, "OKToUse");
            batteryPage.put(0x1B, "BatterySupported");
            batteryPage.put(0x1C, "SelectorRevision");
            batteryPage.put(0x1D, "ChargingIndicator");
            // ... 0x1E-0x27 Reserved
            batteryPage.put(0x28, "ManufacturerAccess");
            batteryPage.put(0x29, "RemainingCapacityLimit");
            batteryPage.put(0x2A, "RemainingTimeLimit");
            batteryPage.put(0x2B, "AtRate");
            batteryPage.put(0x2C, "CapacityMode");
            batteryPage.put(0x2D, "BroadcastToCharger");
            batteryPage.put(0x2E, "PrimaryBattery");
            batteryPage.put(0x2F, "ChargeController");
            // ... 0x30-0x3F Reserved
            batteryPage.put(0x40, "TerminateCharge");
            batteryPage.put(0x41, "TerminateDischarge");
            batteryPage.put(0x42, "BelowRemainingCapacityLimit");
            batteryPage.put(0x43, "RemainingTimeLimitExpired");
            batteryPage.put(0x44, "Charging");
            batteryPage.put(0x45, "Discharging");
            batteryPage.put(0x46, "FullyCharged");
            batteryPage.put(0x47, "FullyDischarged");
            batteryPage.put(0x48, "ConditioningFlag");
            batteryPage.put(0x49, "AtRateOK");
            batteryPage.put(0x4A, "SmartBatteryErrorCode");
            batteryPage.put(0x4B, "NeedReplacement");
            // ... 0x4C-0x5F Reserved
            batteryPage.put(0x60, "AtRateTimeToFull");
            batteryPage.put(0x61, "AtRateTimeToEmpty");
            batteryPage.put(0x62, "AverageCurrent");
            batteryPage.put(0x63, "MaxError");
            batteryPage.put(0x64, "RelativeStateOfCharge");
            batteryPage.put(0x65, "AbsoluteStateOfCharge");
            batteryPage.put(0x66, "RemainingCapacity");
            batteryPage.put(0x67, "FullChargeCapacity");
            batteryPage.put(0x68, "RunTimeToEmpty");
            batteryPage.put(0x69, "AverageTimeToEmpty");
            batteryPage.put(0x6A, "AverageTimeToFull");
            batteryPage.put(0x6B, "CycleCount");
            // ... 0x6C-0x7F Reserved
            batteryPage.put(0x80, "BatteryPackModelLevel");
            batteryPage.put(0x81, "InternalChargeController");
            batteryPage.put(0x82, "PrimaryBatterySupport");
            batteryPage.put(0x83, "DesignCapacity");
            batteryPage.put(0x84, "SpecificationInfo");
            batteryPage.put(0x85, "ManufactureDate");
            batteryPage.put(0x86, "SerialNumber");
            batteryPage.put(0x87, "iManufacturerName");
            batteryPage.put(0x88, "iDeviceName");
            batteryPage.put(0x89, "iDeviceChemistry");
            batteryPage.put(0x8A, "ManufacturerData");
            batteryPage.put(0x8B, "Rechargable");
            batteryPage.put(0x8C, "WarningCapacityLimit");
            batteryPage.put(0x8D, "CapacityGranularity1");
            batteryPage.put(0x8E, "CapacityGranularity2");
            batteryPage.put(0x8F, "iOEMInformation");
            // ... 0x90-0xBF Reserved
            batteryPage.put(0xC0, "InhibitCharge");
            batteryPage.put(0xC1, "EnablePolling");
            batteryPage.put(0xC2, "ResetToZero");
            // ... 0xC3-0xCF Reserved
            batteryPage.put(0xD0, "ACPresent");
            batteryPage.put(0xD1, "BatteryPresent");
            batteryPage.put(0xD2, "PowerFail");
            batteryPage.put(0xD3, "AlarmInhibited");
            batteryPage.put(0xD4, "ThermistorUnderRange");
            batteryPage.put(0xD5, "ThermistorHot");
            batteryPage.put(0xD6, "ThermistorCold");
            batteryPage.put(0xD7, "ThermistorOverRange");
            batteryPage.put(0xD8, "VoltageOutOfRange");
            batteryPage.put(0xD9, "CurrentOutOfRange");
            batteryPage.put(0xDA, "CurrentNotRegulated");
            batteryPage.put(0xDB, "VoltageNotRegulated");
            batteryPage.put(0xDC, "MasterMode");
            // ... 0xDD-0xEF Reserved
            batteryPage.put(0xF0, "ChargerSelectorSupport");
            batteryPage.put(0xF1, "ChargerSpec");
            batteryPage.put(0xF2, "Level2");
            batteryPage.put(0xF3, "Level3");
            // ... 0xF4-0xFFFF Reserved
            USAGE_TABLES.put(BATTERY_SYSTEM_PAGE, batteryPage);
        }

        public static String getUsage(int page, int usage) {
            Map<Integer, String> table = USAGE_TABLES.get(page);
            if (table != null) {
                String name = table.get(usage);
                if (name != null) return name;
                // 检查是否在Reserved区间
                if (page == POWER_PAGE) {
                    if ((usage >= 0x06 && usage <= 0x0F) ||
                            (usage >= 0x26 && usage <= 0x2F) ||
                            (usage >= 0x39 && usage <= 0x3F) ||
                            (usage >= 0x48 && usage <= 0x4F) ||
                            (usage >= 0x5B && usage <= 0x5F) ||
                            (usage == 0x6A) ||
                            (usage >= 0x74 && usage <= 0xFC) ||
                            (usage >= 0x100 && usage <= 0xFFFF))
                        return "Reserved";
                } else if (page == BATTERY_SYSTEM_PAGE) {
                    if ((usage >= 0x0A && usage <= 0x0F) ||
                            (usage >= 0x1E && usage <= 0x27) ||
                            (usage >= 0x30 && usage <= 0x3F) ||
                            (usage >= 0x4C && usage <= 0x5F) ||
                            (usage >= 0x6C && usage <= 0x7F) ||
                            (usage >= 0x90 && usage <= 0xBF) ||
                            (usage >= 0xC3 && usage <= 0xCF) ||
                            (usage >= 0xDD && usage <= 0xEF) ||
                            (usage >= 0xF4 && usage <= 0xFFFF))
                        return "Reserved";
                }
                return String.format("0x%02X", usage);
            }
            // 其他常见Usage Page，可自行扩展
            if (page == 0x01) { // Generic Desktop
                switch (usage) {
                    case 0x01: return "Pointer";
                    case 0x02: return "Mouse";
                    case 0x04: return "Joystick";
                    case 0x05: return "Game Pad";
                    case 0x06: return "Keyboard";
                    case 0x07: return "Keypad";
                    case 0x08: return "Multi-axis Controller";
                    case 0x30: return "X";
                    case 0x31: return "Y";
                    case 0x32: return "Z";
                    case 0x33: return "Rx";
                    case 0x34: return "Ry";
                    case 0x35: return "Rz";
                    case 0x36: return "Slider";
                    case 0x37: return "Dial";
                    case 0x38: return "Wheel";
                    case 0x39: return "Hat switch";
                    case 0x3A: return "Counted Buffer";
                    case 0x3B: return "Byte Count";
                    case 0x3C: return "Motion Wakeup";
                    case 0x3D: return "Start";
                    case 0x3E: return "Select";
                    default:   return String.format("0x%02X", usage);
                }
            }
            // 未知Page
            return String.format("0x%02X", usage);
        }
    }

    public static enum CollectionType {
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

    public enum UsagePage {
        GENERIC_DESKTOP(0x01, "Generic Desktop"),
        SIMULATION(0x02, "Simulation"),
        VR_CONTROLS(0x03, "VR Controls"),
        SPORT_CONTROLS(0x04, "Sport Controls"),
        GAME_CONTROLS(0x05, "Game Controls"),
        GENERIC_DEVICE_CONTROLS(0x06, "Generic Device Controls"),
        KEYBOARD_KEYPAD(0x07, "Keyboard/Keypad"),
        LEDS(0x08, "LEDs"),
        BUTTON(0x09, "Button"),
        POWER_PAGE(0x84, "PowerPage"),
        BATTERY_SYSTEM_PAGE(0x85, "BatterySystemPage"),
        UNKNOWN(-1, "Unknown");

        private final int value;
        private final String description;

        UsagePage(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        public static UsagePage fromValue(int value) {
            for (UsagePage page : UsagePage.values()) {
                if (page.getValue() == value) {
                    return page;
                }
            }
            return UNKNOWN;
        }
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HIDReportDescriptorFormatterGUI().setVisible(true));
    }
}

