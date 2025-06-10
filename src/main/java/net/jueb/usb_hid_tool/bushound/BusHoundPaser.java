package net.jueb.usb_hid_tool.bushound;

import java.util.ArrayList;
import java.util.List;

public class BusHoundPaser {

    /**
     * Data                                                Cmd.Phase.Ofs(rep)  Length    Phase  Description       Driver        Device  Address
     * --------------------------------------------------  ------------------  --------  -----  ----------------  ------------  ------  -------------------
     * 80 06 00 01  00 00 12 00                                   1.1.0                  CTL    GET DESCRIPTOR    ntoskrnl        54.0
     * 12 01 10 01  00 00 00 08  63 04 ff ff  00 01 01 02         1.2.0              18  IN     ........c.......                  54.0
     * @param text
     * @return
     */
    public static List<CmdOpVo> paserToVos(String text){
        List<LineVo> lineVos=new ArrayList<>();
        String lenSplit="  ";//2个空格拆分
        int dataLineStartIndex=-1;
        String[] split = text.split("\n");
        String[] colNames=null;
        int[] colLens=null;
        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            boolean isTableLine=line.contains("Cmd.Phase.Ofs(rep)") && line.contains("Data");
            if(isTableLine){
                colNames = line.split("\\s+");
                String colLenLine = split[i+1];
                String[] split1 = colLenLine.split(lenSplit);
                colLens=new int[split1.length];
                for (int i1 = 0; i1 < split1.length; i1++) {
                    colLens[i1]=split1[i1].length();
                }
                dataLineStartIndex=i+2;
                continue;
            }
            if(dataLineStartIndex<0 || i<dataLineStartIndex){
                continue;
            }
            //处理数据
            LineVo vo=new LineVo();
            int useLen=0;
            for (int i1 = 0; i1 < colLens.length; i1++) {
                String colName=colNames[i1];
                int colLen=colLens[i1];
                int startIndex=useLen;
                int endIndex=startIndex+colLen;
                useLen+=colLen+lenSplit.length();
                String content=line.substring(startIndex,endIndex);
                if(colName.equals("Length")){
                    String s=content.trim();
                    if(!s.isEmpty()){
                        vo.setLen(Integer.parseInt(s));
                    }
                }
                if(colName.equals("Data")){
                    vo.setData(content);
                }
                if(colName.equals("Phase")){
                    vo.setPhase(content.trim());
                }
                if(colName.equals("Description")){
                    vo.setDesc(content);
                }
                if(colName.equals("Cmd.Phase.Ofs(rep)")){
                    //38.1.0(4)
                    String cmdStr = content.trim();
                    vo.setCmdPhaseOfs(cmdStr);
                    String[] split1 = cmdStr.split("\\.");
                    int cmdSeq=Integer.parseInt(split1[0].trim());
                    int cmdStep=Integer.parseInt(split1[1].trim());
                    String s3=split1[2].trim();
                    if(s3.contains("(")){
                        s3=s3.split("\\(")[0];
                    }
                    int cmdStep2=Integer.parseInt(s3);
                    vo.setCmdSeq(cmdSeq);
                    vo.setCmdStep(cmdStep);
                    vo.setCmdStep2(cmdStep2);
                }
            }
            lineVos.add(vo);
        }
        List<CmdOpVo> res = mergeCmdOpVos(lineVos);
        return res;
    }

    private static List<CmdOpVo> mergeCmdOpVos(List<LineVo> lineVos) {
        List<CmdOpVo> res = new ArrayList<>();
        for (int i = 0; i < lineVos.size(); i++) {
            LineVo lineVo = lineVos.get(i);
            if(lineVo.getCmdStep()==1){
                CmdOpVo vo=new CmdOpVo();
                vo.setReq(lineVo);
                res.add(vo);
                continue;
            }
            if(lineVo.getCmdStep()==2){
                CmdOpVo cmdOpVo = res.get(res.size() - 1);
                if(lineVo.getCmdStep2()==0){
                    cmdOpVo.setRsp(lineVo);
                    continue;
                }
                LineVo rsp = cmdOpVo.getRsp();
                String data=rsp.getData()+"\n"+lineVo.getData();
                rsp.setData(data);
            }
        }
        return res;
    }

    public static String toString(List<CmdOpVo> list){
        StringBuilder sb = new StringBuilder();
        for (CmdOpVo cmdOpVo : list) {
            LineVo req = cmdOpVo.getReq();
            LineVo rsp = cmdOpVo.getRsp();
            sb.append("# cmdSeq:"+req.getCmdSeq()).append("\n");
            sb.append("#Phase:"+req.getPhase()+" desc:"+req.getDesc()+"\n");
            sb.append(req.getData()).append("\n");
            if(rsp!=null){
                sb.append("#Phase:"+rsp.getPhase()+" desc:"+rsp.getDesc()+"\n");
                sb.append(rsp.getData()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String paser(String txt_input){
        List<CmdOpVo> cmdOpVos = paserToVos(txt_input);
        String string = toString(cmdOpVos);
        return string;
    }

    public static void main(String[] args) {
        String str="bushound\n" +
                "\n" +
                "  Data   - Hex dump of the data transferred\n" +
                "  Cmd... - Position in the captured data\n" +
                "  Length - Total transfer length\n" +
                "  Phase  - Phase Type\n" +
                "            CTL   USB control transfer       \n" +
                "            IN    Data in transfer           \n" +
                "            USTS  USB status                 \n" +
                "  Descr  - Description of the phase\n" +
                "  Driver - Driver that submitted the command\n" +
                "            ntoskrnl     : NT Kernel & System 10.0.26100.4061 (WinBuild.160101.0800)\n" +
                "  Device - Device ID (followed by the endpoint for USB devices)\n" +
                "            (53) USB �����豸 (COM3)\n" +
                "            (54) MGE USB UPS\n" +
                "            (55) HID UPS Battery\n" +
                "  Address - FireWire async address or channel number\n" +
                "\n" +
                "\n" +
                "Data                                                Cmd.Phase.Ofs(rep)  Length    Phase  Description       Driver        Device  Address            \n" +
                "--------------------------------------------------  ------------------  --------  -----  ----------------  ------------  ------  -------------------\n" +
                "80 06 00 01  00 00 12 00                                   1.1.0                  CTL    GET DESCRIPTOR    ntoskrnl        54.0                       \n" +
                "12 01 10 01  00 00 00 08  63 04 ff ff  00 01 01 02         1.2.0              18  IN     ........c.......                  54.0                       \n" +
                "03 01                                                      1.2.16                        ..                                                           \n" +
                "80 06 00 02  00 00 09 00                                   2.1.0                  CTL    GET DESCRIPTOR    ntoskrnl        54.0                       \n" +
                "09 02 22 00  01 01 00 a0  64                               2.2.0               9  IN     ..\".....d                         54.0                       \n" +
                "80 06 00 02  00 00 22 00                                   3.1.0                  CTL    GET DESCRIPTOR    ntoskrnl        54.0                       \n" +
                "09 02 22 00  01 01 00 a0  64 09 04 00  00 01 03 01         3.2.0              34  IN     ..\".....d.......                  54.0                       \n" +
                "00 00 09 21  10 01 00 01  22 9e 03 07  05 81 03 08         3.2.16                        ...!....\".......                                             \n" +
                "00 14                                                      3.2.32                        ..                                                           \n";
        paser(str);
    }
}
