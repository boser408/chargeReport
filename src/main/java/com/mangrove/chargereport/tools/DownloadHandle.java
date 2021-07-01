package com.mangrove.chargereport.tools;

import com.mangrove.chargereport.entity.CtnrInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DownloadHandle {
    private final HttpServletResponse response;

    public DownloadHandle(HttpServletResponse response) {
        this.response = response;
    }

    public void ctnrInfoDl(String downloadName, List<CtnrInfo> ctnrInfoList){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/hmtl;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((downloadName).getBytes("UTF-8"), "UTF-8"));
            ServletOutputStream out = response.getOutputStream();
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            String capital = "ctnrId"+","+"mbl"+","+"ctnrType"+","+"ctnrWeight"+","+"portETA"+","+"demLFD" +","+"outGate"
                    +","+"delivery"+","+"empty" +","+"emptyReturn" +","+"perDiemLFD" +","+"customer" +","+"notes\n";
            out.write(capital.getBytes("UTF-8"));
            for (CtnrInfo ctnrInfo:ctnrInfoList) {
                out.write((ctnrInfo.toCSV() + "\n").getBytes("UTF-8"));
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
