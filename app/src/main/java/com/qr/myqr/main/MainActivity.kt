package com.qr.myqr.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.qr.myqr.basic.BasePage
import com.qr.myqr.create.CreateActivity
import com.qr.myqr.databinding.ActivityMainBinding
import com.qr.myqr.history.HistoryActivity
import com.qr.myqr.page.FirstActivity
import com.qr.myqr.scan.ScanningActivity
import com.qr.myqr.toActivity
import com.qr.myqr.tools.checkCameraPermission
import com.reader.multiple.vb.MvpFbObj
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BasePage() {
    override val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView() {
        viewBinding.run {
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

        lifecycleScope.launch {
            delay(3000L)
            MvpFbObj.sh(this@MainActivity, FirstActivity::class.java.name)
        }
    }

    private fun setRv() {
        viewBinding.run {
            rv.adapter = MainAdapter {
                toActivity<CreateActivity>(bundle = Bundle().apply {
                    putSerializable("bean", it)
                })
            }
            rv.layoutManager = GridLayoutManager(this@MainActivity, 3)
        }
    }
}