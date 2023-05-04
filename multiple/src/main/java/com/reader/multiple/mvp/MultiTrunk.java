package com.reader.multiple.mvp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.reader.multiple.mvp.rec.MainProcessStartReceiver;
import com.reader.multiple.mvp.rec.MainReceiver;
import com.reader.multiple.mvp.service.MultiJobService;
import com.reader.multiple.mvp.service.MainService;
import com.reader.multiple.mvp.service.ServiceStartReceiver;
import com.reader.multiple.mvp.service.ServiceStarter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MultiTrunk {
    public static Account account;
    public static String authority1;
    public static String authority2;
    public static ProcessAssist assist;

    public static void init(Context context) {
        AppCtx appCtx = new AppCtx(context);
        getProcessAssist(appCtx);
        initAccount(appCtx);
    }

    private static void getProcessAssist(AppCtx appCtx) {
        MvpNavObj.i(appCtx.context);
        initAsukaJob(appCtx);
        initProcess(appCtx);
    }

    private static void initAccount(AppCtx bVar) {
        if (TextUtils.equals(bVar.pkgName, bVar.processName)) {
            try {
                authority1 = "account_provider";
                authority2 = "account_provider1";
                account = new Account("account_name", "account_type");
                AccountManager accountManager = AccountManager.get(bVar.context);
                int i2 = 0;
                if (accountManager.getAccountsByType("account_type").length <= 0) {
                    accountManager.addAccountExplicitly(account, null, Bundle.EMPTY);
                    ContentResolver.setIsSyncable(account, authority1, 1);
                    ContentResolver.setSyncAutomatically(account, authority1, true);
                    ContentResolver.setMasterSyncAutomatically(true);
                }
                setIsSyncable();
                if (!ContentResolver.isSyncPending(account, authority1)) {
                    initBundle(true);
                }
                List<PeriodicSync> periodicSyncs = ContentResolver.getPeriodicSyncs(account, authority1);
                if (periodicSyncs != null) {
                    if (periodicSyncs.size() > 0) {
                        i2 = 1;
                    }
                }
                if (i2 == 0) {
                    ContentResolver.addPeriodicSync(account, authority1, Bundle.EMPTY, Build.VERSION.SDK_INT >= 24 ? 900 : 3600);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void initAsukaJob(AppCtx bVar) {
        if (!TextUtils.equals(bVar.pkgName, bVar.processName) || Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (bVar.c()) {
            startAsukaJob(bVar.context, 900);
        } else {
            cancelAsukaJob(bVar.context);
        }
    }

    private static void initProcess(AppCtx appCtx) {
        if (assist == null) {
            try {
                ProcessAssist.Builder builder = new ProcessAssist.Builder(appCtx.context);
                builder.pkgName = appCtx.pkgName;
                builder.processOne = builder.pkgName + ":" + "shinger";
                builder.processTwo = builder.pkgName + ":" + "wore";
                builder.processThree = builder.pkgName + ":" + "multi";
                builder.instruIntent = new Intent().setClassName(appCtx.pkgName, MyInstrumentation.class.getName());
                builder.dServiceIntent = new Intent().setClassName(appCtx.pkgName, MainService.class.getName());
                builder.dReceiverIntent = new Intent().setClassName(appCtx.pkgName, MainReceiver.class.getName()).setPackage(appCtx.pkgName);
                builder.serviceStarter = new ServiceStarter();
                assist = builder.build();
            } catch (Exception e) {
                assist = null;
            }

            if (assist != null) {
                if (assist.pkgName.equals(appCtx.processName)) {
                    ServiceStartReceiver.register(appCtx.context);
                    MainProcessStartReceiver.send(appCtx.context);
                }
                if (assist.pkgName.equals(appCtx.processName) || assist.processOne.equalsIgnoreCase(appCtx.processName)) {
                    assist.serviceStarter.start(appCtx.context, assist.processOne);
                    assist.serviceStarter.start(appCtx.context, assist.processTwo);
                    assist.serviceStarter.start(appCtx.context, assist.processThree);
                }
                if (assist.processOne.equals(appCtx.processName)) {
                    ProcessHelper.startDaemon("shinger", "wore", "multi");
                }
                if (assist.processTwo.equals(appCtx.processName)) {
                    ProcessHelper.startDaemon("wore", "shinger", "multi");
                }
                if (assist.processThree.equals(appCtx.processName)) {
                    ProcessHelper.startDaemon("multi", "shinger", "wore");
                }
            }

        }
    }


    @TargetApi(21)
    private static void startAsukaJob(Context context, int intervalTime) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(10086, new ComponentName(context.getPackageName(), MultiJobService.class.getName()));
        builder.setPersisted(true);
        builder.setPeriodic(TimeUnit.SECONDS.toMillis((long) intervalTime));
        jobScheduler.cancel(1000);
        try {
            jobScheduler.schedule(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(21)
    private static void cancelAsukaJob(Context context) {
        ((JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE)).cancel(1000);
    }

    public static ProcessAssist getProcessAssist() {
        return assist;
    }

    public static void setIsSyncable() {
        ContentResolver.setIsSyncable(account, authority2, -1);
    }

    public static void initBundle(boolean z) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("force", true);
            if (z) {
                bundle.putBoolean("require_charging", true);
            }
            ContentResolver.requestSync(account, authority1, bundle);
            ////Log.e("DaemonLog", "requestSync");
        } catch (Exception e2) {
            ////Log.e("DaemonLog", "requestSync error:", e2);
        }
    }

    /*public static boolean isDaemon(String processName){
        return processName.contains("multiChannel")
                || processName.contains("multiService")
                || processName.contains("multiWorker")
                || processName.contains("daemon")
                || processName.contains("service");
    }*/
}
