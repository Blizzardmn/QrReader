package com.reader.multiple.mp4;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelMulti implements Parcelable.Creator<MvpParcel> {
    @Override // android.os.Parcelable.Creator
    public MvpParcel createFromParcel(Parcel parcel) {
        return new MvpParcel(parcel);
    }

    @Override // android.os.Parcelable.Creator
    public MvpParcel[] newArray(int i2) {
        return new MvpParcel[i2];
    }
}
