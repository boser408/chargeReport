package com.mangrove.chargereport.entity;

public class SimpleRateTemp {
    private String item;
    private String Type;
    private float ratePerUOM;
    private String uom;
    private float baseCharge;
    private float chargeCap;
    private String currency;

    public SimpleRateTemp() {
    }

    public SimpleRateTemp(String item, String type, float ratePerUOM, String uom, float baseCharge, float chargeCap, String currency) {
        this.item = item;
        Type = type;
        this.ratePerUOM = ratePerUOM;
        this.uom = uom;
        this.baseCharge = baseCharge;
        this.chargeCap = chargeCap;
        this.currency = currency;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public float getRatePerUOM() {
        return ratePerUOM;
    }

    public void setRatePerUOM(float ratePerUOM) {
        this.ratePerUOM = ratePerUOM;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public float getBaseCharge() {
        return baseCharge;
    }

    public void setBaseCharge(float baseCharge) {
        this.baseCharge = baseCharge;
    }

    public float getChargeCap() {
        return chargeCap;
    }

    public void setChargeCap(float chargeCap) {
        this.chargeCap = chargeCap;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "SimpleRateTemp{" +
                "item='" + item + '\'' +
                ", Type='" + Type + '\'' +
                ", ratePerUOM=" + ratePerUOM +
                ", uom='" + uom + '\'' +
                ", baseCharge=" + baseCharge +
                ", chargeCap=" + chargeCap +
                ", currency='" + currency + '\'' +
                '}';
    }
    public String toCSV(){
        return item + "," + Type + "," + ratePerUOM + "," + uom + "," + baseCharge + "," + chargeCap + "," + currency;
    }
}
