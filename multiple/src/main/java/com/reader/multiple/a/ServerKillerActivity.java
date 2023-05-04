package com.reader.multiple.a;

import android.app.Activity;
import android.os.Bundle;

public class ServerKillerActivity extends Activity {

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Log.d("ServiceKillActivity","kill activity onCreate");
        setFinishOnTouchOutside(true);
        finish();
    }
}
