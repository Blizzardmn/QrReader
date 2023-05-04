package com.reader.multiple.murder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class BaseMurderActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("app_fun",this.getClass().getSimpleName() + "->onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("app_fun",this.getClass().getSimpleName() + "->onResume");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("app_fun",this.getClass().getSimpleName() + "->onDestroy");
    }
}
