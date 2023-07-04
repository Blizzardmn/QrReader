package com.qr.myqr.revenue.ad

import android.app.Activity
import android.view.View
import com.anythink.core.api.ATAdInfo
import com.anythink.nativead.api.*
import com.facebook.appevents.AppEventsLogger
import com.qr.myqr.appIns
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.SelfRenderViewUtil
import com.qr.myqr.revenue.conf.AdConf
import java.math.BigDecimal
import java.util.*

class TopNative(@AdPos adPos: String, adConf: AdConf): BaseAd(adPos, adConf) {

    private var atNativeAd: ATNative? = null
    override fun loaded(ad: Any, adsListener: AdsListener) {
        super.loaded(ad, adsListener)
        atNativeAd = ad as? ATNative
    }

    override fun onDestroy() {
        atNativeAd?.nativeAd?.destory()
    }

    fun showAd(activityCtx: Activity, atNativeContainer: ATNativeView, selfRender: View): Boolean {
        val nativeAd = atNativeAd?.nativeAd ?: return false
        ATNative.entryAdScenario(adConf.id, adConf.placeId)
        nativeAd.setNativeEventListener(object :ATNativeEventExListener {
            override fun onAdImpressed(p0: ATNativeAdView?, p1: ATAdInfo?) {
                unitAdShown.invoke()
                if (p1 == null) return
                AppEventsLogger.newLogger(appIns).logPurchase(
                    BigDecimal.valueOf(p1.publisherRevenue),
                    Currency.getInstance(p1.currency)
                )
            }

            override fun onAdClicked(p0: ATNativeAdView?, p1: ATAdInfo?) {
                unitAdClick.invoke()
            }

            override fun onAdVideoStart(p0: ATNativeAdView?) {
            }

            override fun onAdVideoEnd(p0: ATNativeAdView?) {
            }

            override fun onAdVideoProgress(p0: ATNativeAdView?, p1: Int) {
            }

            override fun onDeeplinkCallback(p0: ATNativeAdView?, p1: ATAdInfo?, p2: Boolean) {
            }
        })

        atNativeContainer.removeAllViews()
        try {
            val nativePrepareInfo = ATNativePrepareExInfo()
            if (nativeAd.isNativeExpress) {
                nativeAd.renderAdContainer(atNativeContainer, null)
            } else {
                SelfRenderViewUtil.bindSelfRenderView(
                    activityCtx,
                    nativeAd.adMaterial,
                    selfRender,
                    nativePrepareInfo
                )
                nativeAd.renderAdContainer(atNativeContainer, selfRender)
            }
        } catch (e: java.lang.Exception) {}

        return true
    }
}