package com.qr.myqr.page

import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.qr.myqr.basic.BasePage
import com.qr.myqr.databinding.ActivityFirstBinding
import com.qr.myqr.main.MainActivity
import com.qr.myqr.toActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirstActivity : BasePage() {
    override val viewBinding by lazy { ActivityFirstBinding.inflate(layoutInflater) }

    override fun initView() {
        lifecycleScope.launch {
            delay(1000)
            toActivity<MainActivity>()
            finish()
        }
    }

    override fun onBackPressed() {

    }
}