package com.reader.multiple.bmw4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccAuthenticatorService extends Service {

    /* renamed from: a  reason: collision with root package name */
    public MvpAccountAA accountAA;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.accountAA.getIBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.accountAA = new MvpAccountAA(getApplicationContext());
    }
}

