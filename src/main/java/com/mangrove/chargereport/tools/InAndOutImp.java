package com.mangrove.chargereport.tools;

import com.mangrove.chargereport.entity.IdName;
import com.mangrove.chargereport.entity.Report;
import com.mangrove.chargereport.entity.SimpleRateTemp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;


public class InAndOutImp implements InAndOut{


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
        try{
            FileOutputStream fos=new FileOutputStream(savePath);
            fos.write(0xef);
            fos.write(0xbb);
            fos.write(0xbf);
            OutputStreamWriter osw=new OutputStreamWriter(fos,UTF_8);
            BufferedWriter out = new BufferedWriter(osw);
            out.write("Item"+","+"Type"+","+"RatePerUOM"+","+"UOM"+","+"BaseCharge"+","+"ChargeCap"+"Currency");
            out.newLine();
            for(SimpleRateTemp simpleRateTemp:simpleRateTempList){
                out.write(simpleRateTemp.getItem()+","+simpleRateTemp.getType()+","+simpleRateTemp.getRatePerUOM()+","
                        +simpleRateTemp.getUom()+","+simpleRateTemp.getBaseCharge()+","+simpleRateTemp.getChargeCap()+","
                        +simpleRateTemp.getCurrency());
                out.newLine();
            }
            out.flush();
            osw.flush();
            fos.flush();
            out.close();
            osw.close();
            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
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
        try{
            FileOutputStream fos=new FileOutputStream(savePath);
            fos.write(0xef);
            fos.write(0xbb);
            fos.write(0xbf);
            OutputStreamWriter osw=new OutputStreamWriter(fos,UTF_8);
            BufferedWriter out = new BufferedWriter(osw);
            out.write("Id"+","+"Name");
            out.newLine();
            for(IdName idName:idNameList){
                out.write(idName.toCSV());
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
        try{
            FileOutputStream fos=new FileOutputStream(savePath);
            fos.write(0xef);
            fos.write(0xbb);
            fos.write(0xbf);
            OutputStreamWriter osw=new OutputStreamWriter(fos,UTF_8);
            BufferedWriter out = new BufferedWriter(osw);
            out.write("ID"+","+"refID"+","+"category"+","+"rateperuom"+","+"qty"+","+"uom"+","+"charge"+","+"startTime"+
                    ","+"endTime"+","+"costAdjust"+","+"total"+","+"note1"+","+"costStatus");
            out.newLine();
            for(Report report:reportList){
                out.write(report.toCSV());
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
}
