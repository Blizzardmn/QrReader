package com.reader.multiple.mp4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountAuthenticatorService extends Service {

    /* renamed from: a  reason: collision with root package name */
    public AsukaAccountAA f12207a;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f12207a.getIBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f12207a = new AsukaAccountAA(getApplicationContext());
    }
}

