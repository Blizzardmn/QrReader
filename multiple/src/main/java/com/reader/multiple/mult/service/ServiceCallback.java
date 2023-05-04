package com.reader.multiple.mult.service;

import android.app.Service;
import android.content.Context;

import com.reader.multiple.mult.receiver.MainProcessStartReceiver;

public class ServiceCallback implements MainProcessStartReceiver.OnMainProcessStartCallback {

    private final Service service;

    public ServiceCallback(Service service) {
        this.service = service;
    }

    @Override
    public void onMainStart(Context context) {
        ServiceStartReceiver.send(context, this.service.getClass().getName());
    }
}
