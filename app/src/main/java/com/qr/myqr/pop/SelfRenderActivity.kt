package com.qr.myqr.pop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.qr.myqr.basic.BaseCompatActivity
import com.qr.myqr.databinding.ActivitySelfRenderBinding
import com.qr.myqr.revenue.ad.TopNative

class SelfRenderActivity: BaseCompatActivity() {

    private lateinit var binding: ActivitySelfRenderBinding

    companion object {
        private var showNavAd: TopNative? = null
        fun open(activity: Activity, ad: TopNative): Boolean {
            showNavAd = ad
            val intent = Intent(activity, SelfRenderActivity::class.java)
            activity.startActivity(intent)
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelfRenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ad = showNavAd
        if (ad == null) {
            finish()
            return
        }

        val params = window.attributes
        window.setGravity(Gravity.CENTER)
       // params.width = (resources.displayMetrics.density * 330).toInt()
        params.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            dimAmount = 0f
            alpha = 1f
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        }
        window.attributes = params
        setFinishOnTouchOutside(false)

        ad.showAd(this, binding.nativeAdView, binding.nativeSelfRender.root)

        binding.nativeSelfRender.nativeAdClose.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                showNavAd?.onDestroy()
                showNavAd = null
                finish()
            }
        }
    }
}