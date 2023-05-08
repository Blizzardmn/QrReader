package com.qr.myqr.revenue.load

import android.app.Activity
import android.content.Context
import android.util.Log
import com.anythink.banner.api.ATBannerView
import com.anythink.basead.ui.AppRatingView.dip2px
import com.anythink.core.api.*
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.nativead.api.ATNative
import com.anythink.nativead.api.ATNativeNetworkListener
import com.anythink.splashad.api.ATSplashAd
import com.qr.myqr.dip2px
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.ad.*
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.conf.AdConf

class TopOnLoader {

    private val tag = "TagTopOnLoader"

    fun loadOpen(ctx: Context, @AdPos space: String, config: AdConf, adsListener: AdsListener, loadAction: (ad: BaseAd?) -> Unit) {
        val localOpenAd = TopOpen(space, config)
        //localOpenAd.redefineListener(adsListener)
        localOpenAd.unitAdLoaded = {
            loadAction.invoke(localOpenAd)
        }
        localOpenAd.unitAdError = {
            loadAction.invoke(null)
        }

        val splashAT = ATSplashAd(ctx, config.id, localOpenAd.loadListener, 5000, "")
        localOpenAd.loaded(splashAT, adsListener)
        val localMap = HashMap<String, Any>()
        //localMap[ATAdConst.KEY.AD_WIDTH] = ctx.resources.displayMetrics.widthPixels
        //localMap[ATAdConst.KEY.AD_HEIGHT] = ctx.resources.displayMetrics.widthPixels
        localMap[ATAdConst.KEY.AD_CLICK_CONFIRM_STATUS] = true
        splashAT.setLocalExtra(localMap)

        splashAT.setAdSourceStatusListener(ATAdSourceStatusListenerImpl())
        splashAT.loadAd()
    }

    fun loadBanner(ctx: Context, @AdPos space: String, config: AdConf, adsListener: AdsListener, loadAction: (ad: BaseAd?) -> Unit) {
        if (ctx !is Activity) return
        val localBanner = TopBanner(space, config)
        //localBanner.redefineListener(adsListener)
        localBanner.unitAdLoaded = {
            loadAction.invoke(localBanner)
        }
        localBanner.unitAdError = {
            loadAction.invoke(null)
        }

        val bannerView = ATBannerView(ctx)
        bannerView.setPlacementId(config.id)
        val localMap = HashMap<String, Any>()
        val padding: Int = ctx.dip2px(12f)
        localMap[ATAdConst.KEY.AD_WIDTH] = ctx.resources.displayMetrics.widthPixels - 2 * padding
        localMap[ATAdConst.KEY.AD_HEIGHT] = ctx.dip2px(100f)
        bannerView.setLocalExtra(localMap)

        localBanner.loaded(bannerView, adsListener)
        bannerView.setBannerAdListener(localBanner.listener)
        bannerView.setAdSourceStatusListener(ATAdSourceStatusListenerImpl())
        bannerView.loadAd()
    }

    fun loadInterstitial(ctx: Context, @AdPos space: String, config: AdConf, adsListener: AdsListener, loadAction: (ad: BaseAd?) -> Unit) {
        val localInterstitial = TopInterstitial(space, config)
        localInterstitial.unitAdLoaded = {
            loadAction.invoke(localInterstitial)
        }
        localInterstitial.unitAdError = {
            loadAction.invoke(null)
        }

        val interstitialAd = ATInterstitial(ctx, config.id)
        localInterstitial.loaded(interstitialAd, adsListener)

        interstitialAd.setAdListener(localInterstitial.adListener)
        val localMap = HashMap<String, Any>()
        interstitialAd.setLocalExtra(localMap)
        interstitialAd.load()
    }

    fun loadNative(ctx: Context, @AdPos space: String, config: AdConf, adsListener: AdsListener, loadAction: (ad: BaseAd?) -> Unit) {
        val localNative = TopNative(space, config)

        val atNative = ATNative(ctx, config.id, object :ATNativeNetworkListener {
            override fun onNativeAdLoaded() {
                loadAction.invoke(localNative)
            }

            override fun onNativeAdLoadFail(p0: AdError?) {
                loadAction.invoke(null)
            }
        })
        localNative.loaded(atNative, adsListener)

        val localExtra: MutableMap<String, Any> = java.util.HashMap()
        val adViewWidth = ctx.resources.displayMetrics.widthPixels
        val adViewHeight = adViewWidth * 3 / 4
        localExtra[ATAdConst.KEY.AD_WIDTH] = adViewWidth
        localExtra[ATAdConst.KEY.AD_HEIGHT] = adViewHeight

        atNative.setLocalExtra(localExtra)
        atNative.makeAdRequest()
    }


    class ATAdSourceStatusListenerImpl : ATAdSourceStatusListener {
        private val TAG = "TagTopOnLoader"
        override fun onAdSourceBiddingAttempt(adInfo: ATAdInfo) {
            Log.i(TAG, "onAdSourceBiddingAttempt: $adInfo")
        }

        override fun onAdSourceBiddingFilled(adInfo: ATAdInfo) {
            Log.i(TAG, "onAdSourceBiddingFilled: $adInfo")
        }

        override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: AdError) {
            Log.i(TAG, "onAdSourceBiddingFail Info: $adInfo")
            if (adError != null) {
                Log.i(TAG, "onAdSourceBiddingFail error: " + adError.fullErrorInfo)
            }
        }

        override fun onAdSourceAttempt(adInfo: ATAdInfo) {
            Log.i(TAG, "onAdSourceAttempt: $adInfo")
        }

        override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
            Log.i(TAG, "onAdSourceLoadFilled: $adInfo")
        }

        override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: AdError) {
            Log.i(TAG, "onAdSourceLoadFail Info: $adInfo")
            Log.i(TAG, "onAdSourceLoadFail error: " + adError.fullErrorInfo)
        }
    }
}