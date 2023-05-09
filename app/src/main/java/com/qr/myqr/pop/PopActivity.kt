package com.qr.myqr.pop

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import com.qr.myqr.appIns
import com.qr.myqr.basic.BaseCompatActivity
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
            AdsLoader.loadAd(appIns, AdPos.insOut, object :AdsListener() {
                override fun onLoadedAd(ad: BaseAd) {
                    showAd = ad
                    MvpFbObj.sm(appIns, Intent(appIns, PopActivity::class.java))
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
        when (ad) {
            is TopInterstitial -> {
                ad.show(this)
            }
            is TopNative -> {

            }
        }
        finish()
        AdsLoader.preloadAd(appIns, AdPos.insOut)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }


}