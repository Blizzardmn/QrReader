package com.qr.myqr.history

import com.qr.myqr.R
import com.qr.myqr.basic.BasePage
import com.qr.myqr.databinding.ActivityHistoryBinding

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
    }
}