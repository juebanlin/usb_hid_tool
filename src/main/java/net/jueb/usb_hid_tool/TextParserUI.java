package net.jueb.usb_hid_tool;

import net.jueb.usb_hid_tool.bushound.BusHoundPaser;
import net.jueb.usb_hid_tool.reportdesc.ReportDescPaser;

import javax.swing.*;
import java.awt.*;

public class TextParserUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Text Parser");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.BOTH;

            // 输入说明标签
            JLabel inputLabel = new JLabel("输入:请输入十六进制值(字节)，以逗号或空格分隔，“0x”或“$”前缀是可选的，无效数字被视为0或忽略，C风格的注释将被删除");
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 5;
            frame.add(inputLabel, gbc);

            // 创建文本输入框
            JTextArea inputTextArea = new JTextArea();
            inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));// 使用等宽字体
            JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 5;
            gbc.weightx = 1.0;
            gbc.weighty = 0.4;
            frame.add(inputScrollPane, gbc);

            // 解析方式标签
            JLabel parseLabel = new JLabel("解析方式:");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.weightx = 0;
            gbc.weighty = 0;
            frame.add(parseLabel, gbc);

            // 创建按钮区
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            JButton parseButton1 = new JButton("bushound数据清洗");
            JButton parseButton2 = new JButton("报告描述符");
            JButton parseButton3 = new JButton("USB标准请求");
            JButton parseButton4 = new JButton("按内容自动判断分析");

            buttonPanel.add(parseButton1);
            buttonPanel.add(parseButton2);
            buttonPanel.add(parseButton3);
            buttonPanel.add(parseButton4);

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 4;
            frame.add(buttonPanel, gbc);

            // 输出标签
            JLabel outputLabel = new JLabel("输出");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 5;
            frame.add(outputLabel, gbc);

            // 创建文本展示框
            JTextArea outputTextArea = new JTextArea();
            outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));// 使用等宽字体
            outputTextArea.setEditable(false); // 设置为不可编辑
            JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
            outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 始终显示垂直滚动条
            outputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 根据需要显示水平滚动条
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 5;
            gbc.weightx = 1.0;
            gbc.weighty = 0.6;
            frame.add(outputScrollPane, gbc);

            // 添加按钮事件监听器
            parseButton1.addActionListener(e -> {
                String inputText = inputTextArea.getText();
                String parsedText = BusHoundPaser.paser(inputText);
                outputTextArea.setText(parsedText);
            });

            parseButton2.addActionListener(e -> {
                String inputText = inputTextArea.getText();
                String parsedText = ReportDescPaser.parseDescriptor(inputText);
                outputTextArea.setText(parsedText);
            });

            parseButton3.addActionListener(e -> {
                String inputText = inputTextArea.getText();
                String parsedText = ReportDescPaser.parseDescriptor(inputText);
                outputTextArea.setText(parsedText);
            });

            parseButton4.addActionListener(e -> {
                String inputText = inputTextArea.getText();
                String parsedText = ReportDescPaser.parseDescriptor(inputText);
                outputTextArea.setText(parsedText);
            });

            frame.setVisible(true);
        });
    }
}
