package com.qr.myqr.page

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.qr.myqr.appIns
import com.qr.myqr.basic.AnimatorListener
import com.qr.myqr.basic.BasePage
import com.qr.myqr.databinding.ActivityFirstBinding
import com.qr.myqr.main.MainActivity
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopOpen
import com.qr.myqr.toActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirstActivity : BasePage() {
    override val binding by lazy { ActivityFirstBinding.inflate(layoutInflater) }

    companion object {
        fun hotStart(activity: Activity) {
            val intent = Intent(activity, FirstActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private var isAdImpression = false
    override fun initView() {
        val openNextLogic = {
            startMain()
        }
        runAnim(11_000L) {
            if (isAdImpression) return@runAnim
            if (isActivityPaused()) {
                return@runAnim
            }
            openNextLogic.invoke()
        }
        lifecycleScope.launch {
            delay(200L)
            openAdLogic {
                openNextLogic.invoke()
            }
        }
        AdsLoader.preloadAd(appIns, AdPos.insOut)
        AdsLoader.preloadAd(this, AdPos.insHome)
        AdsLoader.preloadAd(this, AdPos.insClick)
        AdsLoader.preloadAd(this, AdPos.bannerMain)
    }

    private fun startMain() {
        toActivity<MainActivity>()
        finish()
    }

    private fun openAdLogic(adNextAction: () -> Unit) {
        isAdImpression = false

        fun preloadOpen() {
            AdsLoader.preloadAd(appIns, AdPos.open)
        }

        AdsLoader.loadAd(appIns, AdPos.open, object : AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (isActivityPaused()) {
                    AdsLoader.add2Cache(AdPos.open, ad)
                    return
                }
                preloadOpen()
                if (ad !is TopOpen || !ad.show(this@FirstActivity, binding.splashAdContainer)) {
                    adNextAction.invoke()
                }
            }

            override fun onLoadErr(msg: String) {
                preloadOpen()
                if (isActivityPaused()) return
                adNextAction.invoke()
            }

            override fun onDismiss() {
                if (appIns.isAppForeground()) adNextAction.invoke() else finish()
            }

            override fun onShown() {
                isAdImpression = true
            }
        })
    }

    private var valueAni: ValueAnimator? = null
    private fun runAnim(long: Long, action: () -> Unit) {
        valueAni?.cancel()
        valueAni = ValueAnimator.ofInt(binding.progressBar.progress, 100)
        valueAni?.duration = long
        valueAni?.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(p0: ValueAnimator) {
                (p0.animatedValue as? Int)?.apply {
                    binding.progressBar.progress = this
                }
            }
        })
        valueAni?.addListener(object : AnimatorListener() {
            private var isCanceled = false
            override fun onAnimationCancel(animation: Animator) {
                isCanceled = true
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!isCanceled) {
                    action.invoke()
                }
            }
        })
        valueAni?.start()
    }

    override fun onBackPressed() {

    }
}