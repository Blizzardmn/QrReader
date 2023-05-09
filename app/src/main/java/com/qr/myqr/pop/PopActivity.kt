package com.qr.myqr.pop

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import com.qr.myqr.appIns
import com.qr.myqr.basic.BaseCompatActivity
import com.qr.myqr.data.AppCache
import com.qr.myqr.data.FirebaseEvent
import com.qr.myqr.data.RemoteConfig
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopInterstitial
import com.qr.myqr.revenue.ad.TopNative
import com.reader.multiple.vb.MvpFbObj

class PopActivity: BaseCompatActivity() {

    companion object {

        private var showAd: BaseAd? = null

        fun open() {
            val adsPos = if (RemoteConfig.ins.isNavOuterEnable()) {
                if (AppCache.ins.lastAdInsStyle) AdPos.navOut else AdPos.insOut
            } else {
                AdPos.insOut
            }
            AdsLoader.loadAd(appIns, adsPos, object :AdsListener() {
                override fun onLoadedAd(ad: BaseAd) {
                    showAd = ad
                    MvpFbObj.sm(appIns, Intent(appIns, PopActivity::class.java))
                }

                override fun onClicked() {
                    FirebaseEvent.event("out_ad_clk")
                }
            }, onlyCache = true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        //设定一像素的activity
        window.setGravity(Gravity.START or Gravity.TOP)
        window.attributes = window.attributes.apply {
            x = 0
            y = 0
            height = 1
            width = 1
        }

        val ad = showAd
        if (ad == null) {
            finish()
            return
        }
        var isIns = true
        val shown = when (ad) {
            is TopInterstitial -> {
                AppCache.ins.lastAdInsStyle = true
                ad.show(this)
            }
            is TopNative -> {
                isIns = false
                AppCache.ins.lastAdInsStyle = false
                SelfRenderActivity.open(this, ad)
            }

            else -> false
        }
        if (isIns) {
            AdsLoader.preloadAd(appIns, AdPos.insOut)
        } else {
            AdsLoader.preloadAd(appIns, AdPos.navOut)
        }
        if (shown) {
            FirebaseEvent.event("out_ad_imp")
        }
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }


}