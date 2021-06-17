package com.mangrove.chargereport.controller;

import com.mangrove.chargereport.entity.Report;
import com.mangrove.chargereport.tools.InAndOut;
import com.mangrove.chargereport.tools.InAndOutImp;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static com.mangrove.chargereport.genManage.CardConstant.*;

@Controller
public class ReportController {

    InAndOut inAndOut=new InAndOutImp();

    public String getctnrlistPath(){
        String lspName=SecurityContextHolder.getContext().getAuthentication().getName();
        return dataPathPrex+"Dray"+sep+lspName+sep+"ctnrlist-"+lspName+".csv";
    }
    public String getChargePath(){
        String lspName=SecurityContextHolder.getContext().getAuthentication().getName();
        return dataPathPrex+"Dray"+sep+lspName+sep+"chargeRate-"+lspName+".csv";
    }
    public String getPendingPath(){
        String lspName=SecurityContextHolder.getContext().getAuthentication().getName();
        return dataPathPrex+"Dray"+sep+lspName+sep+"pending-"+lspName+".csv";
    }
    public String getSubmitPath(){
        String lspName=SecurityContextHolder.getContext().getAuthentication().getName();
        return dataPathPrex+"Dray"+sep+lspName+sep+"submitted-"+lspName+".csv";
    }
    public String getFinalPaht(){
        String lspName=SecurityContextHolder.getContext().getAuthentication().getName();
        return dataPathPrex+"Dray"+sep+lspName+sep+"approved-"+lspName+".csv";
    }
    @RequestMapping("/drayrpt/issuelist")
    public String issuelist(Model model){
        model.addAttribute("reports",inAndOut.readReportFromCSV(getPendingPath()));
        model.addAttribute("confreports",inAndOut.readReportFromCSV(getSubmitPath()));
        model.addAttribute("adhocFinals",inAndOut.readReportFromCSV(getFinalPaht()));
        return "drayissuelist";
    }

    @RequestMapping("/drayrpt/del")
    public String drayrptDel(@RequestParam("delID") String delID,Model model){
        List<Report> reportList=inAndOut.readReportFromCSV(getPendingPath());
        reportList.removeIf(report -> report.getId().equals(delID));
        inAndOut.saveReportToCSV(reportList,getPendingPath());
        model.addAttribute("reports",inAndOut.readReportFromCSV(getPendingPath()));
        model.addAttribute("confreports",inAndOut.readReportFromCSV(getSubmitPath()));
        model.addAttribute("adhocFinals",inAndOut.readReportFromCSV(getFinalPaht()));
        return "drayissuelist";
    }

    @RequestMapping("/drayrpt/Edit")
    public String drayreportEdit(@RequestParam("myid")String myid, Model model){
        List<Report> reportList=inAndOut.readReportFromCSV(getPendingPath());
        for (Report report:reportList){
            if(report.getId().equals(myid)){
                model.addAttribute("report",report);
                break;
            }
        }
        model.addAttribute("ctnrlist",inAndOut.readIdNameFromCSV(getctnrlistPath()));
        model.addAttribute("typelist",inAndOut.readSimpleRateFromCSV(getChargePath()));
        return "drayreportEdit";
    }

    @RequestMapping("/drayrpt/Add")
    public String drayreportAdd(Model model){
        File file=new File(getPendingPath());
        if(file.exists()){
            model.addAttribute("id",inAndOut.readReportFromCSV(getPendingPath()).size()+1);
        }else {
            model.addAttribute("id","1");
        }
        return "drayreportAdd";
    }

    @ResponseBody
    @RequestMapping("/drayrpt/saveUpdate")
    public String drayrptUpdate(@RequestParam("myid")String myid,
                                @RequestParam("costStatus")String costStatus,
                                @RequestParam("rptUpdates") String[] rptUpdates){
        if(costStatus.equals("Estimated")){
            changeRpt(myid,getPendingPath(),rptUpdates,costStatus);
        }else {
            addNewRpt(myid,getSubmitPath(),rptUpdates,costStatus);
            delById(myid,getPendingPath());
        }
        return "Update Successful!";
    }

    @ResponseBody
    @RequestMapping("/drayrpt/saveNew")
    public String drayrptAddnew(@RequestParam("myid")String myid,
                                @RequestParam("costStatus")String costStatus,
                                @RequestParam("rptUpdates") String[] rptUpdates){
        if(costStatus.equals("Estimated")){
            addNewRpt(myid,getPendingPath(),rptUpdates,costStatus);
        }else {
            addNewRpt(myid,getSubmitPath(),rptUpdates,costStatus);
        }
        return "New Record Saved!";
    }

    /*@ResponseBody
    @RequestMapping("/drayrpt/del")
    public String drayrptdel(@RequestParam("myid")String myid){
        delById(myid,getPendingPath());
        return "Update Successful!";
    }*/

    @RequestMapping("/drayrpt/download")
    public void fileDownLoad(@RequestParam("chargeStatus") String chargeStatus,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        String downloadPath;
        String downloadName;
        if(chargeStatus.equals("Estimated")){
            downloadPath=getPendingPath();
            downloadName="EstimatedDrayCharge.csv";
        }else {
            downloadPath=getSubmitPath();
            downloadName="ConfirmedDrayCharge.csv";
        }
        try {
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((downloadName).getBytes(), StandardCharsets.UTF_8));
            InputStream in = new FileInputStream(downloadPath);
            ServletOutputStream outputStream = response.getOutputStream();

            int b;
            while ((b = in.read()) != -1) {
                outputStream.write(b);
            }
            in.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeRpt(String myid,String filePath,String[] rptUpdates,String costStatus){
        List<Report> reportList=inAndOut.readReportFromCSV(filePath);
        int n=Integer.parseInt(myid)-1;
        reportList.get(n).setId(myid);
        reportList.get(n).setRefID(rptUpdates[0]);
        reportList.get(n).setCategory(rptUpdates[1]);
        reportList.get(n).setRateperuom(Float.parseFloat(rptUpdates[2].isEmpty()?"0":rptUpdates[2]));
        reportList.get(n).setQty(Float.parseFloat(rptUpdates[3].isEmpty()?"0":rptUpdates[3]));
        reportList.get(n).setUom(rptUpdates[4]);
        reportList.get(n).setCharge(Float.parseFloat(rptUpdates[5].isEmpty()?"0":rptUpdates[5]));
        reportList.get(n).setStartTime(rptUpdates[6]);
        reportList.get(n).setEndTime(rptUpdates[7]);
        reportList.get(n).setCostAdjust(Float.parseFloat(rptUpdates[8].isEmpty()?"0":rptUpdates[8]));
        reportList.get(n).setTotal(Float.parseFloat(rptUpdates[9].isEmpty()?"0":rptUpdates[9]));
        reportList.get(n).setNote1(rptUpdates[10]);
        reportList.get(n).setCostStatus(costStatus);
        inAndOut.saveReportToCSV(reportList,filePath);
    }

    public void addNewRpt(String myid,String filePath,String[] rptUpdates,String costStatus){
        List<Report> reportList=new ArrayList<>();
        Report report=new Report(myid,rptUpdates[0],rptUpdates[1],
                Float.parseFloat(rptUpdates[2].isEmpty()?"0":rptUpdates[2]),
                Float.parseFloat(rptUpdates[3].isEmpty()?"0":rptUpdates[3]),
                rptUpdates[4],Float.parseFloat(rptUpdates[5].isEmpty()?"0":rptUpdates[5]),rptUpdates[6],
                rptUpdates[7],Float.parseFloat(rptUpdates[8].isEmpty()?"0":rptUpdates[8]),
                Float.parseFloat(rptUpdates[9].isEmpty()?"0":rptUpdates[9]),rptUpdates[10],costStatus);
        if(new File(filePath).exists()){reportList=inAndOut.readReportFromCSV(filePath);}
        reportList.add(report);
        for(int t=0;t<=reportList.size()-1;t++){
            reportList.get(t).setId(Integer.toString(t+1));
        }
        inAndOut.saveReportToCSV(reportList,filePath);
    }
    public void delById(String myid,String filePath){
        List<Report> reportList=inAndOut.readReportFromCSV(filePath);
        int n=Integer.parseInt(myid);
        if(reportList.size()==1){
            reportList=new ArrayList<>();
        }else if(n==reportList.size()){
            reportList.remove(n-1);
        }else {
            reportList.remove(n-1);
            for(int t=0;t<=reportList.size()-1;t++){
                reportList.get(t).setId(Integer.toString(t+1));
            }
        }
        inAndOut.saveReportToCSV(reportList,filePath);
    }
}
