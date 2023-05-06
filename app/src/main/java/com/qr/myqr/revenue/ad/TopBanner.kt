package com.qr.myqr.revenue.ad

import android.content.Context
import android.view.ViewGroup
import com.anythink.banner.api.ATBannerExListener
import com.anythink.banner.api.ATBannerView
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.conf.AdConf

class TopBanner(@AdPos adPos: String, adConf: AdConf): BaseAd(adPos, adConf) {

    var unitAdLoaded: () -> Unit = {}
    var unitAdError: ()-> Unit = {}

    private var mBanner: ATBannerView? = null
    override fun loaded(ad: Any, adsListener: AdsListener) {
        super.loaded(ad, adsListener)
        mBanner = ad as? ATBannerView
    }

    fun show(adContainer: ViewGroup) {
        adContainer.removeAllViews()
        if (mBanner != null) {
            adContainer.addView(mBanner)
        }
    }

    val listener = object: ATBannerExListener {
        override fun onBannerLoaded() {
            unitAdLoaded.invoke()
        }

        override fun onBannerFailed(p0: AdError?) {
            unitAdError.invoke()
        }

        override fun onBannerClicked(p0: ATAdInfo?) {
            unitAdClick.invoke()
        }

        override fun onBannerShow(p0: ATAdInfo?) {
            unitAdShown.invoke()
        }

        override fun onBannerClose(p0: ATAdInfo?) {
            unitAdClose.invoke()
        }

        override fun onBannerAutoRefreshed(p0: ATAdInfo?) {
        }

        override fun onBannerAutoRefreshFail(p0: AdError?) {
        }

        override fun onDeeplinkCallback(p0: Boolean, p1: ATAdInfo?, p2: Boolean) {
        }

        override fun onDownloadConfirm(p0: Context?, p1: ATAdInfo?, p2: ATNetworkConfirmInfo?) {
        }
    }

}