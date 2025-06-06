package net.jueb.usb_hid_tool;
import net.jueb.usb_hid_tool.enums.*;
import net.jueb.usb_hid_tool.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * JDK11 语法
 */
public class HIDReportDescriptorPaserGUI extends JFrame {
    private JTextArea inputArea;
    private JButton parseButton;
    private JTextArea outputArea;

    // 当前解析到的Usage Page（全局变量，支持嵌套Push/Pop）
    private Deque<Integer> usagePageStack = new ArrayDeque<>();

    public HIDReportDescriptorPaserGUI() {
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
        int spaceNum="0XFF".length();//隔一个16进制的缩进距离
        String spaceStr=" ".repeat(spaceNum);//单个缩进距离
        List<Comment> list=new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int indent = 0;
        usagePageStack.clear();
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
                    if (j > 0) valueHex.append(", ");
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
    public String toString(Comment comment,int maxLineHexLen,String spaceStr){
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

    private String getHexStr(Comment comment,String spaceStr){
        //处理缩进
        String indentStr = spaceStr.repeat(Math.max(0, comment.getIndent()));
        StringBuilder line = new StringBuilder();
        line.append(indentStr);
        // 字节显示
        line.append(String.format("0x%02X", comment.getPrefix()));
        if (comment.getDataLen() > 0) line.append(", ").append(comment.getValueHex());
        return line.toString();
    }

    private String getNoteStr(Comment comment){
        StringBuilder line = new StringBuilder();
        String commentDesc = Utils.getCommentDescriptor(comment);
        line.append("// ").append(commentDesc);// 注释
        return line.toString();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HIDReportDescriptorPaserGUI().setVisible(true));
    }
}

