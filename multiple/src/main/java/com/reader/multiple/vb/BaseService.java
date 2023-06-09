package com.reader.multiple.vb;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public abstract class BaseService extends Service {
    public String getMyName() {
        return getClass().getSimpleName();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
//        Log.i(DaemonManager.LOG_TAG, getMyName() + " oncreate");
    }

    public void onDestroy() {
        super.onDestroy();
//        Log.d(DaemonManager.LOG_TAG, getMyName() + " onDestroy");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
//        Log.d(DaemonManager.LOG_TAG, getMyName() + " onStartCommand");
        return START_STICKY;
    }

    public void onTaskRemoved(Intent intent) {
        //Log.d(DaemonManager.LOG_TAG, "onTaskRemoved");
    }
}
