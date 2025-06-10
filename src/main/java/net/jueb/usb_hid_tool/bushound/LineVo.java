package net.jueb.usb_hid_tool.bushound;

public class LineVo {
    private int cmdSeq;
    private int cmdStep;
    private int cmdStep2;
    private String data;
    private int len;
    private String cmdPhaseOfs;
    private String phase;
    private String desc;

    public int getCmdSeq() {
        return cmdSeq;
    }

    public void setCmdSeq(int cmdSeq) {
        this.cmdSeq = cmdSeq;
    }

    public int getCmdStep() {
        return cmdStep;
    }

    public void setCmdStep(int cmdStep) {
        this.cmdStep = cmdStep;
    }

    public int getCmdStep2() {
        return cmdStep2;
    }

    public void setCmdStep2(int cmdStep2) {
        this.cmdStep2 = cmdStep2;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getCmdPhaseOfs() {
        return cmdPhaseOfs;
    }

    public void setCmdPhaseOfs(String cmdPhaseOfs) {
        this.cmdPhaseOfs = cmdPhaseOfs;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
