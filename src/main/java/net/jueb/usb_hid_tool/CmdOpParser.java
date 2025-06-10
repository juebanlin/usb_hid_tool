package net.jueb.usb_hid_tool;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CmdOpParser extends JFrame {
    private JTextArea textArea;
    private JButton parseButton;
    private JTextArea resultArea;

    public CmdOpParser() {
        setTitle("CmdOp Parser");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        parseButton = new JButton("Parse");
        resultArea = new JTextArea();
        resultArea.setEditable(false); // Make result area read-only

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(textArea), new JScrollPane(resultArea));
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
        add(parseButton, BorderLayout.SOUTH);

        parseButton.addActionListener(e -> parseText());

        setVisible(true);
    }

    private void parseText() {
        String text = textArea.getText();
        List<CmdOpVo> cmdOpVos = parseCmdOp(text);
        StringBuilder result = new StringBuilder();
        for (CmdOpVo vo : cmdOpVos) {
            result.append(vo.toString()).append("\n");
        }
        resultArea.setText("result:\n"+result.toString());
    }

    private List<CmdOpVo> parseCmdOp(String text) {
        List<CmdOpVo> cmdOpVos = new ArrayList<>();
        String[] lines = text.split("\n");
        Map<String, String> req = new LinkedHashMap<>();
        Map<String, String> rsp = new LinkedHashMap<>();
        String currentCmd = null;

        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("Data")) {
                continue;
            }

            String[] parts = line.split("\\s+");
            if (parts.length > 1) {
                String cmdPhaseOfs = parts[1];
                if (cmdPhaseOfs.endsWith(".0")) {
                    if (currentCmd != null) {
                        CmdOpVo vo = new CmdOpVo(new LinkedHashMap<>(req), new LinkedHashMap<>(rsp));
                        cmdOpVos.add(vo);
                        req.clear();
                        rsp.clear();
                    }
                    currentCmd = cmdPhaseOfs;
                    req.put(cmdPhaseOfs, line);
                } else {
                    rsp.put(cmdPhaseOfs, line);
                }
            }
        }

        if (currentCmd != null) {
            CmdOpVo vo = new CmdOpVo(req, rsp);
            cmdOpVos.add(vo);
        }

        return cmdOpVos;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CmdOpParser::new);
    }
}

class CmdOpVo {
    private Map<String, String> req;
    private Map<String, String> rsp;

    public CmdOpVo(Map<String, String> req, Map<String, String> rsp) {
        this.req = req;
        this.rsp = rsp;
    }

    @Override
    public String toString() {
        return "CmdOpVo{" +
                "req=" + req +
                ", rsp=" + rsp +
                '}';
    }
}