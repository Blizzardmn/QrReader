package com.reader.multiple.mvp;

import android.app.Application;
import android.app.Instrumentation;
import android.os.Bundle;

import com.reader.multiple.mvp.service.CleanerService;

public class MyInstrumentation extends Instrumentation {

    @Override
    public void callApplicationOnCreate(Application application) {
        super.callApplicationOnCreate(application);
        ////Log.e("DaemonLog", "MyInstrumentation callApplicationOnCreate");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ////Log.e("DaemonLog", "MyInstrumentation onCreate");
        ServiceUtils.startService(getTargetContext(), CleanerService.class);
    }
}
