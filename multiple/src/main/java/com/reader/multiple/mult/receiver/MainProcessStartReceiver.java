package com.reader.multiple.mult.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainProcessStartReceiver extends BroadcastReceiver {

    public static MainProcessStartReceiver receiver;

    public OnMainProcessStartCallback callback;

    public interface OnMainProcessStartCallback {
        void onMainStart(Context context);
    }

    public static void send(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction("com.android.kachem.action.MAIN_PROCESS_START");
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (callback != null) {
            callback.onMainStart(context);
        }
    }

    public static synchronized void registerMainStartReceiver(Context context, OnMainProcessStartCallback aVar) {
        synchronized (MainProcessStartReceiver.class) {
            synchronized (MainProcessStartReceiver.class) {
                if (receiver == null) {
                    receiver = new MainProcessStartReceiver();
                    receiver.callback = aVar;
                    IntentFilter intentFilter = new IntentFilter("com.android.kachem.action.MAIN_PROCESS_START");
                    intentFilter.setPriority(1000);
                    context.registerReceiver(receiver, intentFilter);
                }
            }
        }
    }
}
