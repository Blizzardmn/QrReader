package com.reader.multiple.mult.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountAuthenticatorService extends Service {

    public AccountAuthenticator authenticator;

    @Override
    public IBinder onBind(Intent intent) {
        return this.authenticator.getIBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.authenticator = new AccountAuthenticator(getApplicationContext());
    }
}
