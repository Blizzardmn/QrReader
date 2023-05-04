package com.reader.multiple.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BlockActivityB extends Activity {
    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //Log.d("BringActivity9","bring up activity onCreate");
        setFinishOnTouchOutside(true);
        startActivity(new Intent(this, MvpKillerActivity.class));
        startActivity(new Intent(this, ServerKillerActivity.class));
        finish();
    }
}
