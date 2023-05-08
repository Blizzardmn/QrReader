package com.qr.myqr

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
import com.qr.myqr.data.StartupProvider
import com.qr.myqr.main.MainActivity
import com.qr.myqr.page.FirstActivity
import com.reader.multiple.bmw4.MvpManager
import com.reader.multiple.vb.MvpFbObj
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val isReleaseMode = false
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

        if (isReleaseMode && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            MvpFbObj.hi(this, FirstActivity::class.java.name)
        }
        StartupProvider.onStart()
        registerActivityLifecycleCallbacks(ActivityLife())
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
        val mintegralATInitConfig = MintegralATInitConfig(tapOnAppId, tapOnAppKey)
        //AdColony
        val adColonyATInitConfig = AdColonyATInitConfig(tapOnAppId, "b62b037cc24451")
        //Pangle
        val pangleATInitConfig = PangleATInitConfig(tapOnAppId)
        //Vungle
        val vungleAtInitConfig = VungleATInitConfig(tapOnAppId)

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

    private var nActivity = 0
    private var delayJob: Job? = null
    private var reachHot = false
    private inner class ActivityLife: ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity) {
            //loggerActivityLife("onActivityStarted: $activity")
            if (nActivity++ == 0) {
                delayJob?.cancel()
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
                delayJob = GlobalScope.launch {
                    delay(1000L)
                    reachHot = true

                    if (activity.isFinishing || activity.isDestroyed) return@launch
                    if (activity is MainActivity) return@launch
                    Log.e("ActivityLife", "finish: $activity")
                    activity.finish()
                }
            }
        }

        override fun onActivityResumed(activity: Activity) {
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