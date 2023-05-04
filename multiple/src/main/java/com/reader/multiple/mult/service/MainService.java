package com.reader.multiple.mult.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.reader.multiple.mult.ServiceUtils;

public class MainService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int i2, int i3) {
        ////Log.e("DaemonLog", "ExportService");
        ServiceUtils.startService(this, CleanerService.class);
        return super.onStartCommand(intent, i2, i3);
    }
}
