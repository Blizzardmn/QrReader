package com.reader.multiple.mult.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.reader.multiple.mult.ServiceUtils;
import com.reader.multiple.mult.service.CleanerService;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ////Log.e("DaemonLog", "MainReceiver onReceive");
        ServiceUtils.startService(context.getApplicationContext(), CleanerService.class);
    }
}
