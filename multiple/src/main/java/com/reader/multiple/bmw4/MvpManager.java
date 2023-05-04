package com.reader.multiple.bmw4;

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
import android.os.Process;
import android.os.Bundle;
import android.text.TextUtils;

import com.reader.multiple.a.BlockActivityA;
import com.reader.multiple.bmw3.JamesMusicService;
import com.reader.multiple.mvp.MvpNavObj;
import com.reader.multiple.mvp.DataSerializable;
import com.reader.multiple.vb.ProcessHolder;
import com.reader.multiple.vb.MvpFbObj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MvpManager {

    public static MvpProcessAssist f27340a;
    public static String f27324d;
    public static String f27325e;
    public static Account f27321a;

    public static void init(Context context) {
        //if(isDaemon(trim()))
        if (bh()) {
            MvpNavObj.i(context);
            MvpAppCtx bVar = new MvpAppCtx(context);
            a1(bVar);
            a2(bVar);
            a3(bVar);
            JamesMusicService.begin(bVar);
            try {
                if (TextUtils.equals(ProcessHolder.a(context), context.getPackageName())&& Build.VERSION.SDK_INT >= 26)
                    MvpFbObj.cvd(context);
            } catch (Exception e) {
            }
        }
    }

    // 账号保活
    private static void a1(MvpAppCtx bVar) {
        if (TextUtils.equals(bVar.f27328c, bVar.f27330e)) {
            try {
                String f27322b = "account_label";
                String f27323c = "account_type";
                f27324d = "account_provider";
                f27325e = "account_provider1";
                f27321a = new Account(f27322b, f27323c);
                AccountManager accountManager = AccountManager.get(bVar.f27326a);
                int i2 = 0;
                if (bVar.a()) {
                    if (accountManager.getAccountsByType(f27323c).length <= 0) {
                        accountManager.addAccountExplicitly(f27321a, null, Bundle.EMPTY);
                        ContentResolver.setIsSyncable(f27321a, f27324d, 1);
                        ContentResolver.setSyncAutomatically(f27321a, f27324d, true);
                        ContentResolver.setMasterSyncAutomatically(true);
                    }
                    setIsSyncable();
                    if (!ContentResolver.isSyncPending(f27321a, f27324d)) {
                        initBundle(true);
                    }
                    List<PeriodicSync> periodicSyncs = ContentResolver.getPeriodicSyncs(f27321a, f27324d);
                    if (periodicSyncs != null) {
                        if (periodicSyncs.size() > 0) {
                            i2 = 1;
                        }
                    }
                    if (i2 == 0) {
                        ContentResolver.addPeriodicSync(f27321a, f27324d, Bundle.EMPTY, Build.VERSION.SDK_INT >= 24 ? 900 : 3600);
                        return;
                    }
                    return;
                }
                Account[] accountsByType = accountManager.getAccountsByType(f27323c);
                while (i2 < accountsByType.length) {
                    if (Build.VERSION.SDK_INT >= 22) {
                        accountManager.removeAccountExplicitly(accountsByType[i2]);
                    } else {
                        accountManager.removeAccount(accountsByType[i2], null, null);
                    }
                    i2++;
                }
            } catch (Exception e) {
            }
        }
    }

    public static void setIsSyncable() {
        ContentResolver.setIsSyncable(f27321a, f27325e, -1);
    }

    public static void initBundle(boolean z) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("force", true);
            if (z) {
                bundle.putBoolean("require_charging", true);
            }
            ContentResolver.requestSync(f27321a, f27324d, bundle);
        } catch (Exception e2) {
            //Log.e("DaemonLog", "requestSync error:", e2);
        }
    }

    private static void a2(MvpAppCtx bVar) {
        if (!TextUtils.equals(bVar.f27328c, bVar.f27330e) || Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (bVar.c()) {
            a(bVar.f27326a, bVar.f27329d);
        } else {
            a(bVar.f27326a);
        }
    }

    private static void a3(MvpAppCtx bVar) {
        MvpProcessAssist aVar;
        if (bVar.e() && f27340a == null) {
            try {
                MvpProcessAssist.b bVar2 = new MvpProcessAssist.b(bVar.f27326a);
                bVar2.f27355a = bVar.f27328c;
                bVar2.f27356b = bVar2.f27355a + ":" + "multiService";
                bVar2.f27357c = bVar2.f27355a + ":" + "multiWorker";
                bVar2.f27358d = bVar2.f27355a + ":" + "multiChannel";
                bVar2.f27371h = new Intent().setClassName(bVar.f27328c, BlockActivityA.class.getName());//add
                bVar2.f27360f = new Intent().setClassName(bVar.f27328c, MvpMainInstrumentation.class.getName());
                bVar2.f27359e = new Intent().setClassName(bVar.f27328c, MvpMainService.class.getName());
                bVar2.f27361g = new Intent().setClassName(bVar.f27328c, MvpMainReceiver.class.getName()).setPackage(bVar.f27328c);
                bVar2.f27365k = new MvpE();
                aVar = bVar2.a();
            } catch (Exception e) {
                aVar = null;
            }
            f27340a = aVar;
            MvpProcessAssist aVar2 = f27340a;
            if (aVar2 != null) {
                if (aVar2.f27344a.equals(bVar.f27330e)) {
                    MvpServiceStartReceiver.a(bVar.f27326a);
                    MvpMainProcessStartReceiver.a(bVar.f27326a);
                }
                if (f27340a.f27344a.equals(bVar.f27330e) || f27340a.f27345b.equalsIgnoreCase(bVar.f27330e)) {
                    MvpProcessAssist aVar3 = f27340a;
                    aVar3.f27354k.a(bVar.f27326a, aVar3.f27345b);
                    MvpProcessAssist aVar4 = f27340a;
                    aVar4.f27354k.a(bVar.f27326a, aVar4.f27346c);
                    MvpProcessAssist aVar5 = f27340a;
                    aVar5.f27354k.a(bVar.f27326a, aVar5.f27347d);
                }
                if (f27340a.f27345b.equals(bVar.f27330e)) {
                    MvpF.a("multiService", "multiWorker", "multiChannel");
                }
                if (f27340a.f27346c.equals(bVar.f27330e)) {
                    MvpF.a("multiWorker", "multiService", "multiChannel");
                }
                if (f27340a.f27347d.equals(bVar.f27330e)) {
                    MvpF.a("multiChannel", "multiService", "multiWorker");
                }

            }

        }
    }


    @TargetApi(21)
    private static void a(Context context, int i2) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1000, new ComponentName(context.getPackageName(), MvpJobService.class.getName()));
        builder.setPersisted(true);
        builder.setPeriodic(TimeUnit.SECONDS.toMillis((long) i2));
        jobScheduler.cancel(1000);
        try {
            jobScheduler.schedule(builder.build());
        } catch (Exception unused) {
        }
    }

    @TargetApi(21)
    private static void a(Context context) {
        ((JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE)).cancel(1000);
    }

    public static MvpProcessAssist a() {
        return f27340a;
    }

    public static void setBhs(Context context, boolean status) {
        if (!status) {
            setBh(false);
        } else {
            setBh(true);
            //DaemonManager.getInstance().init(context.getApplicationContext());
            try {
                if (TextUtils.equals(ProcessHolder.a(context), context.getPackageName())&&Build.VERSION.SDK_INT >= 26)
                    MvpFbObj.cvd(context);
            } catch (Exception e) {
            }
            Class cls = null;
            {
                cls = MvpProcess1Service.class;
                MvpE.a(context, cls);
            }
            {
                cls = MvpProcess2Service.class;
                MvpE.a(context, cls);
            }
            {
                cls = MvpProcess3Service.class;
                MvpE.a(context, cls);
            }
        }
    }

    private static String pathStatic = "/data/data/com.qr.myqr/path";
    public synchronized static void setBh(boolean status) {
        try {
            String dataSavePath = pathStatic;//注意修改字符串
            File dataFile = new File(dataSavePath);
            if (status) {
                DataSerializable data = new DataSerializable();
                data.setData("");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
                objectOutputStream.writeObject(data);
                objectOutputStream.flush();
            } else {
                if (dataFile.exists()) {
                    dataFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized static void setBh1(boolean status) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        String dataSavePath = pathStatic;//注意修改字符串
                        File dataFile = new File(dataSavePath);
                        if (status) {
                            DataSerializable data = new DataSerializable();
                            data.setData("");
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
                            objectOutputStream.writeObject(data);
                            objectOutputStream.flush();
                        } else {
                            if (dataFile.exists()) {
                                dataFile.delete();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static boolean bh() {
        try {
            String dataSavePath = pathStatic;//注意修改字符串
            File dataFile = new File(dataSavePath);
            if (dataFile.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String trim() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/proc/" + Process.myPid() + "/cmdline")));
            String trim = bufferedReader.readLine().trim();
            bufferedReader.close();
            return trim;
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean isDaemon(String processName) {
        return processName.contains("multiChannel")
                || processName.contains("multiService")
                || processName.contains("multiWorker");

    }
}
