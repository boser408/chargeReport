package com.mangrove.chargereport.genManage;

import java.io.File;

public class CardConstant {
    public static final String sep= File.separator;
    public static final String dataPathPrex="E:"+ sep+"LSPreports"+sep+"Data"+sep;
    public static final String lsplistPath=dataPathPrex+"Template"+sep+"lsplist.csv";
    public static final String allApproved=dataPathPrex+"Report"+sep+"approved-All.csv";
    public static final String allRejected=dataPathPrex+"Report"+sep+"rejected-All.csv";
    public static final String allSubmitted=dataPathPrex+"Report"+sep+"submitted-All.csv";

}
