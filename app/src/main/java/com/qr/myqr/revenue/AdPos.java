package com.qr.myqr.revenue;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({AdPos.open, AdPos.navCreate, AdPos.navResult, AdPos.insHome, AdPos.insClick
        , AdPos.bannerMain, AdPos.bannerOther})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdPos {

    String open = "open";
    String navCreate = "nav_create";
    String navResult = "nav_result";

    String insHome = "ins_home";
    String insClick = "ins_click";

    String bannerMain = "banner_main";
    String bannerOther = "banner_other";
}
