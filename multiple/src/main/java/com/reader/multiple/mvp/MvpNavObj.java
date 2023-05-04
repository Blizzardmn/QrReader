package com.reader.multiple.mvp;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Keep;

import com.reader.multiple.vb.MvpManager;
import com.reader.multiple.vb.ViInstrumentation;

import java.io.File;

@Keep
public class MvpNavObj {

    static {
        try {
            System.loadLibrary("james");
        } catch (Exception e) {
            ////Log.e("DaemonLog","System.loadLibrary error:");
        }
    }

    @Keep
    public static native void i(Context context);

    @Keep
    public static native int l(String str);

    @Keep
    public static native int s();

    @Keep
    public static native int w(String str);

    @Keep
    public static native void ahead(String var1, String var2, String var3, String var4, String var5, String var6);

    @Keep
    public static native void forkChild(Context context, String forkName, String selfForkLockFile, String selfForkWaitFile, String selfForkIndicatorFile, String selfForkWaitIndicatorFile);

    @Keep
    public static native void p(Intent intent);


    @Keep
    public static void restartProcess() {
        Context context = MvpManager.getInstance().getContext();
        //Log.e("DaemonLog", "restartProcess " + context);
        if (context != null && com.reader.multiple.bmw4.MvpManager.bh()) {
            try {
                context.startInstrumentation(new ComponentName(MvpManager.getInstance().getContext(), ViInstrumentation.class), null, null);
            } catch (Exception e) {
                e.printStackTrace();
                ////Log.e("DaemonLog", "startInstrumentation  " + e.getMessage());
            }
        }
    }

    @Keep
    public static void sp(String var1, String var2, String var3, String var4, String var5) {
        ////Log.e("DaemonLog", var1+" "+var2+" "+var3+" "+var4+" "+var5);
        ProcessHelper.startProcess(new File(var1), var2, var3, var4, var5);
    }

    //public static native void TB(Context context, String str, String str2, String str3, String str4);
}
