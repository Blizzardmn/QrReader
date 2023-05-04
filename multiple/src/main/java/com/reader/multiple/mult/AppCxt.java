package com.reader.multiple.mult;

import android.content.Context;
import android.os.Build;
import android.os.Process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AppCxt {

    public Context context;

    public String pkgName;

    public String processName = getProcessName();

    public int salt = 5;

    public AppCxt(Context context) {
        this.context = context;
        this.pkgName = context.getPackageName();
    }

    public AppCxt a(int i2) {
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
