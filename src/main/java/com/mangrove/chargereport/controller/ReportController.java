package com.mangrove.chargereport.controller;

import com.mangrove.chargereport.entity.ChargeRecord;
import com.mangrove.chargereport.entity.IdName;
import com.mangrove.chargereport.entity.LSPupload;
import com.mangrove.chargereport.entity.Report;
import com.mangrove.chargereport.tools.DownloadHandle;
import com.mangrove.chargereport.tools.InAndOut;
import com.mangrove.chargereport.tools.InAndOutImp;
import javafx.util.converter.LocalDateStringConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.mangrove.chargereport.genManage.CardConstant.*;

@Controller
public class ReportController {

    InAndOut inAndOut=new InAndOutImp();
    public String getLspName(){
      return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public String getctnrlistPath(String lspName){
        return dataPathPrex+"Template"+sep+"ctnrlist0616.csv";
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
    public String getTempRptPath(){
        return dataPathPrex+"Dray"+sep+getLspName()+sep+"Temp"+sep+"tempUploadReport.csv";
    }

    String loginName=getLspName();
    String pendingPath=dataPathPrex+"Dray"+sep+loginName+sep+"pending-"+loginName+".csv";
    String submitPath=dataPathPrex+"Dray"+sep+loginName+sep+"submitted-"+loginName+".csv";
    String approvedPath=dataPathPrex+"Dray"+sep+loginName+sep+"approved-"+loginName+".csv";
    String temRptPath=dataPathPrex+"Dray"+sep+loginName+sep+"Temp"+sep+"tempUploadReport.csv";

    @RequestMapping("/drayrpt/ctnrlist")
    public String ctnrlist(Model model){
        model.addAttribute("ctnrlist",inAndOut.readIdNameFromCSV(getctnrlistPath(getLspName())));
        return "drayctnrlist";
    }
    @RequestMapping("/drayrpt/ctnrAddblank")
    public String ctnrAdd(Model model){
        int newid=1;
        List<IdName> ctnrlist=inAndOut.readIdNameFromCSV(getctnrlistPath(getLspName()));
        if(ctnrlist.size()>1){
            newid=ctnrlist.stream().max(Comparator.comparingInt(IdName::getId)).get().getId()+1;
        }
        model.addAttribute("id",newid);
        return "ctnrAdd";
    }
    @RequestMapping("/drayrpt/ctnrcreate")
    public String ctnrcreate(@RequestParam("myid")String myid,
                             @RequestParam("ctnrid")String ctnrid,Model model){
        IdName idName=new IdName(Integer.parseInt(myid),ctnrid);
        List<IdName> ctnrlist=inAndOut.readIdNameFromCSV(getctnrlistPath(getLspName()));
        ctnrlist.add(idName);
        inAndOut.saveIdNameToCSV(ctnrlist,getctnrlistPath(getLspName()));
        model.addAttribute("ctnrlist",inAndOut.readIdNameFromCSV(getctnrlistPath(getLspName())));
        return "drayctnrlist";
    }
    @RequestMapping("/drayrpt/rcReview")
    public String rcReview(Model model){
        String drayRC=webRCpathPrex+"Dray"+sep+getLspName()+sep+"dray"+getLspName()+"LGB.csv";
        model.addAttribute("drayRC",inAndOut.readDrayRateFromCSV(drayRC));
        return "drayRCreview";
    }
    @RequestMapping("/drayrpt/issuelist")
    public String issuelist(Model model){
        model.addAttribute("reports",inAndOut.readReportFromCSV(getPendingPath(getLspName())));
        model.addAttribute("confreports",inAndOut.readReportFromCSV(getSubmitPath(getLspName())));
        model.addAttribute("adhocFinals",inAndOut.readReportFromCSV(getFinalPath(getLspName())));
        return "drayissuelist";
    }

    @RequestMapping("/drayrpt/reviewUpload")
    public String reviewUpload(@RequestParam("filePath") MultipartFile csvFile,Model model){

        List<LSPupload> lsPuploadList=inAndOut.importLSPuploadFromMPF(csvFile);
        int year=LocalDate.now().getYear();
        for(LSPupload lsPupload:lsPuploadList){
            lsPupload.setStartTime(year+"/"+lsPupload.getStartTime());
            lsPupload.setEndTime(year+"/"+lsPupload.getEndTime());
        }
        inAndOut.saveLSPuploadToCSV(lsPuploadList,temRptPath);
        model.addAttribute("lsPuploads",lsPuploadList);
        return "drayReviewUpload";
    }
    @RequestMapping("/drayrpt/rptsubmit")
    public String handelRptSubmit(Model model){
        List<LSPupload> lsPuploadList=inAndOut.readLSPuploadFromCSV(temRptPath);
        List<ChargeRecord> pendingList=inAndOut.readChargeRecordFromCSV(pendingPath);
        List<ChargeRecord> submitList=inAndOut.readChargeRecordFromCSV(submitPath);
        List<ChargeRecord> approvedList=inAndOut.readChargeRecordFromCSV(approvedPath);
        int maxID=getMaxId(pendingList,submitList,approvedList);
        for (LSPupload lsPupload:lsPuploadList){
            maxID++;
            ChargeRecord chargeRecord=new ChargeRecord(lsPupload);
            chargeRecord.setId(String.valueOf(maxID));
            if(lsPupload.getCostStatus().equals("Confirmed")){
                submitList.add(chargeRecord);
            }else {
                pendingList.add(chargeRecord);
            }
        }
        inAndOut.saveChargeRecordToCSV(pendingList,pendingPath);
        inAndOut.saveChargeRecordToCSV(submitList,submitPath);
        inAndOut.saveChargeRecordToCSV(approvedList,approvedPath);
        model.addAttribute("reports",pendingList);
        model.addAttribute("confreports",submitList);
        model.addAttribute("adhocFinals",approvedList);
        return "drayissuelist";
    }
    @RequestMapping("/drayrpt/del")
    public String drayrptDel(@RequestParam("delID") String delID,Model model){
        delRptById(getPendingPath(getLspName()),delID);
        model.addAttribute("reports",inAndOut.readChargeRecordFromCSV(pendingPath));
        model.addAttribute("confreports",inAndOut.readChargeRecordFromCSV(submitPath));
        model.addAttribute("adhocFinals",inAndOut.readChargeRecordFromCSV(approvedPath));
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
            String newId=getLspName()+"-"+report.getId();
            toAllsubmit.setId(newId);
            System.out.println("newId is---------"+newId);
            addNewRpt(allSubmitted,toAllsubmit);
            delRptById(getPendingPath(getLspName()),rptUpdates[0]);
        }
        model.addAttribute("reports",inAndOut.readChargeRecordFromCSV(pendingPath));
        model.addAttribute("confreports",inAndOut.readChargeRecordFromCSV(submitPath));
        model.addAttribute("adhocFinals",inAndOut.readChargeRecordFromCSV(approvedPath));
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
            String newId=getLspName()+"-"+report.getId();
            toAllsubmit.setId(newId);
            System.out.println("newId is---------"+newId);
            addNewRpt(allSubmitted,toAllsubmit);
        }
        model.addAttribute("reports",inAndOut.readChargeRecordFromCSV(pendingPath));
        model.addAttribute("confreports",inAndOut.readChargeRecordFromCSV(submitPath));
        model.addAttribute("adhocFinals",inAndOut.readChargeRecordFromCSV(approvedPath));
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
        //rmSubmittedReport(id,lspName);
        delRptById(getSubmitPath(lspName),id);
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

                submittedCharges.remove(report);
                inAndOut.saveReportToCSV(submittedCharges,allSubmitted);
                break;
            }
        }
        //rmSubmittedReport(id,lspName);
        delRptById(getSubmitPath(lspName),id);
        model.addAttribute("submitted",inAndOut.readReportFromCSV(allSubmitted));
        model.addAttribute("approved",inAndOut.readReportFromCSV(allApproved));
        model.addAttribute("rejected",inAndOut.readReportFromCSV(allRejected));
        return "opChargelist";
    }
    @RequestMapping("/drayrpt/ctnrdownload")
    public void ctnrDownLoad(HttpServletResponse response) {
        List<IdName> idNameList=inAndOut.readIdNameFromCSV(getctnrlistPath(getLspName()));
        String downloadName="containerList.csv";
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((downloadName).getBytes("UTF-8"), "UTF-8"));
            ServletOutputStream out = response.getOutputStream();
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            String capital="ID"+","+"CtnrID\n";
            out.write(capital.getBytes("UTF-8"));
            for(IdName idName:idNameList){
                out.write((idName.toString()+"\n").getBytes("UTF-8"));
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/lspuploadTempDl")
    public void lspUploadTempdl(HttpServletResponse response){
        String downloadname="lspuploadTemp.csv";
        String capital="refID"+","+"Leg"+","+"category"+","+"CN"+","+"rateperuom"+","+"qty"+","+"uom"+","+"charge"+","+"startTime"+
                ","+"endTime"+","+"costAdjust"+","+"total"+","+"note1"+","+"costStatus\n";
        new DownloadHandle(response).dlHandle(downloadname,capital
                ,inAndOut.readLSPuploadFromCSV(dataPathPrex+"Template"+sep+"LSPuploadTemp.csv"));
    }
    @RequestMapping("/drayrpt/download")
    public void fileDownLoad(@RequestParam("chargeStatus") String chargeStatus,HttpServletResponse response) {
        List<Report> reportList;
        String lspName=getLspName();
        String lspFolderPath=dataPathPrex+"Dray"+sep+lspName+sep;
        String downloadName=chargeStatus+"-"+lspName+".csv";
        if(chargeStatus.equals("All")){
            reportList=inAndOut.readReportFromCSV(lspFolderPath+"pending"+"-"+lspName+".csv");
            reportList.addAll(inAndOut.readReportFromCSV(lspFolderPath+"submitted"+"-"+lspName+".csv"));
            reportList.addAll(inAndOut.readReportFromCSV(lspFolderPath+"approved"+"-"+lspName+".csv"));
        }else {
            String downloadPath=lspFolderPath+downloadName;
            reportList=inAndOut.readReportFromCSV(downloadPath);
        }
        downloadHandle(response, downloadName, reportList);
    }
    @RequestMapping("/op/csopdownload")
    public void csopRptDownLoad(@RequestParam("chargeStatus") String chargeStatus,HttpServletResponse response) {
        List<Report> reportList;
        String downloadName=chargeStatus+"-All.csv";
        if(chargeStatus.equals("submitted")){
            reportList=inAndOut.readReportFromCSV(allSubmitted);
        }else if(chargeStatus.equals("approved")){
            reportList=inAndOut.readReportFromCSV(allApproved);
        }else {
            reportList=inAndOut.readReportFromCSV(allRejected);
        }
        downloadHandle(response, downloadName, reportList);
    }

    public void downloadHandle(HttpServletResponse response, String downloadName,List<Report> reportList) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/hmtl;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((downloadName).getBytes("UTF-8"), "UTF-8"));
            ServletOutputStream out = response.getOutputStream();
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            String capital="ID"+","+"refID"+","+"category"+","+"rateperuom"+","+"qty"+","+"uom"+","+"charge"+","+"startTime"+
                    ","+"endTime"+","+"costAdjust"+","+"total" +","+"note1"+","+"costStatus\n";
            out.write(capital.getBytes("UTF-8"));
            for(Report report:reportList){
                out.write((report.toString()+"\n").getBytes("UTF-8"));
            }
            out.close();
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
    public void delRptById(String filePath,String delID){
        List<Report> reportList=inAndOut.readReportFromCSV(filePath);
        reportList.removeIf(report -> report.getId().equals(delID));
        inAndOut.saveReportToCSV(reportList,filePath);
    }
    public void addNewRpt(String filePath,Report report){
        List<Report> reportList=inAndOut.readReportFromCSV(filePath);
        reportList.add(report);
        inAndOut.saveReportToCSV(reportList,filePath);
    }
    public int getMaxId(List<ChargeRecord> pendingList,List<ChargeRecord> submitList,List<ChargeRecord> approvedList){
        int nonZero=pendingList.size()*submitList.size()*approvedList.size();
        int maxId=1;
        if(nonZero>0){
            pendingList.sort(Comparator.comparing(ChargeRecord::getId));
            submitList.sort(Comparator.comparing(ChargeRecord::getId));
            approvedList.sort(Comparator.comparing(ChargeRecord::getId));
            ArrayList<String> strings = new ArrayList<String>();
            strings.add(pendingList.get(pendingList.size()-1).getId());
            strings.add(submitList.get(submitList.size()-1).getId());
            strings.add(approvedList.get(approvedList.size()-1).getId());
            Collections.sort(strings);
            maxId=Integer.parseInt(strings.get(2));
        }
        return maxId;
    }
}
