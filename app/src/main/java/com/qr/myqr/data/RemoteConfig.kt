package com.qr.myqr.data

class RemoteConfig {

    companion object {
        val ins: RemoteConfig by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RemoteConfig()
        }
    }

    fun getAdsConf(): String {
        return ""//ads_conf
    }

    fun getSafeMode(): Long {
        return 0L//safe_mode
    }

    fun getPopConfig(): String {
        return ""//pop_conf {"open":true,"installTimeMin":10,"intervalMin":10,"onedayUp":1}
    }

    fun isNavOuterEnable(): Boolean {
        return true//"out_nav_enable"
    }

}