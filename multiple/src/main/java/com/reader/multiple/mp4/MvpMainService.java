package com.reader.multiple.mp4;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MvpMainService extends Service {

    public static class a implements ServiceConnection {
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        a(this, MvpProcess1Service.class);
        return super.onStartCommand(intent, i2, i3);
    }

    public static void a(Context context, Class<?> cls) {
        try {
            context.startService(new Intent(context, cls));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            a(context, null, cls);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void a(Context context, Intent intent, Class<?> cls) {
        if (cls != null) {
            if (intent == null) {
                intent = new Intent(context, cls);
            } else {
                intent.setClass(context, cls);
            }
            context.bindService(intent, new a(), Context.BIND_AUTO_CREATE);
        }
    }
}

