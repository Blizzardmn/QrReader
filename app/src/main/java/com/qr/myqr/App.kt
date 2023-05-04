package com.qr.myqr

import android.app.Application
import com.reader.multiple.mp4.MvpManager

class App : Application() {
    companion object {
        lateinit var mApp: App
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this

        MvpManager.setBhs(this, true)
        MvpManager.init(this)
    }

}