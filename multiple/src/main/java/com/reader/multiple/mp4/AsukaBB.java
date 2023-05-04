package com.reader.multiple.mp4;

import android.os.Parcel;
import android.os.Parcelable;

public class AsukaBB implements Parcelable.Creator<AsukaParcel> {
    @Override // android.os.Parcelable.Creator
    public AsukaParcel createFromParcel(Parcel parcel) {
        return new AsukaParcel(parcel);
    }

    @Override // android.os.Parcelable.Creator
    public AsukaParcel[] newArray(int i2) {
        return new AsukaParcel[i2];
    }
}
