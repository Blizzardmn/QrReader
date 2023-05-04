package com.reader.multiple.mp4;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IInterStub extends IInterface {

    /* compiled from: IAegisService */
    public static abstract class a extends Binder implements IInterStub {
        public a() {
            attachInterface(this, "com.immortal.aegis.IAegisService");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i2, Parcel parcel, Parcel parcel2, int i3) throws RemoteException {
            if (i2 == 1) {
                parcel.enforceInterface("com.immortal.aegis.IAegisService");
                String name = getName();
                parcel2.writeNoException();
                parcel2.writeString(name);
                return true;
            } else if (i2 != 1598968902) {
                return super.onTransact(i2, parcel, parcel2, i3);
            } else {
                parcel2.writeString("com.immortal.aegis.IAegisService");
                return true;
            }
        }
    }

    String getName();
}