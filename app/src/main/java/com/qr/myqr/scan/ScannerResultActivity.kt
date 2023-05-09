package com.qr.myqr.scan


import com.qr.myqr.basic.BasePage
import com.qr.myqr.copyText
import com.qr.myqr.databinding.ActivityScannerResultBinding
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopNative
import com.qr.myqr.shareTextToOtherApp

class ScannerResultActivity : BasePage() {
    private var result = ""
    override val binding by lazy { ActivityScannerResultBinding.inflate(layoutInflater) }
    override fun initView() {
        result = intent.getStringExtra("scanResult") ?: ""
        binding.run {
            ivBack.setOnClickListener { onBackPressed() }
            tvShare.setOnClickListener {
                shareTextToOtherApp(result)
            }
            tvResult.text = result
            ivCopy.setOnClickListener {
                copyText(result)
            }
        }

        showNavAd()
    }

    private fun showNavAd() {
        AdsLoader.loadAd(this, AdPos.navResult, object : AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (ad !is TopNative) return
                ad.showAd(this@ScannerResultActivity, binding.nativeAdView, binding.nativeSelfRender.root)

                AdsLoader.preloadAd(this@ScannerResultActivity, AdPos.navResult)
            }
        })
    }
}