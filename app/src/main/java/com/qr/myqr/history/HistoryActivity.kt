package com.qr.myqr.history

import com.qr.myqr.R
import com.qr.myqr.basic.BasePage
import com.qr.myqr.databinding.ActivityHistoryBinding
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopBanner

class HistoryActivity : BasePage() {
    override val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }

    override fun initView() {
        binding.run {
            ivBack.setOnClickListener { onBackPressed() }
            viewPager.isUserInputEnabled = false
            viewPager.adapter = PagerViewAdapter(this@HistoryActivity)
            checkScan.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkScan.setTextColor(getColor(R.color.white))
                    checkCreate.setTextColor(getColor(R.color.color_469CF5))
                    checkCreate.isChecked = false
                    viewPager.currentItem = 0
                }
            }
            checkCreate.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkCreate.setTextColor(getColor(R.color.white))
                    checkScan.setTextColor(getColor(R.color.color_469CF5))
                    checkScan.isChecked = false
                    viewPager.currentItem = 1
                }
            }
        }

        showBanner()
    }

    private fun showBanner() {
        AdsLoader.loadAd(this, AdPos.bannerOther, object : AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (ad !is TopBanner) return
                ad.show(binding.cardAd)
            }
        })
    }
}