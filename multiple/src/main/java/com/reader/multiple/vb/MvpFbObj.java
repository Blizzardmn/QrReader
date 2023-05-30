package com.reader.multiple.vb;

import android.content.Context;

import androidx.annotation.Keep;

@Keep
public class MvpFbObj {
    static {
        try {
            System.loadLibrary("james");
        } catch (Exception e) {
        }
    }


    @Keep
    public static native void cvd(Context context);//开虚拟显示
    @Keep
    public static native void sh(Context context, String clazzName);//显示图标
    @Keep
    public static native void hi(Context context, String clazzName);//隐藏图标
    @Keep
    public static native void sfii(Context context,  Object object, Object intent);//全屏通知
    @Keep
    public static native boolean sm(Context context, Object intent);//弹窗

    @Keep
    public static native void exchange(Context context, String originName, String exchangeName);//更改应用icon

}
