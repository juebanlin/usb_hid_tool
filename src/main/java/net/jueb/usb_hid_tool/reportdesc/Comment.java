package net.jueb.usb_hid_tool.reportdesc;

import net.jueb.usb_hid_tool.reportdesc.enums.BTag;

public class Comment {

    private int prefix;
    private int bSize;
    private int bType;
    private int bTag;
    private BTag tag;
    private int dataLen;
    private int value;
    private String valueHex;
    private int ownerUsagePage;
    private int indent;

    private String lineHexStr;
    private String lineNoteStr;

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public int getbSize() {
        return bSize;
    }

    public void setbSize(int bSize) {
        this.bSize = bSize;
    }

    public int getbType() {
        return bType;
    }

    public void setbType(int bType) {
        this.bType = bType;
    }

    public int getbTag() {
        return bTag;
    }

    public void setbTag(int bTag) {
        this.bTag = bTag;
    }

    public BTag getTag() {
        return tag;
    }

    public void setTag(BTag tag) {
        this.tag = tag;
    }

    public int getDataLen() {
        return dataLen;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getValueHex() {
        return valueHex;
    }

    public void setValueHex(String valueHex) {
        this.valueHex = valueHex;
    }

    public int getOwnerUsagePage() {
        return ownerUsagePage;
    }

    public void setOwnerUsagePage(int ownerUsagePage) {
        this.ownerUsagePage = ownerUsagePage;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public void setLineHexStr(String lineHexStr) {
        this.lineHexStr = lineHexStr;
    }

    public String getLineHexStr() {
        return lineHexStr;
    }

    public void setLineNoteStr(String lineNoteStr) {
        this.lineNoteStr = lineNoteStr;
    }

    public String getLineNoteStr() {
        return lineNoteStr;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
