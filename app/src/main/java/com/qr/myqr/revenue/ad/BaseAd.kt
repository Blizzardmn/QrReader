package com.qr.myqr.revenue.ad

import androidx.annotation.CallSuper
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.conf.AdConf

open class BaseAd(@AdPos adPos: String, val adConf: AdConf) {

    private val loadedTms = System.currentTimeMillis()

    @CallSuper
    open fun loaded(ad: Any, adsListener: AdsListener) {
        redefineListener(adsListener)
    }

    fun isExpired(): Boolean {
        return (System.currentTimeMillis() - loadedTms) < 120 * 60 * 1000L
    }

    fun redefineListener(adsListener: AdsListener) {
        unitAdShown = { adsListener.onShown() }
        unitAdClick = { adsListener.onClicked() }
        unitAdClose = { adsListener.onDismiss() }
    }

    var unitAdShown: () -> Unit = {}
    var unitAdClick: () -> Unit = {}
    var unitAdClose: () -> Unit = {}

    open fun onDestroy() { }

}