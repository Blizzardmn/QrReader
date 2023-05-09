package com.qr.myqr.main

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.qr.myqr.basic.BasePage
import com.qr.myqr.create.CreateActivity
import com.qr.myqr.databinding.ActivityMainBinding
import com.qr.myqr.history.HistoryActivity
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopBanner
import com.qr.myqr.revenue.ad.TopInterstitial
import com.qr.myqr.scan.ScanningActivity
import com.qr.myqr.toActivity
import com.qr.myqr.tools.checkCameraPermission

class MainActivity : BasePage() {
    override val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView() {
        binding.run {
            ivHistory.setOnClickListener {
                toActivity<HistoryActivity>()
            }
            ivScannerBg.setOnClickListener {
                checkCameraPermission{
                    toActivity<ScanningActivity>()
                }
            }
        }
        setRv()
        showBanner()
    }

    override fun onResume() {
        super.onResume()

        AdsLoader.preloadAd(this, AdPos.navCreate)
        AdsLoader.preloadAd(this, AdPos.navResult)
        AdsLoader.preloadAd(this, AdPos.bannerOther)
    }

    private fun setRv() {
        binding.run {
            rv.adapter = MainAdapter {
                showIns {
                    toActivity<CreateActivity>(bundle = Bundle().apply {
                        putSerializable("bean", it)
                    })
                }
            }
            rv.layoutManager = GridLayoutManager(this@MainActivity, 3)
        }
    }

    private fun showBanner() {
        AdsLoader.loadAd(this, AdPos.bannerMain, object :AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (ad !is TopBanner) return
                ad.show(binding.cardAd)
            }
        })
    }

    private fun showIns(nextDo: () -> Unit) {
        AdsLoader.loadAd(this, AdPos.insHome, object :AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (ad !is TopInterstitial) return
                if (!ad.show(this@MainActivity)) {
                    nextDo.invoke()
                }

                AdsLoader.preloadAd(this@MainActivity, AdPos.insHome)
            }

            override fun onLoadErr(msg: String) {
                nextDo.invoke()
            }

            override fun onDismiss() {
                nextDo.invoke()
            }
        }, onlyCache = true)
    }
}