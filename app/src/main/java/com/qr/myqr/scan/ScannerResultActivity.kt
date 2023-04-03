package com.qr.myqr.scan


import com.qr.myqr.basic.BasePage
import com.qr.myqr.copyText
import com.qr.myqr.databinding.ActivityScannerResultBinding
import com.qr.myqr.shareTextToOtherApp

class ScannerResultActivity : BasePage() {
    private var result = ""
    override val viewBinding by lazy { ActivityScannerResultBinding.inflate(layoutInflater) }
    override fun initView() {
        result = intent.getStringExtra("scanResult") ?: ""
        viewBinding.run {
            ivBack.setOnClickListener { onBackPressed() }
            tvShare.setOnClickListener {
                shareTextToOtherApp(result)
            }
            tvResult.text = result
            ivCopy.setOnClickListener {
                copyText(result)
            }
        }
    }
}