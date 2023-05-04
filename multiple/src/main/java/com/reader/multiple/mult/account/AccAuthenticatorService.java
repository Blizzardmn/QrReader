package com.reader.multiple.mult.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccAuthenticatorService extends Service {

    public AccAuthenticator authenticator;

    @Override
    public IBinder onBind(Intent intent) {
        return this.authenticator.getIBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.authenticator = new AccAuthenticator(getApplicationContext());
    }
}
