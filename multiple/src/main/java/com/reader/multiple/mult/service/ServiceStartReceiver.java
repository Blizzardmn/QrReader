package com.reader.multiple.mult.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.reader.multiple.mult.ServiceUtils;

public class ServiceStartReceiver extends BroadcastReceiver {

    public static ServiceStartReceiver receiver;

    public static synchronized void register(Context context) {
        synchronized (ServiceStartReceiver.class) {
            synchronized (ServiceStartReceiver.class) {
                if (receiver == null) {
                    receiver = new ServiceStartReceiver();
                    IntentFilter intentFilter = new IntentFilter("com.android.kachem.action.SERVICE_START");
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
        intent.setAction("com.android.kachem.action.SERVICE_START");
        intent.putExtra("service_class", str);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }
}
