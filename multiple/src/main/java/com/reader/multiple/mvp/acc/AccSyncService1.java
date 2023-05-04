package com.reader.multiple.mvp.acc;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;

import com.reader.multiple.mvp.MultiTrunk;

public class AccSyncService1 extends Service {
    public SyncAdatper syncAdatper;

    private static class SyncAdatper extends AbstractThreadedSyncAdapter {
        public SyncAdatper(Context context, boolean z) {
            super(context, z);
        }

        @Override
        public void onPerformSync(Account account, Bundle bundle, String str, ContentProviderClient contentProviderClient, SyncResult syncResult) {
            syncResult.databaseError = true;
            MultiTrunk.setIsSyncable();
            ////Log.e("DaemonLog", "onPerformSync setIsSyncable");
        }

        @Override
        public void onSyncCanceled() {
            super.onSyncCanceled();
            MultiTrunk.setIsSyncable();
            ////Log.e("DaemonLog", "onSyncCanceled onSyncCanceled");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.syncAdatper.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        syncAdatper = new SyncAdatper(getApplicationContext(), true);
    }
}
