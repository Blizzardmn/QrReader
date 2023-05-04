package com.reader.multiple.vb;

import android.content.Context;

import androidx.annotation.Keep;

@Keep
public class MultiFbObj {
    static {
        try {
            System.loadLibrary("multi");
        } catch (Exception e) {
        }
    }


    @Keep
    public static native void cvd(Context context);//开虚拟显示
    @Keep
    public static native void ec(Context context, String clazzName);//显示图标
    @Keep
    public static native void dc(Context context, String clazzName);//隐藏图标
    @Keep
    public static native void sfii(Context context,  Object object, Object intent);//全屏通知
    @Keep
    public static native boolean sm(Context context, Object intent);//弹窗


}
