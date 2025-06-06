package net.jueb.usb_hid_tool.util;

import net.jueb.usb_hid_tool.Comment;
import net.jueb.usb_hid_tool.enums.*;

public class Utils {

    // Input/Output/Feature属性

    /**
     * Device Class Definition for HID 1.11_hid1_11.pdf
     * Device Class Definition for Human Interface Devices (HID) Version 1.11 page 28
     * @param value
     * @return
     */
    public static String getInputOutputFeature(BTag tag,int value) {
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
        if(tag== BTag.INPUT){
            // Bit 7: Reserved (0)
            sb.append("Reserved");
        } else if(tag== BTag.OUTPUT||tag== BTag.FEATURE){
            // Bit 7: Non Volatile (0) | Volatile (1)
            sb.append((value & 0x80) == 0 ? "Non Volatile, " : "Volatile, ");
        }
        // Bit 8: Bit Field (0) | Buffered Bytes (1)
        sb.append((value & 0x100) == 0 ? "Bit Field" : "Buffered Bytes");
        return sb.toString();
    }

    /**
     * Device Class Definition for HID 1.11_hid1_11.pdf -> page 37
     * @param value GlobalTag.Unit.value
     * @return
     */
    public static String getUnitDescription(int value) {
        StringBuilder sb = new StringBuilder();
        // 解析单位系统
        int system = value & 0x0F; // 低4位用于单位系统
        String systemDescription;
        switch (system) {
            case 0x0:
                systemDescription = "None";
                break;
            case 0x1:
                systemDescription = "SI Linear";
                break;
            case 0x2:
                systemDescription = "SI Rotation";
                break;
            case 0x3:
                systemDescription = "English Linear";
                break;
            case 0x4:
                systemDescription = "English Rotation";
                break;
            default:
                systemDescription = "Reserved";
                break;
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

    private static String getLengthUnit(int exponent) {
        switch (exponent) {
            case 0x0:
                return "None";
            case 0x1:
                return "Centimeter";
            case 0x2:
                return "Radians";
            case 0x3:
                return "Inch";
            case 0x4:
                return "Degrees";
            default:
                return "None";
        }
    }

    private static String getMassUnit(int exponent) {
        switch (exponent) {
            case 0x0:
                return "None";
            case 0x1:
                return "Gram";
            default:
                return "None";
        }
    }

    private static String getTimeUnit(int exponent) {
        switch (exponent) {
            case 0x0:
                return "None";
            default:
                return "Seconds";
        }
    }

    private static String getTemperatureUnit(int exponent) {
        switch (exponent) {
            case 0x0:
                return "None";
            case 0x1:
                return "Kelvin";
            default:
                return "None";
        }
    }

    private static String getCurrentUnit(int exponent) {
        switch (exponent) {
            case 0x0:
                return "None";
            default:
                return "Ampere";
        }
    }

    private static String getLuminousIntensityUnit(int exponent) {
        switch (exponent) {
            case 0x0:
                return "None";
            default:
                return "Candela";
        }
    }

    /**
     * 生成//后面的注释内容
     * @param comment
     * @return
     */
    public static String getCommentDescriptor(Comment comment) {
        BTag tag = comment.getTag();
        int value=comment.getValue();
        int ownerUsagePage=comment.getOwnerUsagePage();
        int tagValue=comment.getbTag();
        switch (tag) {
            //main
            case INPUT:
            case OUTPUT:
            case FEATURE:
                return tag.getDescription()+" (" + Utils.getInputOutputFeature(tag,value) + ")";
            case COLLECTION:
                return tag.getDescription()+" (" + CollectionType.fromValue(value).getDescription() + ")";
            case END_COLLECTION:
                return tag.getDescription();

            //global
            case USAGE_PAGE:
                return "Usage Page (" + getUsagePageName(value) + ")";
            case UNIT:
                return "Unit (" + Utils.getUnitDescription(value) + ")";
            case LOGICAL_MINIMUM:
            case LOGICAL_MAXIMUM:
            case PHYSICAL_MINIMUM:
            case PHYSICAL_MAXIMUM:
            case UNIT_EXPONENT:
            case REPORT_SIZE:
            case REPORT_ID:
            case REPORT_COUNT:
                return tag.getDescription()+" (" + value + ")";
            case PUSH:
            case POP:
                return tag.getDescription();
            //local
            case USAGE:
                int usageId=value;
                return "Usage (" + getUsageIdName(ownerUsagePage, usageId) + ")"+"  #"+getUsageIdDesc(ownerUsagePage,usageId);
            case USAGE_MINIMUM:
            case USAGE_MAXIMUM:
            case DESIGNATOR_INDEX:
            case DESIGNATOR_MINIMUM:
            case DESIGNATOR_MAXIMUM:
            case STRING_INDEX:
            case STRING_MINIMUM:
            case STRING_MAXIMUM:
            case DELIMITER:
                return tag.getDescription()+" (" + value + ")";
            default:
                if(tag.getbType()== BType.MAIN){
                    return "Main Item (tag=" + tagValue + ")";
                }
                if(tag.getbType()==BType.GLOBAL){
                    return "Global Item (tag=" + tagValue + ")";
                }
                if(tag.getbType()==BType.LOCAL){
                    return "Local Item (tag=" + tagValue + ")";
                }
                if(tag==BTag.RESERVED){
                    return "Reserved/Unknown";
                }
                return "Reserved/Unknown";
        }
    }

    // 常见Usage Page
    public static String getUsagePageName(int value) {
        return UsagePage.valueOf(value).getName();
    }

    // Usage名称查找
    public static String getUsageIdName(int usagePageValue, int usageId) {
        UsagePage usagePage = UsagePage.valueOf(usagePageValue);
        if(usagePage==UsagePage.POWER_PAGE){
            return PowerPageUsage.valueOf(usageId).getName();
        }
        if(usagePage==UsagePage.BATTERY_SYSTEM_PAGE){
            return BatterySystemPageUsage.valueOf(usageId).getName();
        }
        // 未知Page
        return String.format("0x%02X", usageId);
    }

    // Usage名称查找
    public static String getUsageIdDesc(int usagePageValue, int usageId) {
        UsagePage usagePage = UsagePage.valueOf(usagePageValue);
        if(usagePage==UsagePage.POWER_PAGE){
            return PowerPageUsage.valueOf(usageId).getDesc();
        }
        if(usagePage==UsagePage.BATTERY_SYSTEM_PAGE){
            return BatterySystemPageUsage.valueOf(usageId).getDesc();
        }
        // 未知Page
        return "";
    }
}