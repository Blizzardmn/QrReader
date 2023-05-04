package com.reader.multiple.vb;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;

import com.reader.multiple.mult.Reflection;

import java.lang.reflect.Field;

public class StartHelper {
    private volatile static StartHelper instance;

    private IBinder iBinder;

    private Parcel serviceParcel;

    private Parcel instruParcel;

    private int serviceCode;

    private int instrumentationCode;

    private StartHelper() {

    }

    public static StartHelper getInstance() {
        if (instance == null) {
            synchronized (StartHelper.class) {
                if (instance == null) {
                    instance = new StartHelper();
                }
            }
        }

        return instance;
    }

    public void start(Context context) {
        try {
            ////Log.e("DaemonLog", "StartHelper start");
            startInstrumentation();
            startService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initParcels(Context context) {
        Reflection.unseal(); //放开反射权限

        try {
            iBinder = (IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", String.class).invoke(null, "activity");
        } catch (Exception e3) {
            e3.printStackTrace();
        }

        /*Class<?> activityManagerNative;
        try {
            activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Object amn = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
            Field mRemoteField = amn.getClass().getDeclaredField("mRemote");
            mRemoteField.setAccessible(true);
            iBinder = (IBinder) mRemoteField.get(amn);
        } catch (Throwable e) {
            e.printStackTrace();
        }*/

        ////Log.e("DaemonLog", "initParcels iBinder" + iBinder);

        serviceCode = getTransactCode("TRANSACTION_startService", "START_SERVICE_TRANSACTION");
        instrumentationCode = getTransactCode("TRANSACTION_startInstrumentation", "START_INSTRUMENTATION_TRANSACTION");
        if (serviceCode == -1 && instrumentationCode == -1) {
            ////Log.e("DaemonLog", "initParcels serviceCode instrumentationCode -1");
            throw new RuntimeException("all binder code get failed");
        }
        try {
            serviceParcel = Parcel.obtain();
            serviceParcel.writeInterfaceToken("android.app.IActivityManager");
            serviceParcel.writeStrongBinder(null);
            if (Build.VERSION.SDK_INT >= 26) {
                serviceParcel.writeInt(1);
            }

            Class<?> serviceClass = DaemonHelper.f.get(context.getPackageName());
            if (serviceClass == null) {
                ////Log.e("DaemonLog", "initParcels service class is null");
                return;
            }
            Intent intent = new Intent().setClassName(context.getPackageName(), serviceClass.getName());
            intent.writeToParcel(serviceParcel, 0);
            serviceParcel.writeString(null);
            if (Build.VERSION.SDK_INT >= 26) {
                serviceParcel.writeInt(0);
            }
            if (Build.VERSION.SDK_INT > 22) {
                serviceParcel.writeString(intent.getComponent().getPackageName());
            }
            serviceParcel.writeInt(0);
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            instruParcel = Parcel.obtain();
            instruParcel.writeInterfaceToken("android.app.IActivityManager");
            if (Build.VERSION.SDK_INT >= 26) {
                instruParcel.writeInt(1);
            }
            Intent intent = new Intent().setClassName(context.getPackageName(), ViInstrumentation.class.getName());
            intent.getComponent().writeToParcel(instruParcel, 0);
            instruParcel.writeString(null);
            instruParcel.writeInt(0);
            instruParcel.writeInt(0);
            instruParcel.writeStrongBinder(null);
            instruParcel.writeStrongBinder(null);
            instruParcel.writeInt(0);
            instruParcel.writeString(null);
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    private int getTransactCode(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.app.IActivityManager$Stub");
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField.getInt(cls);
        } catch (Exception unused) {
            try {
                Class<?> cls2 = Class.forName("android.app.IActivityManager");
                Field declaredField2 = cls2.getDeclaredField(str2);
                declaredField2.setAccessible(true);
                return declaredField2.getInt(cls2);
            } catch (Exception unused2) {
                return -1;
            }
        }
    }

    private void startInstrumentation() {
        IBinder iBinder;
        ////Log.e("DaemonLog", "start startInstrumentation start ");
        Parcel parcel = instruParcel;
        if (!(parcel == null || (iBinder = this.iBinder) == null)) {
            try {
                iBinder.transact(instrumentationCode, parcel, null, 1);
                ////Log.e("DaemonLog", "start startInstrumentation transact ");
            } catch (Exception e2) {
                ////Log.e("DaemonLog", "start startInstrumentation:" + instrumentationCode, e2);
            }
        }
        ////Log.e("DaemonLog", "start startInstrumentation end");
    }

    private void startService() {
        IBinder iBinder;
        ////Log.e("DaemonLog", "start startService start");
        Parcel parcel = serviceParcel;
        if (!(parcel == null || (iBinder = this.iBinder) == null)) {
            try {
                iBinder.transact(serviceCode, parcel, null, 1);
            } catch (Exception e2) {
                ////Log.e("DaemonLog", "start startService:", e2);
            }
        }
        ////Log.e("DaemonLog", "start startService end");
    }
}
