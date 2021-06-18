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
    public String getLspName(){
      return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public String getctnrlistPath(String lspName){
        return dataPathPrex+"Dray"+sep+lspName+sep+"ctnrlist-"+lspName+".csv";
    }
    public String getChargePath(String lspName){
        return dataPathPrex+"Dray"+sep+lspName+sep+"chargeRate-"+lspName+".csv";
    }
    public String getPendingPath(String lspName){
        return dataPathPrex+"Dray"+sep+lspName+sep+"pending-"+lspName+".csv";
    }
    public String getSubmitPath(String lspName){
        return dataPathPrex+"Dray"+sep+lspName+sep+"submitted-"+lspName+".csv";
    }
    public String getFinalPath(String lspName){
        return dataPathPrex+"Dray"+sep+lspName+sep+"approved-"+lspName+".csv";
    }

    @RequestMapping("/drayrpt/ctnrlist")
    public String ctnrlist(Model model){
        model.addAttribute("ctnrlist",inAndOut.readIdNameFromCSV(getctnrlistPath(getLspName())));
        return "drayctnrlist";
    }
    @RequestMapping("/drayrpt/issuelist")
    public String issuelist(Model model){
        model.addAttribute("reports",inAndOut.readReportFromCSV(getPendingPath(getLspName())));
        model.addAttribute("confreports",inAndOut.readReportFromCSV(getSubmitPath(getLspName())));
        model.addAttribute("adhocFinals",inAndOut.readReportFromCSV(getFinalPath(getLspName())));
        return "drayissuelist";
    }

    @RequestMapping("/drayrpt/del")
    public String drayrptDel(@RequestParam("delID") String delID,Model model){
        List<Report> reportList=inAndOut.readReportFromCSV(getPendingPath(getLspName()));
        reportList.removeIf(report -> report.getId().equals(delID));
        inAndOut.saveReportToCSV(reportList,getPendingPath(getLspName()));
        model.addAttribute("reports",inAndOut.readReportFromCSV(getPendingPath(getLspName())));
        model.addAttribute("confreports",inAndOut.readReportFromCSV(getSubmitPath(getLspName())));
        model.addAttribute("adhocFinals",inAndOut.readReportFromCSV(getFinalPath(getLspName())));
        return "drayissuelist";
    }

    @RequestMapping("/drayrpt/Edit")
    public String drayreportEdit(@RequestParam("myid")String myid, Model model){
        List<Report> reportList=inAndOut.readReportFromCSV(getPendingPath(getLspName()));
        for (Report report:reportList){
            if(report.getId().equals(myid)){
                model.addAttribute("report",report);
                break;
            }
        }
        model.addAttribute("ctnrlist",inAndOut.readIdNameFromCSV(getctnrlistPath(getLspName())));
        model.addAttribute("typelist",inAndOut.readSimpleRateFromCSV(getChargePath(getLspName())));
        return "drayreportEdit";
    }

    @RequestMapping("/drayrpt/Add")
    public String drayreportAdd(Model model){
        int allRecords=inAndOut.readReportFromCSV(getPendingPath(getLspName())).size()
                +inAndOut.readReportFromCSV(getSubmitPath(getLspName())).size()
                +inAndOut.readReportFromCSV(getFinalPath(getLspName())).size()+1;
        model.addAttribute("id",allRecords);
        model.addAttribute("ctnrlist",inAndOut.readIdNameFromCSV(getctnrlistPath(getLspName())));
        model.addAttribute("typelist",inAndOut.readSimpleRateFromCSV(getChargePath(getLspName())));
        return "drayreportAdd";
    }

    @RequestMapping("/drayrpt/saveUpdate")
    public String drayrptUpdate(@RequestParam("rptUpdates") String[] rptUpdates,Model model){
        Report report=createNewRpt(rptUpdates);
        if(rptUpdates[12].equals("Estimated")){
            changeRpt(getPendingPath(getLspName()),rptUpdates);
        }else {
            addNewRpt(getSubmitPath(getLspName()),report);
            Report toAllsubmit=new Report(report);
            toAllsubmit.setId(getLspName()+"-"+report.getId());
            addNewRpt(allSubmitted,toAllsubmit);
        }
        model.addAttribute("reports",inAndOut.readReportFromCSV(getPendingPath(getLspName())));
        model.addAttribute("confreports",inAndOut.readReportFromCSV(getSubmitPath(getLspName())));
        model.addAttribute("adhocFinals",inAndOut.readReportFromCSV(getFinalPath(getLspName())));
        return "drayissuelist";
    }

    @RequestMapping("/drayrpt/saveNew")
    public String drayrptAddnew(@RequestParam("rptUpdates") String[] rptUpdates,Model model){
        Report report=createNewRpt(rptUpdates);
        if(rptUpdates[12].equals("Estimated")){
            addNewRpt(getPendingPath(getLspName()),report);
        }else {
            addNewRpt(getSubmitPath(getLspName()),report);
            Report toAllsubmit=new Report(report);
            toAllsubmit.setId(getLspName()+"-"+report.getId());
            addNewRpt(allSubmitted,report);
        }
        model.addAttribute("reports",inAndOut.readReportFromCSV(getPendingPath(getLspName())));
        model.addAttribute("confreports",inAndOut.readReportFromCSV(getSubmitPath(getLspName())));
        model.addAttribute("adhocFinals",inAndOut.readReportFromCSV(getFinalPath(getLspName())));
        return "drayissuelist";
    }

    @RequestMapping("/op/getChargelist")
    public String opdraycharges(Model model){
        model.addAttribute("submitted",inAndOut.readReportFromCSV(allSubmitted));
        model.addAttribute("approved",inAndOut.readReportFromCSV(allApproved));
        model.addAttribute("rejected",inAndOut.readReportFromCSV(allRejected));
        return "opChargelist";
    }

    @RequestMapping("/op/rejcharge")
    public String oprejcharges(@RequestParam("myid")String myid,Model model){
        String lspName=myid.split("-")[0];
        String id=myid.split("-")[1];
        List<Report> submittedCharges=inAndOut.readReportFromCSV(allSubmitted);
        List<Report> rejectedCharges=inAndOut.readReportFromCSV(allRejected);
        for (Report report:submittedCharges){
            if (report.getId().equals(myid)){
                rejectedCharges.add(report);
                inAndOut.saveReportToCSV(rejectedCharges,allRejected);

                Report rejectedReport=new Report(report);
                rejectedReport.setId(id);
                rejectedReport.setCostStatus("rejected");
                List<Report> lspPendings=inAndOut.readReportFromCSV(getPendingPath(lspName));
                lspPendings.add(rejectedReport);
                inAndOut.saveReportToCSV(lspPendings,getPendingPath(lspName));
                submittedCharges.remove(report);
                inAndOut.saveReportToCSV(submittedCharges,allSubmitted);
                break;
            }
        }
        rmSubmittedReport(id,lspName);
        model.addAttribute("submitted",inAndOut.readReportFromCSV(allSubmitted));
        model.addAttribute("approved",inAndOut.readReportFromCSV(allApproved));
        model.addAttribute("rejected",inAndOut.readReportFromCSV(allRejected));
        return "opChargelist";
    }
    @RequestMapping("/op/aprcharge")
    public String opaprcharges(@RequestParam("myid")String myid,Model model){
        String lspName=myid.split("-")[0];
        String id=myid.split("-")[1];
        List<Report> submittedCharges=inAndOut.readReportFromCSV(allSubmitted);
        List<Report> approvedCharges=inAndOut.readReportFromCSV(allApproved);
        for (Report report:submittedCharges){
            if (report.getId().equals(myid)){
                approvedCharges.add(report);
                inAndOut.saveReportToCSV(approvedCharges,allApproved);

                Report approvedReport=new Report(report);
                approvedReport.setId(id);
                approvedReport.setCostStatus("appoved");
                List<Report> lspApproves=inAndOut.readReportFromCSV(getFinalPath(lspName));
                lspApproves.add(approvedReport);
                inAndOut.saveReportToCSV(lspApproves,getFinalPath(lspName));

                break;
            }
        }
        rmSubmittedReport(id,lspName);
        model.addAttribute("submitted",inAndOut.readReportFromCSV(allSubmitted));
        model.addAttribute("approved",inAndOut.readReportFromCSV(allApproved));
        model.addAttribute("rejected",inAndOut.readReportFromCSV(allRejected));
        return "opChargelist";
    }
    @RequestMapping("/drayrpt/download")
    public void fileDownLoad(@RequestParam("chargeStatus") String chargeStatus,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        String downloadPath;
        String downloadName;
        if(chargeStatus.equals("Estimated")){
            downloadPath=getPendingPath(getLspName());
            downloadName="EstimatedDrayCharge.csv";
        }else {
            downloadPath=getSubmitPath(getLspName());
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
    public void changeRpt(String filePath,String[] rptUpdates){
        List<Report> reportList=inAndOut.readReportFromCSV(filePath);
        int n;
        for(n=0;n<reportList.size();n++){
            if(reportList.get(n).getId().equals(rptUpdates[0])){
                reportList.get(n).setId(rptUpdates[0]);
                reportList.get(n).setRefID(rptUpdates[1]);
                reportList.get(n).setCategory(rptUpdates[2]);
                reportList.get(n).setRateperuom(Float.parseFloat(rptUpdates[3].isEmpty()?"0":rptUpdates[3]));
                reportList.get(n).setQty(Float.parseFloat(rptUpdates[4].isEmpty()?"0":rptUpdates[4]));
                reportList.get(n).setUom(rptUpdates[5]);
                reportList.get(n).setCharge(Float.parseFloat(rptUpdates[6].isEmpty()?"0":rptUpdates[6]));
                reportList.get(n).setStartTime(rptUpdates[7]);
                reportList.get(n).setEndTime(rptUpdates[8]);
                reportList.get(n).setCostAdjust(Float.parseFloat(rptUpdates[9].isEmpty()?"0":rptUpdates[9]));
                reportList.get(n).setTotal(Float.parseFloat(rptUpdates[10].isEmpty()?"0":rptUpdates[10]));
                reportList.get(n).setNote1(rptUpdates[11]);
                reportList.get(n).setCostStatus(rptUpdates[12]);
                break;
            }
        }
        inAndOut.saveReportToCSV(reportList,filePath);
    }
    public Report createNewRpt(String[] rptUpdates){
        return new Report(rptUpdates[0],rptUpdates[1],rptUpdates[2],
                Float.parseFloat(rptUpdates[3].isEmpty()?"0":rptUpdates[3]),
                Float.parseFloat(rptUpdates[4].isEmpty()?"0":rptUpdates[4]),
                rptUpdates[5],Float.parseFloat(rptUpdates[6].isEmpty()?"0":rptUpdates[6]),rptUpdates[7],
                rptUpdates[8],Float.parseFloat(rptUpdates[9].isEmpty()?"0":rptUpdates[9]),
                Float.parseFloat(rptUpdates[10].isEmpty()?"0":rptUpdates[10]),rptUpdates[11],rptUpdates[12]);
    }
    public void addNewRpt(String filePath,Report report){
        List<Report> reportList=inAndOut.readReportFromCSV(filePath);
        reportList.add(report);
        inAndOut.saveReportToCSV(reportList,filePath);
    }
    public void rmSubmittedReport(String id,String lspName){
        List<Report> lspSubmitCharges=inAndOut.readReportFromCSV(getSubmitPath(lspName));
        lspSubmitCharges.removeIf(report -> report.getId().equals(id));
        inAndOut.saveReportToCSV(lspSubmitCharges,getSubmitPath(lspName));
    }
}
