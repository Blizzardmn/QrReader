package com.qr.myqr

import android.app.Application

class App : Application() {
    companion object {
        lateinit var mApp: App
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this
    }
}