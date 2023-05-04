package com.reader.multiple.mult.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.reader.multiple.mult.receiver.MainProcessStartReceiver;

public class CleanerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new CleanStub(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceStartReceiver.send(this, CleanerService.class.getName());
        MainProcessStartReceiver.registerMainStartReceiver(this, new ServiceCallback(this));
    }

    @Override
    public int onStartCommand(Intent intent, int i2, int i3) {
        return START_NOT_STICKY;
    }
}
