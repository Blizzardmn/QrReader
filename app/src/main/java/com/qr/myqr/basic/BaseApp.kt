package com.qr.myqr.basic

import android.app.Application
import com.tencent.mmkv.MMKV

open class BaseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }

}