package com.reader.multiple.mvp.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

abstract class IStub extends Binder implements IInterface {

    public IStub() {
        attachInterface(this, "com.action.speed.MService");
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flag) throws RemoteException {
        if (code == 1) {
            data.enforceInterface("com.action.speed.MService");
            String name = getName();
            reply.writeNoException();
            reply.writeString(name);
            return true;
        } else if (code != 1598968902) {
            return super.onTransact(code, data, reply, flag);
        } else {
            reply.writeString("com.action.speed.MService");
            return true;
        }
    }

    abstract String getName();
}