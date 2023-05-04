package com.reader.multiple.mp4;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AsukaProcess2Service extends Service {

    public class a implements AsukaMainProcessStartReceiver.a {
        public a() {
        }

        @Override // com.immortal.aegis.native1.receiver.MainProcessStartReceiver.a
        public void a(Context context) {
            AsukaServiceStartReceiver.a(context, AsukaProcess2Service.class.getName());
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new AsukaB(this);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        AsukaServiceStartReceiver.a(this, AsukaProcess2Service.class.getName());
        AsukaMainProcessStartReceiver.a(this, new a());
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        return START_NOT_STICKY;
    }
}
