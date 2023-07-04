package com.qr.myqr.revenue.ad

import android.app.Activity
import android.content.Context
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.interstitial.api.ATInterstitialExListener
import com.facebook.appevents.AppEventsLogger
import com.qr.myqr.appIns
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.conf.AdConf
import java.math.BigDecimal
import java.util.*

class TopInterstitial(@AdPos adPos: String, adConf: AdConf): BaseAd(adPos, adConf) {

    var unitAdLoaded: () -> Unit = {}
    var unitAdError: ()-> Unit = {}

    private var mInterstitial: ATInterstitial? = null
    override fun loaded(ad: Any, adsListener: AdsListener) {
        super.loaded(ad, adsListener)
        mInterstitial = ad as? ATInterstitial
    }

    fun show(activity: Activity): Boolean {
        val interstitial = mInterstitial
        if (interstitial == null || !interstitial.isAdReady) return false
        ATInterstitial.entryAdScenario(adConf.id, adConf.placeId)
        interstitial.show(activity)
        return true
    }

    override fun isAdReady(): Boolean {
        return mInterstitial?.isAdReady == true
    }

    val adListener = object :ATInterstitialExListener {
        override fun onInterstitialAdLoaded() {
            unitAdLoaded.invoke()
        }

        override fun onInterstitialAdLoadFail(p0: AdError?) {
            unitAdError.invoke()
        }

        override fun onInterstitialAdClicked(p0: ATAdInfo?) {
            unitAdClick.invoke()
        }

        override fun onInterstitialAdShow(p0: ATAdInfo?) {
            unitAdShown.invoke()
            if (p0 == null) return
            AppEventsLogger.newLogger(appIns).logPurchase(
                BigDecimal.valueOf(p0.publisherRevenue),
                Currency.getInstance(p0.currency)
            )
        }

        override fun onInterstitialAdClose(p0: ATAdInfo?) {
            unitAdClose.invoke()
        }

        override fun onInterstitialAdVideoStart(p0: ATAdInfo?) {
        }

        override fun onInterstitialAdVideoEnd(p0: ATAdInfo?) {
        }

        override fun onInterstitialAdVideoError(p0: AdError?) {
        }

        override fun onDeeplinkCallback(p0: ATAdInfo?, p1: Boolean) {
        }

        override fun onDownloadConfirm(p0: Context?, p1: ATAdInfo?, p2: ATNetworkConfirmInfo?) {
        }
    }
}