package com.reader.multiple.mp3;

import android.media.MediaPlayer;

public class SilentListener implements MediaPlayer.OnPreparedListener {

    public final PlaySilent f27332a;

    public SilentListener(PlaySilent cVar){
        this.f27332a = cVar;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        this.f27332a.f27337d = true;
        if (this.f27332a.f27338e){
            this.f27332a.b();
        }
    }
}

