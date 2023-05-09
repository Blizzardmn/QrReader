package com.qr.myqr.basic

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.viewbinding.ViewBinding

abstract class BasePage : BaseCompatActivity() {

    abstract val binding: ViewBinding
    abstract fun initView()

    private var isPagePaused = false
    fun isActivityPaused(): Boolean {
        return isPagePaused
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDensity()
        setContentView(binding.root)
        initView()
    }

    override fun onResume() {
        super.onResume()
        isPagePaused = false
    }

    override fun onPause() {
        super.onPause()
        isPagePaused = true
    }

    override fun onStop() {
        super.onStop()
        isPagePaused = true
    }

    //resources.displayMetrics.density
    private fun setDensity() {
        val metrics: DisplayMetrics = resources.displayMetrics
        val td = metrics.heightPixels / 812f
        val dpi = (375 * td).toInt()
        metrics.density = td
        metrics.scaledDensity = td
        metrics.densityDpi = dpi
    }

}