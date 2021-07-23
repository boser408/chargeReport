package com.mangrove.chargereport.tools;

import com.mangrove.chargereport.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;


public class InAndOutImp implements InAndOut{

    public <T> void saveToCSV(List<T> tList,String capital,String savePath) {
        try{
            FileOutputStream fos=new FileOutputStream(savePath);
            fos.write(0xef);
            fos.write(0xbb);
            fos.write(0xbf);
            OutputStreamWriter osw=new OutputStreamWriter(fos,UTF_8);
            BufferedWriter out = new BufferedWriter(osw);
            out.write(capital);
            out.newLine();
            for(T t:tList){
                out.write(t.toString());
                out.newLine();
            }
            out.flush();
            osw.flush();
            fos.flush();
            out.close();
            osw.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public List<SimpleRateTemp> readSimpleRateFromCSV(String readPath) {
        List<SimpleRateTemp> simpleRateTempList=new ArrayList<>();
        try{
            File filename = new File(readPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null){
                String[] pricebar=line.split(cvsSplitBy);
                SimpleRateTemp simpleRateTemp=new SimpleRateTemp();
                simpleRateTemp.setItem(pricebar[0]);
                simpleRateTemp.setType(pricebar[1]);
                simpleRateTemp.setRatePerUOM(Float.parseFloat(pricebar[2]));
                simpleRateTemp.setUom(pricebar[3]);
                simpleRateTemp.setBaseCharge(Float.parseFloat(pricebar[4]));
                simpleRateTemp.setChargeCap(Float.parseFloat(pricebar[5]));
                simpleRateTemp.setCurrency(pricebar[6]);
                simpleRateTempList.add(simpleRateTemp);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return simpleRateTempList;
    }
    @Override
    public void saveSimpleRateToCSV(List<SimpleRateTemp> simpleRateTempList, String savePath) {
        String capital="Item"+","+"Type"+","+"RatePerUOM"+","+"UOM"+","+"BaseCharge"+","+"ChargeCap"+","+"Currency";
        saveToCSV(simpleRateTempList,capital,savePath);
    }
    @Override
    public List<IdName> readIdNameFromCSV(String readPath) {
        List<IdName> idNameList=new ArrayList<>();
        try {
            File filename = new File(readPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                IdName idName=new IdName();
                idName.setId(Integer.parseInt(pricebar[0]));
                idName.setName(pricebar[1]);
                idNameList.add(idName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idNameList;
    }
    @Override
    public void saveIdNameToCSV(List<IdName> idNameList, String savePath) {
        String capital="Id"+","+"Name";
        saveToCSV(idNameList,capital,savePath);
    }
    @Override
    public List<Report> readReportFromCSV(String readPath) {
        List<Report> reportList=new ArrayList<>();
        try {
            File filename = new File(readPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                Report report=new Report(pricebar[0],pricebar[1],pricebar[2],Float.parseFloat(pricebar[3]),Float.parseFloat(pricebar[4]),
                        pricebar[5],Float.parseFloat(pricebar[6]),pricebar[7],pricebar[8],Float.parseFloat(pricebar[9]),Float.parseFloat(pricebar[10]),pricebar[11],pricebar[12]);
                reportList.add(report);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportList;
    }
    @Override
    public void saveReportToCSV(List<Report> reportList, String savePath) {
        String capital="ID"+","+"refID"+","+"category"+","+"rateperuom"+","+"qty"+","+"uom"+","+"charge"+","+"startTime"+
                ","+"endTime"+","+"costAdjust"+","+"total"+","+"note1"+","+"costStatus";
        saveToCSV(reportList,capital,savePath);
    }
    @Override
    public List<CtnrInfo> readCtnrInfoFromCSV(String readPath) {
        List<CtnrInfo> ctnrInfoList=new ArrayList<>();
        try {
            File filename = new File(readPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                CtnrInfo ctnrInfo=new CtnrInfo(pricebar[0],pricebar[1],pricebar[2],Float.parseFloat(pricebar[3]),pricebar[4],pricebar[5]
                        ,pricebar[6],pricebar[7],pricebar[8],pricebar[9],pricebar[10],pricebar[11],pricebar[12]);
                ctnrInfoList.add(ctnrInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctnrInfoList;
    }
    @Override
    public void saveCtnrInfoToCSV(List<CtnrInfo> ctnrInfoList, String savePath) {
        String capital="ctnrId"+","+"mbl"+","+"ctnrType"+","+"ctnrWeight"+","+"portETA"+","+"demLFD"+","+"outGate"
                +","+"delivery"+","+"empty"+","+"emptyReturn"+","+"perDiemLFD"+","+"customer"+","+"notes";
        saveToCSV(ctnrInfoList,capital,savePath);
    }
    @Override
    public List<ChargeRecord> readChargeRecordFromCSV(String readPath) {
        List<ChargeRecord> chargeRecordList=new ArrayList<>();
        try {
            File filename = new File(readPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                ChargeRecord chargeRecord=new ChargeRecord(pricebar[0],pricebar[1],pricebar[2],pricebar[3],pricebar[4]
                        ,Float.parseFloat(pricebar[5]),Float.parseFloat(pricebar[6]),pricebar[7]
                        ,Float.parseFloat(pricebar[8]),pricebar[9],pricebar[10],Float.parseFloat(pricebar[11])
                        ,Float.parseFloat(pricebar[12]),pricebar[13],pricebar[14],pricebar[15]);
                chargeRecordList.add(chargeRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeRecordList;
    }
    @Override
    public List<ChargeRecord> importChargeRecordFromMPF(MultipartFile csvFile) {
        List<ChargeRecord> chargeRecordList=new ArrayList<>();
        try {
            InputStreamReader reader = new InputStreamReader(csvFile.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                ChargeRecord chargeRecord=new ChargeRecord(pricebar[0],pricebar[1],pricebar[2],pricebar[3],pricebar[4]
                        ,Float.parseFloat(pricebar[5]),Float.parseFloat(pricebar[6]),pricebar[7]
                        ,Float.parseFloat(pricebar[8]),pricebar[9],pricebar[10],Float.parseFloat(pricebar[11])
                        ,Float.parseFloat(pricebar[12]),pricebar[13],pricebar[14],pricebar[15]);
                chargeRecordList.add(chargeRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeRecordList;
    }
    @Override
    public void saveChargeRecordToCSV(List<ChargeRecord> chargeRecordList, String savePath) {
        String capital="ID"+","+"refID"+","+"Leg"+","+"category"+","+"CN"+","+"rateperuom"+","+"qty"+","+"uom"+","+"charge"+","+"startTime"+
                ","+"endTime"+","+"costAdjust"+","+"total"+","+"note1"+","+"costStatus"+","+"CSOPcomments";
        saveToCSV(chargeRecordList,capital,savePath);
    }
    @Override
    public List<LSPupload> readLSPuploadFromCSV(String readPath) {
        List<LSPupload> lsPuploadList=new ArrayList<>();
        try {
            File filename = new File(readPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                LSPupload lsPupload=new LSPupload(pricebar[0],pricebar[1],pricebar[2],pricebar[3]
                        ,Float.parseFloat(pricebar[4]),Float.parseFloat(pricebar[5]),pricebar[6]
                        ,Float.parseFloat(pricebar[7]),pricebar[8],pricebar[9],Float.parseFloat(pricebar[10])
                        ,Float.parseFloat(pricebar[11]),pricebar[12],pricebar[13]);
                lsPuploadList.add(lsPupload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lsPuploadList;
    }
    @Override
    public List<LSPupload> importLSPuploadFromMPF(MultipartFile csvFile) {
        List<LSPupload> lsPuploadList=new ArrayList<>();
        try {
            InputStreamReader reader = new InputStreamReader(csvFile.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                LSPupload lsPupload=new LSPupload(pricebar[0],pricebar[1],pricebar[2],pricebar[3]
                        ,Float.parseFloat(pricebar[4]),Float.parseFloat(pricebar[5]),pricebar[6]
                        ,Float.parseFloat(pricebar[7]),pricebar[8],pricebar[9],Float.parseFloat(pricebar[10])
                        ,Float.parseFloat(pricebar[11]),pricebar[12],pricebar[13]);
                lsPuploadList.add(lsPupload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lsPuploadList;
    }
    @Override
    public void saveLSPuploadToCSV(List<LSPupload> lsPuploadList, String savePath) {
        String capital="refID"+","+"Leg"+","+"category"+","+"CN"+","+"rateperuom"+","+"qty"+","+"uom"+","+"charge"+","+"startTime"+
                ","+"endTime"+","+"costAdjust"+","+"total"+","+"note1"+","+"costStatus";
        saveToCSV(lsPuploadList,capital,savePath);
    }
    private void readDrayRate(List<DrayRate> drayRateList, BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine())!= null) {
            String[] pricebar=line.split(",");
            DrayRate drayRate=new DrayRate(pricebar[0],pricebar[1],pricebar[2],Float.parseFloat(pricebar[3])
                    ,Float.parseFloat(pricebar[4]),pricebar[5],pricebar[6]);
            drayRateList.add(drayRate);
        }
    }

    @Override
    public List<DrayRate> readDrayRateFromCSV(String readPath) {
        List<DrayRate> drayRateList=new ArrayList<>();
        try {
            File filename = new File(readPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            readDrayRate(drayRateList, br);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drayRateList;
    }

    @Override
    public void saveDrayRateToCSV(List<DrayRate> drayRateList, String savePath) {
        String capital="Item"+","+"c1"+","+"c2"+","+"RatePerUOM"+","+"Adjust"+","+"UOM"+","+"CN";
        saveToCSV(drayRateList,capital,savePath);
    }

    @Override
    public List<NameAttr> readNameAttrFromCSV(String readPath) {
        List<NameAttr> nameAttrList=new ArrayList<>();
        try {
            File filename = new File(readPath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line ;
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                NameAttr nameAttr=new NameAttr();
                nameAttr.setId(Integer.parseInt(pricebar[0]));
                nameAttr.setName(pricebar[1]);
                nameAttr.setAttr(pricebar[2]);
                nameAttrList.add(nameAttr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameAttrList;
    }

    @Override
    public void saveNameAttrToCSV(List<NameAttr> nameAttrList, String savePath) {
        String capital="Id"+","+"Name"+","+"Attr";
        saveToCSV(nameAttrList,capital,savePath);
    }
}
