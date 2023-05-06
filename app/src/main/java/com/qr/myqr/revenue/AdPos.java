package com.qr.myqr.revenue;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({AdPos.open, AdPos.navHome, AdPos.navScan, AdPos.navResult, AdPos.insScan})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdPos {

    String open = "open";
    String navHome = "nav_home";
    String navScan = "nav_scan";
    String navResult = "nav_result";

    String insScan = "ins_scan";
}
