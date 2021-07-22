package com.mangrove.chargereport;

import com.mangrove.chargereport.entity.*;
import com.mangrove.chargereport.tools.InAndOut;
import com.mangrove.chargereport.tools.InAndOutImp;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static com.mangrove.chargereport.genManage.CardConstant.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChargeReportApplicationTests {
    InAndOut inAndOut=new InAndOutImp();

    @Test
    void emptyListTest() {
      String testPath=dataPathPrex+"Template"+sep+"LSPuploadTemp.csv";
        int year= LocalDate.now().getYear();
      for (LSPupload lsPupload:inAndOut.readLSPuploadFromCSV(testPath)){
          if(lsPupload.getStartTime().contains("/")){
              lsPupload.setStartTime(year+"/"+lsPupload.getStartTime());
          }
          System.out.println(lsPupload.toString());
      }
    }

    @Test
    void chargeReportInit() {
        String chargeTempPath=dataPathPrex+"Template"+sep+"lspChargeRates0616.csv";
        List<SimpleRateTemp> chargeRateTemp=inAndOut.readSimpleRateFromCSV(chargeTempPath);
        List<IdName> lsplist=inAndOut.readIdNameFromCSV(lsplistPath);
        List<Report> reportList=new ArrayList<>();

        for(IdName idName:lsplist){
            String lspName=idName.getName();
            String filePath=dataPathPrex+"Dray"+sep+lspName;
            String pRptPath=dataPathPrex+"Dray"+sep+lspName+sep+"pending-"+lspName+".csv";
            String submitPath=dataPathPrex+"Dray"+sep+lspName+sep+"submitted-"+lspName+".csv";
            String finalReportPath=dataPathPrex+"Dray"+sep+lspName+sep+"approved-"+lspName+".csv";
            boolean a=true;
            File file=new File(filePath);
            if(!file.exists()){
               a=file.mkdir();
            }
            if(a){
               inAndOut.saveSimpleRateToCSV(chargeRateTemp,filePath+sep+"chargeRate-"+lspName+".csv");
               inAndOut.saveReportToCSV(reportList,pRptPath);
               inAndOut.saveReportToCSV(reportList,submitPath);
               inAndOut.saveReportToCSV(reportList,finalReportPath);
            }
        }
        inAndOut.saveReportToCSV(reportList,allApproved);
        inAndOut.saveReportToCSV(reportList,allRejected);
        inAndOut.saveReportToCSV(reportList,allSubmitted);
    }

    @Test
    void genManage(){
        List<ChargeRecord> chargeRecordList=new ArrayList<>();
        for(IdName idName:inAndOut.readIdNameFromCSV(lsplistPath)){
            String lspName=idName.getName();
            String filePath=dataPathPrex+"Dray"+sep+lspName;
            String pRptPath=dataPathPrex+"Dray"+sep+lspName+sep+"pending-"+lspName+".csv";
            String submitPath=dataPathPrex+"Dray"+sep+lspName+sep+"submitted-"+lspName+".csv";
            String finalReportPath=dataPathPrex+"Dray"+sep+lspName+sep+"approved-"+lspName+".csv";
            inAndOut.saveChargeRecordToCSV(chargeRecordList,pRptPath);
            inAndOut.saveChargeRecordToCSV(chargeRecordList,submitPath);
            inAndOut.saveChargeRecordToCSV(chargeRecordList,finalReportPath);
        }
        inAndOut.saveChargeRecordToCSV(chargeRecordList,allApproved);
        inAndOut.saveChargeRecordToCSV(chargeRecordList,allRejected);
        inAndOut.saveChargeRecordToCSV(chargeRecordList,allSubmitted);
    }
}
