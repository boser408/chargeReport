package com.mangrove.chargereport.entity;

import lombok.Data;

@Data
public class DrayRate {
    private String item;
    private String c1;
    private String c2;
    private float ratePerUOM;
    private float adjust;
    private String uom;
    private String notes;

    public DrayRate(String item, String c1, String c2, float ratePerUOM, float adjust, String uom, String notes) {
        this.item = item;
        this.c1 = c1;
        this.c2 = c2;
        this.ratePerUOM = ratePerUOM;
        this.adjust = adjust;
        this.uom = uom;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return item+","+c1+","+c2+","+ratePerUOM+","+adjust+","+uom+","+notes;
    }
}
