package com.mangrove.chargereport;

import com.mangrove.chargereport.entity.IdName;
import com.mangrove.chargereport.entity.Report;
import com.mangrove.chargereport.entity.SimpleRateTemp;
import com.mangrove.chargereport.tools.InAndOut;
import com.mangrove.chargereport.tools.InAndOutImp;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static com.mangrove.chargereport.genManage.CardConstant.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChargeReportApplicationTests {
    InAndOut inAndOut=new InAndOutImp();

    @Test
    void contextLoads() {
    }

    /*@Test
    void basiaFolder() {
        String chargeTempPath=dataPathPrex+"Template"+sep+"lspChargeRates0616.csv";
        List<SimpleRateTemp> chargeRateTemp=inAndOut.readSimpleRateFromCSV(chargeTempPath);
        List<IdName> lsplist=inAndOut.readIdNameFromCSV(lsplistPath);
        List<IdName> ctnrlist=new ArrayList<>();
        List<Report> reportList=new ArrayList<>();
        reportList.add(new Report("1"," "," ",0,0," ",
                0," "," ",0,0," "," "));
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
               inAndOut.saveIdNameToCSV(ctnrlist,filePath+sep+"ctnrlist-"+lspName+".csv");
               inAndOut.saveSimpleRateToCSV(chargeRateTemp,filePath+sep+"chargeRate-"+lspName+".csv");
               inAndOut.saveReportToCSV(reportList,pRptPath);
               inAndOut.saveReportToCSV(reportList,submitPath);
               inAndOut.saveReportToCSV(reportList,finalReportPath);
            }
        }
    }*/

}
