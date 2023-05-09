package com.qr.myqr.revenue;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({AdPos.open, AdPos.navCreate, AdPos.navResult, AdPos.navOut
        , AdPos.insHome, AdPos.insClick, AdPos.insOut
        , AdPos.bannerMain, AdPos.bannerOther})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdPos {

    String open = "open";
    String navCreate = "nav_create";
    String navResult = "nav_result";
    String navOut = "nav_out";

    String insHome = "int_home";
    String insClick = "int_click";
    String insOut = "int_out";

    String bannerMain = "banner_main";
    String bannerOther = "banner_other";
}
