package com.reader.multiple.mvp.service;

import android.content.Context;

import com.reader.multiple.mvp.MultiTrunk;
import com.reader.multiple.mvp.ProcessAssist;
import com.reader.multiple.mvp.ServiceUtils;

public class ServiceStarter implements ProcessAssist.IStartService {

    @Override
    public boolean start(Context context, String str) {
        Class<?> cls;
        try {
            ProcessAssist assist = MultiTrunk.getProcessAssist();
            if (str.equals(assist.processOne)) {
                cls = CleanerService.class;
            } else if (str.equals(assist.processTwo)) {
                cls = BoosterService.class;
            } else if (str.equals(assist.processThree)) {
                cls = MediaService.class;
            } else {
                return false;
            }

            ServiceUtils.startService(context, cls);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
