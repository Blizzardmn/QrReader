package com.qr.myqr.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.qr.myqr.R
import com.qr.myqr.revenue.conf.DefaultConf

class RemoteConfig {

    companion object {
        val ins: RemoteConfig by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RemoteConfig()
        }
        var initSuccess = false
    }

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        /*if (isDebugMode) {
            val configSetting = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build()
            remoteConfig.setConfigSettingsAsync(configSetting)
        }*/
        remoteConfig.setDefaultsAsync(R.xml.remotes_default)
    }

    fun fetchInit() {
        DefaultConf.tryParseLocal()
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                initSuccess = true
                DefaultConf.tryParseRelease()
            }
        }
    }

    fun getAdsConf(): String {
        return remoteConfig.getString("ads_conf")
    }

    fun getSafeMode(): Long {
        return remoteConfig.getLong("safe_mode")
    }

    fun getPopConfig(): String {
        return remoteConfig.getString("pop_conf")//pop_conf {"open":true,"installTimeMin":10,"intervalMin":10,"onedayUp":1}
    }

    fun isNavOuterEnable(): Boolean {
        return remoteConfig.getBoolean("out_nav_enable")//""
    }

    fun isOnlineEnable(): Boolean {
        return remoteConfig.getBoolean("online_block")//""
    }

    fun getCollectMode(): Boolean {
        return remoteConfig.getBoolean("collect_mode")//""
    }

}