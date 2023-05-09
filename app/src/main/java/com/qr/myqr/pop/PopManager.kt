package com.qr.myqr.pop

import com.qr.myqr.appIns
import com.qr.myqr.data.AppCache
import com.qr.myqr.data.OneDayCache
import com.qr.myqr.data.RemoteConfig
import com.qr.myqr.tools.NetUtils
import com.reader.multiple.vb.MvpFbObj
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

object PopManager {

    private var outerEnable: Boolean = false
    private var installTimeMin = 0
    private var intervalPopMin = 0
    private var dailyUpper = 10

    fun pop() {
        if (appIns.isAppForeground()) return
        if (!NetUtils.isNetworkConnected()) return
        if (!isValid()) return
        MainScope().launch {
            MvpFbObj.cvd(appIns)
            delay(1200L)
            PopActivity.open()
        }
    }

    //{"open":true,"installTimeMin":10,"intervalMin":10,"onedayUp":10}
    private fun isValid(): Boolean {
        if (installTimeMin <= 0 && intervalPopMin <= 0) {
            val remote = RemoteConfig.ins.getPopConfig()
            try {
                val json = JSONObject(remote)
                outerEnable = json.optBoolean("open", false)
                installTimeMin = json.optInt("installTimeMin", 10)
                intervalPopMin = json.optInt("intervalMin", 10)
                dailyUpper = json.optInt("onedayUp", 10)
            } catch (e: Exception) {
            }
        }

        if (!outerEnable) return false
        val installedMin = (System.currentTimeMillis() - getInstallTime()) / 60_000
        if (installedMin < installTimeMin) return false

        val interval = (System.currentTimeMillis() - OneDayCache.ins.getLastAlertTimestamp()) / 60_000L
        if (interval < intervalPopMin) return false

        val times = OneDayCache.ins.getAlertCounters()
        if (times > dailyUpper) return false

        OneDayCache.ins.addAlertCounters()
        OneDayCache.ins.setLastAlertTimestamp()
        return true
    }

    private fun getInstallTime(): Long {
        return try {
            val pm = appIns.applicationContext.packageManager
            val packageInfo = pm.getPackageInfo(appIns.packageName, 0)
            packageInfo.firstInstallTime
        } catch (e: Exception) {
            AppCache.ins.firstInAppTms
        }
    }
}