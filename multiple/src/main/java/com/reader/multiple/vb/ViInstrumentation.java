package com.reader.multiple.vb;

import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;

public class ViInstrumentation extends Instrumentation {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Log.i("DaemonLog", "ViInstrumentation onCreate");
    }

    public void onDestroy() {
        super.onDestroy();
//        Log.d(DaemonManager.LOG_TAG, "ViInstrumentation onDestroy");
    }
}
