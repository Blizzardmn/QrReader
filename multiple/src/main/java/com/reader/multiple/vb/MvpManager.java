package com.reader.multiple.vb;

import android.content.Context;

import com.reader.multiple.mult.MvpNavObj;

import java.io.File;

public class MvpManager {
    private volatile static MvpManager instance;
    public Context context;
    public static final String LOG_TAG = "DaemonLog";

    public static MvpManager getInstance() {
        if (instance == null) {
            synchronized (MvpManager.class) {
                if (instance == null) {
                    instance = new MvpManager();
                }
            }
        }
        return instance;
    }

    /*public class AccountRunnable implements Runnable {
        public void run() {
            AccountHelper.autoSyncAccount(context);
        }
    }

    private void initDaemonReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        context.registerReceiver(new DaemonReceiver(), intentFilter);
    }

    private void startPackageMonitor() {
        if (RomUtil.isMiui() && Build.VERSION.SDK_INT < 29) {
            new Thread(new Runnable() {
                public void run() {
                    Process.setThreadPriority(-2);
                    Log.d(DaemonManager.LOG_TAG, "startPackageMonitor");
                    while (true) {
                        try {
                            if ((context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).flags & 2097152) != 0) {
                                DaemonNative.restartProcess();
                                for (int i = 0; i < 3; i++) {
                                    new Thread(new Runnable() {
                                        public void run() {
                                            DaemonNative.restartProcess();
                                        }
                                    }).start();
                                }
                            }
                        } catch (Throwable th) {
                            Log.e(DaemonManager.LOG_TAG, "getApplicationInfo error", th);
                        }
                    }
                }
            }).start();
        }
    }*/

    public Context getContext() {
        return context;
    }

    public void init(final Context context) {
        if(!com.reader.multiple.mp4.MvpManager.bh()){
            return;
        }
        this.context = context;
        ProcessHolder.init(context);
        MvpHelper.init(context);
        if (ProcessHolder.IS_MAIN) {
            clearIndicatorFiles();
        }
        if (ProcessHolder.IS_MAIN || ProcessHolder.IS_DAEMON) {
            //initDaemonReceiver();
            //startPackageMonitor();
            new Thread(new Runnable() {
                public void run() {
                    forkDaemonProcess(context);
                }
            }).start();
            MvpHelper.startServices(context);
        }
        //DaemonHelper.startServices(context);
        /*if ((ProcessHolder.IS_MAIN || ProcessHolder.IS_SERVICE)) {
            if (ProcessHolder.IS_MAIN) {
                pk.getInstance().addCallback(listener);
            } else {
                pk.setQueryInterval(1000);
            }
            ScreenMonitorHelper.start();
        }*/
        /*DaemonJobService.scheduleService(context);
        if (ProcessHolder.IS_MAIN) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    startAutoAccountSync();
                }
            }, 3000);
        }*/
    }

    private void clearIndicatorFiles() {
        for (String next : MvpHelper.getIndicatorFiles(context)) {
            if (next != null && new File(next).delete()) {
                //Log.d(DaemonManager.LOG_TAG, "delete indicatorFile success,file=" + next);
            }
        }
    }

    /*private void startAutoAccountSync() {
        new Thread(new AccountRunnable()).start();
    }*/

    public void forkDaemonProcess(Context context) {
        //Log.i(DaemonManager.LOG_TAG, "forkChild,context=" + context);
        String forkName = MvpHelper.getForkName();
        String selfForkLockFile = MvpHelper.getSelfForkLockFile(context);
        String selfForkWaitFile = MvpHelper.getSelfForkWaitFile(context);
        String selfForkIndicatorFile = MvpHelper.getSelfForkIndicatorFile(context);
        String selfForkWaitIndicatorFile = MvpHelper.getSelfForkWaitIndicatorFile(context);
//        Log.i(DaemonManager.LOG_TAG, "===============forkChild log start ==============");
//        Log.i(DaemonManager.LOG_TAG, "forkChild,forkName=" + forkName);
//        Log.i(DaemonManager.LOG_TAG, "forkChild,forkLockFile=" + selfForkLockFile);
//        Log.i(DaemonManager.LOG_TAG, "forkChild,forkWaitFile=" + selfForkWaitFile);
//        Log.i(DaemonManager.LOG_TAG, "forkChild,forkIndicatorFile=" + selfForkIndicatorFile);
//        Log.i(DaemonManager.LOG_TAG, "forkChild,forkWaitIndicatorFile=" + selfForkWaitIndicatorFile);
//        Log.i(DaemonManager.LOG_TAG, "===============forkChild log end==============");
        MvpNavObj.forkChild(context, forkName, selfForkLockFile, selfForkWaitFile, selfForkIndicatorFile, selfForkWaitIndicatorFile);
    }
}
