package net.jueb.usb_hid_tool.bushound;

public class CmdOpVo {
    private LineVo req;
    private LineVo rsp;

    public LineVo getReq() {
        return req;
    }

    public void setReq(LineVo req) {
        this.req = req;
    }

    public LineVo getRsp() {
        return rsp;
    }

    public void setRsp(LineVo rsp) {
        this.rsp = rsp;
    }
}
