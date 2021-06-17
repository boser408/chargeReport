package com.mangrove.chargereport.entity;

public class Report {
    private String id;
    private String refID;
    private String category;
    private float rateperuom;
    private float qty;
    private String uom;
    private float charge;
    private String startTime;
    private String endTime;
    private float costAdjust;
    private float total;
    private String note1;
    private String costStatus;

    public Report() {
    }

    public Report(String id, String refID, String category, float rateperuom, float qty, String uom, float charge, String startTime, String endTime, float costAdjust, float total, String note1, String costStatus) {
        this.id = id;
        this.refID = refID;
        this.category = category;
        this.rateperuom = rateperuom;
        this.qty = qty;
        this.uom = uom;
        this.charge = charge;
        this.startTime = startTime;
        this.endTime = endTime;
        this.costAdjust = costAdjust;
        this.total = total;
        this.note1 = note1;
        this.costStatus = costStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getRateperuom() {
        return rateperuom;
    }

    public void setRateperuom(float rateperuom) {
        this.rateperuom = rateperuom;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public float getCostAdjust() {
        return costAdjust;
    }

    public void setCostAdjust(float costAdjust) {
        this.costAdjust = costAdjust;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public void setCostStatus(String costStatus) {
        this.costStatus = costStatus;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", refID='" + refID + '\'' +
                ", category='" + category + '\'' +
                ", rateperuom=" + rateperuom +
                ", qty=" + qty +
                ", uom='" + uom + '\'' +
                ", charge=" + charge +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", costAdjust=" + costAdjust +
                ", total=" + total +
                ", note1='" + note1 + '\'' +
                ", costStatus='" + costStatus + '\'' +
                '}';
    }

    public String toCSV() {
        return id+","+refID+","+category +","+rateperuom +","+ qty +","+uom+","+charge+","+startTime+","+endTime+","
                +costAdjust+","+total+","+note1+","+costStatus;
    }
}
