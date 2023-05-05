package com.qr.myqr

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
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

}