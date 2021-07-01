package com.mangrove.chargereport.tools;

import com.mangrove.chargereport.entity.CtnrInfo;
import com.mangrove.chargereport.entity.IdName;
import com.mangrove.chargereport.entity.Report;
import com.mangrove.chargereport.entity.SimpleRateTemp;

import java.util.List;


public interface InAndOut {
    List<SimpleRateTemp> readSimpleRateFromCSV(String readPath);
    void saveSimpleRateToCSV(List<SimpleRateTemp> simpleRateTempList, String savePath);
    List<IdName> readIdNameFromCSV(String readPath);
    void saveIdNameToCSV(List<IdName> idNameList, String savePath);
    List<Report> readReportFromCSV(String readPath);
    void saveReportToCSV(List<Report> reportList, String savePath);
    List<CtnrInfo> readCtnrInfoFromCSV(String readPath);
    void saveCtnrInfoToCSV(List<CtnrInfo> ctnrInfoList,String savePath);
}
