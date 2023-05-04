package com.reader.multiple.mp4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class AsukaMainProcessStartReceiver extends BroadcastReceiver {

    /* renamed from: a  reason: collision with root package name */
    public static AsukaMainProcessStartReceiver f12221a;

    /* renamed from: b  reason: collision with root package name */
    public a f12222b;

    public interface a {
        void a(Context context);
    }

    public static void a(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction("com.android.jikealiveintent.action.MAIN_PROCESS_START");
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        } catch (Exception unused) {
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        a aVar = this.f12222b;
        if (aVar != null) {
            aVar.a(context);
        }
    }

    public static synchronized void a(Context context, a aVar) {
        synchronized (AsukaMainProcessStartReceiver.class) {
            synchronized (AsukaMainProcessStartReceiver.class) {
                if (f12221a == null) {
                    f12221a = new AsukaMainProcessStartReceiver();
                    f12221a.f12222b = aVar;
                    IntentFilter intentFilter = new IntentFilter("com.android.jikealiveintent.action.MAIN_PROCESS_START");
                    intentFilter.setPriority(1000);
                    context.registerReceiver(f12221a, intentFilter);
                }
            }
        }
    }
}
