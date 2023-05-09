package com.qr.myqr.revenue.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashExListener
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.conf.AdConf

class TopOpen(@AdPos adPos: String, adConf: AdConf): BaseAd(adPos, adConf) {

    val loadListener = ATSplashLoadListenerImpl()
    var unitAdLoaded: () -> Unit = {}
    var unitAdError: ()-> Unit = {}

    private var mSplashAT: ATSplashAd? = null
    override fun loaded(ad: Any, adsListener: AdsListener) {
        super.loaded(ad, adsListener)
        mSplashAT = ad as? ATSplashAd
    }

    fun show(activity: Activity, container: ViewGroup): Boolean {
        mSplashAT
        val localMap = HashMap<String, Any>()
        val layoutParams = container.layoutParams
        localMap[ATAdConst.KEY.AD_WIDTH] = layoutParams.width
        localMap[ATAdConst.KEY.AD_HEIGHT] = layoutParams.height
        mSplashAT?.setLocalExtra(localMap)

        //建议进入可展示广告场景后调用entryAdScenario(placementId,scenarioId)统计当前广告位的缓存状态
        //scenario是指广告展示场景（非必传，可以直接传null）
        ATSplashAd.entryAdScenario(adConf.placementId, "")
        return if (mSplashAT?.isAdReady == true) {
            mSplashAT?.show(activity, container)
            true
        } else false
    }

    inner class ATSplashLoadListenerImpl : ATSplashExListener {
        private val tag = "TagTopOnLoader"
        override fun onAdLoaded(isTimeout: Boolean) {
            Log.i(tag, "onAdLoaded---------isTimeout:$isTimeout")
            unitAdLoaded.invoke()
        }

        override fun onAdLoadTimeout() {
            Log.i(tag, "onAdLoadTimeout---------")
            unitAdError.invoke()
        }

        override fun onNoAdError(adError: AdError) {
            Log.i(tag, "onNoAdError---------:" + adError.fullErrorInfo)
            unitAdError.invoke()
        }

        override fun onAdShow(entity: ATAdInfo) {
            Log.i(tag, "onAdShow---------:$entity")
            unitAdShown.invoke()
        }

        override fun onAdClick(entity: ATAdInfo) {
            Log.i(tag, "onAdClick---------:$entity")
            unitAdClick.invoke()
        }

        override fun onAdDismiss(entity: ATAdInfo, splashAdExtraInfo: ATSplashAdExtraInfo) {
            Log.i(tag, "onAdDismiss---------:$entity")
            unitAdClose.invoke()
        }

        override fun onDeeplinkCallback(entity: ATAdInfo, isSuccess: Boolean) {
            Log.i(tag, "onDeeplinkCallback---------：$entity isSuccess = $isSuccess")
        }

        override fun onDownloadConfirm(
            context: Context,
            adInfo: ATAdInfo,
            networkConfirmInfo: ATNetworkConfirmInfo
        ) {
            Log.i(tag, "onDownloadConfirm--------- entity = $adInfo")
        }
    }
}