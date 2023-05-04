package com.reader.multiple.mvp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.reader.multiple.mvp.ServiceUtils;

public class ServiceStartReceiver extends BroadcastReceiver {

    public static ServiceStartReceiver receiver;

    public static synchronized void register(Context context) {
        synchronized (ServiceStartReceiver.class) {
            synchronized (ServiceStartReceiver.class) {
                if (receiver == null) {
                    receiver = new ServiceStartReceiver();
                    IntentFilter intentFilter = new IntentFilter("com.speed.action.SERVICE_INIT");
                    intentFilter.setPriority(1000);
                    context.registerReceiver(receiver, intentFilter);
                }
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("service_class");
        if (!TextUtils.isEmpty(stringExtra)) {
            ServiceUtils.bindService(context, stringExtra);
            ////Log.e("DaemonLog", "ServiceStartReceiver bind:" + stringExtra);
        }
    }

    public static void send(Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("com.speed.action.SERVICE_INIT");
        intent.putExtra("service_class", str);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }
}
