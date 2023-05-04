package com.reader.multiple.mvp;

import android.content.Context;
import android.os.Build;
import android.os.Process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AppCtx {

    public Context context;

    public String pkgName;

    public String processName = getProcessName();

    public int salt = 5;

    public AppCtx(Context context) {
        this.context = context;
        this.pkgName = context.getPackageName();
    }

    public AppCtx a(int i2) {
        this.salt = i2;
        return this;
    }

    public boolean e() {
        return (this.salt & 4) != 0;
    }

    public boolean c() {
        return (this.salt & 2) != 0;
    }

    public boolean a() {
        return (this.salt & 1) != 0;
    }


    public static String getProcessName() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/proc/" + Process.myPid() + "/cmdline")));
            String trim = bufferedReader.readLine().trim();
            bufferedReader.close();
            return trim;
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean isUseJob() {
        return (Build.BRAND.toLowerCase().contains("oppo") && Build.VERSION.SDK_INT > 28) ||
                Build.BRAND.toLowerCase().contains("vivo") ||
                Build.BRAND.toLowerCase().contains("meizu");
    }
}
