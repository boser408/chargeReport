package com.mangrove.chargereport.tools;

import com.mangrove.chargereport.entity.CtnrInfo;
import com.mangrove.chargereport.entity.LSPupload;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DownloadHandle {
    private final HttpServletResponse response;

    public DownloadHandle(HttpServletResponse response) {
        this.response = response;
    }
    public <E> void dlHandle(String downloadName, String capital, List<E> eList){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/hmtl;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((downloadName).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            ServletOutputStream out = response.getOutputStream();
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            out.write(capital.getBytes(StandardCharsets.UTF_8));
            for (E e:eList) {
                out.write((e.toString() + "\n").getBytes(StandardCharsets.UTF_8));
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void ctnrInfoDl(String downloadName, List<CtnrInfo> ctnrInfoList){
        String capital = "ctnrId"+","+"mbl"+","+"ctnrType"+","+"ctnrWeight"+","+"portETA"+","+"demLFD" +","+"outGate"
                +","+"delivery"+","+"empty" +","+"emptyReturn" +","+"perDiemLFD" +","+"customer" +","+"notes\n";
        dlHandle(downloadName,capital,ctnrInfoList);
    }
    public void lspUploadTempDl(String downloadName, String capital, List<LSPupload> lsPuploadList){
        dlHandle(downloadName,capital,lsPuploadList);
    }
}
