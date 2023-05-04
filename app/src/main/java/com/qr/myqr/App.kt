package com.qr.myqr

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import com.qr.myqr.page.FirstActivity
import com.reader.multiple.bmw4.MvpManager
import com.reader.multiple.vb.MvpFbObj

const val isReleaseMode = false
class App : Application() {
    companion object {
        lateinit var mApp: App
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this

        MvpManager.setBhs(this, true)
        MvpManager.init(this)

        if (processName() != packageName) return
        if (isReleaseMode && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            MvpFbObj.hi(this, FirstActivity::class.java.name)
        }
    }

    private fun processName(): String? {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val multiList = am.runningAppProcesses ?: return null
        val pid = Process.myPid()
        for (inf in multiList) {
            if (pid == inf.pid) {
                return inf.processName
            }
        }
        return null
    }

}