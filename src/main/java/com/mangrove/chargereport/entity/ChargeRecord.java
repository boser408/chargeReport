package com.mangrove.chargereport.entity;

import lombok.Data;

@Data
public class ChargeRecord {
    private String id;
    private String refID;
    private String leg;
    private String category;
    private String cn;
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
    private String csopComments;

    public ChargeRecord() {
    }

    public ChargeRecord(String id, String refID, String leg, String category, String cn, float rateperuom, float qty
            , String uom, float charge, String startTime, String endTime, float costAdjust, float total, String note1
            , String costStatus, String csopComments) {
        this.id = id;
        this.refID = refID;
        this.leg = leg;
        this.category = category;
        this.cn = cn;
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
        this.csopComments = csopComments;
    }

    @Override
    public String toString() {
        return id+","+refID+","+leg+","+category+","+cn+","+rateperuom+","+qty+","+uom+","+charge+","+startTime
                +","+endTime+","+costAdjust+","+total+","+note1+","+costStatus+","+csopComments;
    }
}
