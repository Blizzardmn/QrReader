package com.reader.multiple.mult.service;

import android.content.Context;

import com.reader.multiple.mult.Asuka;
import com.reader.multiple.mult.ProcessAssist;
import com.reader.multiple.mult.ServiceUtils;

public class ServiceStarter implements ProcessAssist.IStartService {

    @Override
    public boolean start(Context context, String str) {
        Class<?> cls;
        try {
            ProcessAssist assist = Asuka.getProcessAssist();
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
