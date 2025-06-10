package net.jueb.usb_hid_tool.reportdesc;

import net.jueb.usb_hid_tool.reportdesc.enums.BTag;
import net.jueb.usb_hid_tool.reportdesc.enums.BType;
import net.jueb.usb_hid_tool.reportdesc.util.Utils;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ReportDescPaser {

    //16进制之间的间隔符
    public final static String hexSplitStr=", ";

    public static String parseDescriptor(String text_input) {
        String result="";
        String text = text_input.trim();
        if (text.isEmpty()) return result;

        String[] tokens = text.replaceAll("[,]", " ").split("\\s+");
        ArrayList<Byte> bytesList = new ArrayList<>();
        try {
            for (String token : tokens) {
                if (!token.isEmpty()) {
                    bytesList.add((byte) Integer.parseInt(token.replace("0x", ""), 16));
                }
            }
        } catch (NumberFormatException ex) {
            return result;
        }
        byte[] data = new byte[bytesList.size()];
        for (int i = 0; i < bytesList.size(); i++) data[i] = bytesList.get(i);
        result = formatHIDReportDescriptor(data);
        return result;
    }

    private static String formatHIDReportDescriptor(byte[] data) {
        int spaceNum="0XFF".length();//隔一个16进制的缩进距离
        String spaceStr=" ".repeat(spaceNum);//单个缩进距离
        List<Comment> list=new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int indent = 0;
        // 当前解析到的Usage Page（全局变量，支持嵌套Push/Pop）
        Deque<Integer> usagePageStack = new ArrayDeque<>();
        usagePageStack.push(0); // 默认Usage Page为0
        int maxLineHexLen=0;
        while (i < data.length) {
            Comment comment=new Comment();
            //Device Class Definition for HID 1.11_hid1_11.pdf -> 6.2.2.2 Short Items 文档解析
            {
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
                    if (j > 0) valueHex.append(hexSplitStr);
                    valueHex.append(String.format("0x%02X", b));
                }
                int ownerUsagePage=usagePageStack.peek();
                comment.setPrefix(prefix);
                comment.setbSize(bSize);
                comment.setbTag(bTag);
                comment.setTag(BTag.fromValue(BType.fromValue(bType),bTag));
                comment.setDataLen(dataLen);
                comment.setValue(value);
                comment.setValueHex(valueHex.toString());
                comment.setOwnerUsagePage(ownerUsagePage);
                int currentIndent=indent;
                if (comment.getTag()==BTag.COLLECTION) { // Collection
                    indent++; // 如果是Collection，下一个开始增加缩进
                } else if (comment.getTag()==BTag.END_COLLECTION) {// End Collection
                    if (indent > 0) indent--;// 如果是当前是End Collection，先减少缩进
                    currentIndent=indent;
                }
                comment.setIndent(currentIndent);//设置当前缩进
                comment.setLineHexStr(getHexStr(comment,spaceStr));
                comment.setLineNoteStr(getNoteStr(comment));
                maxLineHexLen=Math.max(maxLineHexLen,comment.getLineHexStr().length());
            }

            BTag tag = comment.getTag();
            int value=comment.getValue();
            list.add(comment);

            // 处理Usage Page嵌套
            if (tag==BTag.USAGE_PAGE) { // Usage Page
                usagePageStack.pop();
                usagePageStack.push(value);
            } else if (tag==BTag.PUSH) { // Push
                usagePageStack.push(usagePageStack.peek());
            } else if (tag==BTag.POP) { // Pop
                if (usagePageStack.size() > 1) usagePageStack.pop();
            }

            i += 1 + comment.getDataLen();
        }
        //打印报告描述符
        for (Comment comment : list) {
            String str = toString(comment,maxLineHexLen, spaceStr);
            sb.append(str).append("\n"); // 输出行内容
        }
        return sb.toString();
    }

    /**
     * 0xA1, 0x01                          // Collection (Application)
     *     0x09, 0x24                      // Usage (PowerSummary)  #电源摘要
     * @param comment
     * @return
     */
    public static String toString(Comment comment,int maxLineHexLen,String spaceStr){
        StringBuilder line = new StringBuilder();
        line.append(getHexStr(comment,spaceStr));
        // 对齐注释
        int pad = maxLineHexLen - line.length()+1;
        if (pad < 1) pad = 1;
        line.append(" ".repeat(pad));
        // 注释
        line.append(getNoteStr(comment));
        return line.toString();
    }

    private static String getHexStr(Comment comment,String spaceStr){
        //处理缩进
        String indentStr = spaceStr.repeat(Math.max(0, comment.getIndent()));
        StringBuilder line = new StringBuilder();
        line.append(indentStr);
        // 字节显示
        line.append(String.format("0x%02X", comment.getPrefix()));
        if (comment.getDataLen() > 0){
            line.append(hexSplitStr).append(comment.getValueHex());
        }
        //追加逗号,适配数组代码
        line.append(hexSplitStr);
        return line.toString();
    }

    private static String getNoteStr(Comment comment){
        StringBuilder line = new StringBuilder();
        String commentDesc = Utils.getCommentDescriptor(comment);
        line.append("// ").append(commentDesc);// 注释
        return line.toString();
    }
}
