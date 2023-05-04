package com.qr.myqr

import android.app.Application
import com.reader.multiple.mp4.AsukaManager

class App : Application() {
    companion object {
        lateinit var mApp: App
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this

        AsukaManager.setBhs(this, true)
        AsukaManager.init(this)
    }

}