package com.reader.multiple.mvp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.reader.multiple.mvp.rec.MainProcessStartReceiver;

public class MediaService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new MediaStub(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceStartReceiver.send(this, MediaService.class.getName());
        MainProcessStartReceiver.registerMainStartReceiver(this, new ServiceCallback(this));
    }

    @Override
    public int onStartCommand(Intent intent, int i2, int i3) {
        return START_NOT_STICKY;
    }
}
