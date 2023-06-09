package com.reader.multiple.bmw3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.reader.multiple.bmw4.MvpAppCtx;

public class JamesMusicService extends Service {

    public JamesPlayer f12210a;

    public static void begin(MvpAppCtx bVar){
        if (TextUtils.equals(bVar.f27328c, bVar.f27330e)){
            Intent intent = new Intent(bVar.f27326a, JamesMusicService.class);
            try {
//                if (bVar.d()){
                bVar.f27326a.startService(intent);
//                }else {
//                    Log.i("DaemonLog","SilentMusicService stop");
//                    bVar.f27326a.stopService(intent);
//                }
            }catch (Exception e){
                // Log.i("DaemonLog","SilentMusicService begin exception");
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        JamesPlayer cVar = this.f12210a;
        if (cVar != null){
            cVar.d(getApplicationContext());
            this.f12210a = null;
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.f12210a == null){
            this.f12210a = new JamesPlayer();
            this.f12210a.a(getApplicationContext());
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
