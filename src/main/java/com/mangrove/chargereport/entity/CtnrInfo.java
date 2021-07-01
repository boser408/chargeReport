package com.mangrove.chargereport.entity;

public class CtnrInfo {
    private String ctnrId;
    private String mbl;
    private String ctnrType;
    private float ctnrWeight;
    private String portETA;
    private String demLFD;
    private String outGate;
    private String delivery;
    private String empty;
    private String emptyReturn;
    private String perDiemLFD;
    private String customer;
    private String notes;

    public CtnrInfo(String ctnrId, String mbl, String ctnrType, float ctnrWeight, String portETA, String demLFD
            , String outGate, String delivery, String empty, String emptyReturn, String perDiemLFD, String customer, String notes) {
        this.ctnrId = ctnrId;
        this.mbl = mbl;
        this.ctnrType = ctnrType;
        this.ctnrWeight = ctnrWeight;
        this.portETA = portETA;
        this.demLFD = demLFD;
        this.outGate = outGate;
        this.delivery = delivery;
        this.empty = empty;
        this.emptyReturn = emptyReturn;
        this.perDiemLFD = perDiemLFD;
        this.customer = customer;
        this.notes = notes;
    }

    public String getCtnrId() {
        return ctnrId;
    }

    public void setCtnrId(String ctnrId) {
        this.ctnrId = ctnrId;
    }

    public String getMbl() {
        return mbl;
    }

    public void setMbl(String mbl) {
        this.mbl = mbl;
    }

    public String getCtnrType() {
        return ctnrType;
    }

    public void setCtnrType(String ctnrType) {
        this.ctnrType = ctnrType;
    }

    public float getCtnrWeight() {
        return ctnrWeight;
    }

    public void setCtnrWeight(float ctnrWeight) {
        this.ctnrWeight = ctnrWeight;
    }

    public String getPortETA() {
        return portETA;
    }

    public void setPortETA(String portETA) {
        this.portETA = portETA;
    }

    public String getDemLFD() {
        return demLFD;
    }

    public void setDemLFD(String demLFD) {
        this.demLFD = demLFD;
    }

    public String getOutGate() {
        return outGate;
    }

    public void setOutGate(String outGate) {
        this.outGate = outGate;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getEmpty() {
        return empty;
    }

    public void setEmpty(String empty) {
        this.empty = empty;
    }

    public String getEmptyReturn() {
        return emptyReturn;
    }

    public void setEmptyReturn(String emptyReturn) {
        this.emptyReturn = emptyReturn;
    }

    public String getPerDiemLFD() {
        return perDiemLFD;
    }

    public void setPerDiemLFD(String perDiemLFD) {
        this.perDiemLFD = perDiemLFD;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "CtnrInfo{" +
                "ctnrId='" + ctnrId + '\'' +
                ", mbl='" + mbl + '\'' +
                ", ctnrType='" + ctnrType + '\'' +
                ", ctnrWeight=" + ctnrWeight +
                ", portETA='" + portETA + '\'' +
                ", demLFD='" + demLFD + '\'' +
                ", outGate='" + outGate + '\'' +
                ", delivery='" + delivery + '\'' +
                ", empty='" + empty + '\'' +
                ", emptyReturn='" + emptyReturn + '\'' +
                ", perDiemLFD='" + perDiemLFD + '\'' +
                ", customer='" + customer + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public String toCSV(){
        return ctnrId + "," + mbl+"," +ctnrType + "," + ctnrWeight+ "," + portETA +","+ demLFD +","+ outGate
                +","+ delivery +","+ empty +","+emptyReturn+","+ perDiemLFD +","+ customer +","+ notes;
    }
}
