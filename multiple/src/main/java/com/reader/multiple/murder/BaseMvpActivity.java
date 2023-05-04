package com.reader.multiple.murder;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class BaseMvpActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
