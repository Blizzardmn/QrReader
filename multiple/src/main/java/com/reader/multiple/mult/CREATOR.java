package com.reader.multiple.mult;

import android.os.Parcel;
import android.os.Parcelable;

public class CREATOR implements Parcelable.Creator<MyParcel> {
    @Override
    public MyParcel createFromParcel(Parcel parcel) {
        return new MyParcel(parcel);
    }

    @Override
    public MyParcel[] newArray(int i2) {
        return new MyParcel[i2];
    }
}
