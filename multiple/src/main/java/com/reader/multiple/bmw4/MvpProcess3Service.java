package com.reader.multiple.bmw4;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MvpProcess3Service extends Service {

    public class a implements MvpMainProcessStartReceiver.a {
        public a() {
        }

        @Override // com.immortal.aegis.native1.receiver.MainProcessStartReceiver.a
        public void a(Context context) {
            MvpServiceStartReceiver.a(context, MvpProcess3Service.class.getName());
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new InterS3(this);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        MvpServiceStartReceiver.a(this, MvpProcess3Service.class.getName());
        MvpMainProcessStartReceiver.a(this, new a());
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        return START_NOT_STICKY;
    }
}
