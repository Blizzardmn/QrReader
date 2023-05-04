package com.reader.multiple.add;

import android.app.Activity;
import android.os.Bundle;

public class ServiceKillActivity extends Activity {

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Log.d("ServiceKillActivity","kill activity onCreate");
        setFinishOnTouchOutside(true);
        finish();
    }
}
