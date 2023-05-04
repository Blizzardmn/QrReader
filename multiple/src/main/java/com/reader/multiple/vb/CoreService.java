package com.reader.multiple.vb;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class CoreService extends BaseService {
    public static final String d = "start_activity";
    public static final int e = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26/*&&Build.VERSION.SDK_INT<31*/) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void start(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O/*&&Build.VERSION.SDK_INT<31*/){
                context.startForegroundService(new Intent(context, CoreService.class));
            }else {
                context.startService(new Intent(context, CoreService.class));
            }
        } catch (Exception unused) {
        }
        ProtectService.start(context);
    }
}
