package com.mangrove.chargereport.tools;

import com.mangrove.chargereport.entity.*;
import org.springframework.web.multipart.MultipartFile;

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
    List<ChargeRecord> readChargeRecordFromCSV(String readPath);
    List<ChargeRecord> importChargeRecordFromMPF(MultipartFile csvFile);
    void saveChargeRecordToCSV(List<ChargeRecord> chargeRecordList,String savePath);
    List<LSPupload> readLSPuploadFromCSV(String readPath);
    List<LSPupload> importLSPuploadFromMPF(MultipartFile csvFile);
    void saveLSPuploadToCSV(List<LSPupload> lsPuploadList,String savePath);
    List<DrayRate> readDrayRateFromCSV(String readPath);
    void saveDrayRateToCSV(List<DrayRate> drayRateList,String savePath);
}
