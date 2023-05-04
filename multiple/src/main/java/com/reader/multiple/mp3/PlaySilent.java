package com.reader.multiple.mp3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.util.Log;

public class PlaySilent {

    public MediaPlayer f27334a;

    public PlaySilentBroadcastReceiver f27335b;

    public boolean f27336c = false;

    public boolean f27337d = false;

    public boolean f27338e;


    public class PlaySilentBroadcastReceiver extends BroadcastReceiver {

        public PlaySilentBroadcastReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                String action = intent.getAction();
                if ("android.intent.action.SCREEN_OFF".equalsIgnoreCase(action)) {
                    PlaySilent.this.b();
                } else if ("com.ms.android.dailycleanaegis.background".equalsIgnoreCase(action)) {
                    PlaySilent.this.b();
                } else if ("com.ms.android.dailycleanaegis.foreground".equalsIgnoreCase(action)) {
                    PlaySilent.this.a();
                }
            }
        }
    }

    public void d(Context context){
        e(context);
        a();
        MediaPlayer mediaPlayer = this.f27334a;
        if (mediaPlayer != null){
            mediaPlayer.release();
            this.f27334a = null;
        }
        this.f27337d = false;
        this.f27336c = false;
        this.f27338e = false;
    }

    public final void e(Context context){
        PlaySilentBroadcastReceiver aVar = this.f27335b;
        if (aVar != null){
            context.unregisterReceiver(aVar);
            this.f27335b = null;
        }
    }

    public final void b(){
        this.f27338e = true;
        MediaPlayer mediaPlayer = this.f27334a;
        if (mediaPlayer != null && !this.f27336c && this.f27337d){
            try {
                mediaPlayer.start();
            }catch (IllegalStateException unused){
                this.f27337d = false;
            }
        }
    }

    public final void c(Context context){
    }

    public void a(Context context){
        this.f27338e = true;
        //c(context);
        b(context);
    }

    public final void a(){
        this.f27338e = false;
        MediaPlayer mediaPlayer = this.f27334a;
        if (mediaPlayer != null && this.f27336c && this.f27337d){
            try {
                mediaPlayer.pause();
                Log.i("DaemonLog","MediaPlayer pause");
                this.f27336c = false;
            }catch (IllegalStateException e){
                this.f27336c = false;
            }
        }
    }

    public final void b(Context context){
        if (this.f27335b == null){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("com.ms.android.dailycleanaegis.foreground");
            intentFilter.addAction("com.ms.android.dailycleanaegis.background");
            this.f27335b = new PlaySilentBroadcastReceiver();
            context.registerReceiver(this.f27335b, intentFilter);
        }
    }

}
