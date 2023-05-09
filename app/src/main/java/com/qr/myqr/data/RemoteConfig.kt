package com.qr.myqr.data

class RemoteConfig {

    companion object {
        val ins: RemoteConfig by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RemoteConfig()
        }
    }

    fun getAdsConf(): String {
        return ""
    }

    fun getSafeMode(): Long {
        return 0L
    }

    fun getPopConfig(): String {
        return ""
    }

}