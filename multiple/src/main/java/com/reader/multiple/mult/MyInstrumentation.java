package com.reader.multiple.mult;

import android.app.Application;
import android.app.Instrumentation;
import android.os.Bundle;

import com.reader.multiple.mult.service.CleanerService;

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
