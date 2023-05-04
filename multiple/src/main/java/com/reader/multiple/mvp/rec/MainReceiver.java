package com.reader.multiple.mvp.rec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.reader.multiple.mvp.ServiceUtils;
import com.reader.multiple.mvp.service.CleanerService;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ////Log.e("DaemonLog", "MainReceiver onReceive");
        ServiceUtils.startService(context.getApplicationContext(), CleanerService.class);
    }
}
