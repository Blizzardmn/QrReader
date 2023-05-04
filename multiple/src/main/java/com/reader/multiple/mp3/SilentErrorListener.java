package com.reader.multiple.mp3;

import android.media.MediaPlayer;
import android.util.Log;

public class SilentErrorListener implements MediaPlayer.OnErrorListener {

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("DaemonLog", "mediaPlayer onError: " + what + "," + extra);
        return false;
    }
}

