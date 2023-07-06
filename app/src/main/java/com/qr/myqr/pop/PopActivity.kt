package com.qr.myqr.pop

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
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

open class PopActivity: BaseCompatActivity() {

    companion object {

        private var showAd: BaseAd? = null

        fun open() {
            val adsPos = if (RemoteConfig.ins.isNavOuterEnable()) {
                if (AppCache.ins.lastAdInsStyle) AdPos.navOut else AdPos.insOut
            } else {
                AdPos.insOut
            }
            showAd = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //overridePendingTransition(0, 0)
        //设定一像素的activity
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
            val params = attributes
            params.height = 1
            params.width = 1
            params.gravity = Gravity.TOP
            attributes = params
        }

        FirebaseEvent.event("out_page_imp")
        AdsLoader.loadAd(appIns, AdPos.insOut, object :AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                FirebaseEvent.event("out_req_adloaded")
                showAd(ad)
            }

            override fun onShown() {
                FirebaseEvent.event("out_ad_imp_real")
                //finish()
            }

            override fun onDismiss() {
                checkFinish()
            }

            override fun onClicked() {
                FirebaseEvent.event("out_ad_clk")
            }

            override fun onLoadErr(msg: String) {
                FirebaseEvent.event("out_req_adloaderr")
                checkFinish()
            }

            private fun checkFinish() {
                if (isFinishing || isDestroyed) return
                finish()
            }
        }, onlyCache = true)
    }

    private fun showAd(ad: BaseAd) {
        var isIns = true
        val shown = when (ad) {
            is TopInterstitial -> {
                AppCache.ins.lastAdInsStyle = true
                ad.show(this@PopActivity)
            }
            is TopNative -> {
                isIns = false
                AppCache.ins.lastAdInsStyle = false
                SelfRenderActivity.open(this@PopActivity, ad)
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
    }

    override fun onBackPressed() {
    }


}