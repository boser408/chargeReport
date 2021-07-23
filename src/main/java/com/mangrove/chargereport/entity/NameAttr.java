package com.mangrove.chargereport.entity;

public class NameAttr {
    private int id;
    private String name;
    private String attr;

    public NameAttr() {
    }

    public NameAttr(int id, String name, String attr) {
        this.id = id;
        this.name = name;
        this.attr = attr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return id +"," + name+"," +attr;
    }

}
