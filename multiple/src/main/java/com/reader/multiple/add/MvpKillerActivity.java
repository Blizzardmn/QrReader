package com.reader.multiple.add;

import android.app.Activity;
import android.os.Bundle;

public class MvpKillerActivity extends Activity {

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Log.d("DaemonKillActivity","kill activity onCreate");
        setFinishOnTouchOutside(true);
        finish();
    }

}
