package com.qr.myqr.create

import android.os.Bundle
import android.widget.Toast
import com.qr.myqr.R
import com.qr.myqr.basic.BaseInputFragment
import com.qr.myqr.basic.BasePage
import com.qr.myqr.data.StartupProvider
import com.qr.myqr.databinding.ActivityCreateQrBinding
import com.qr.myqr.db.CreateEntity
import com.qr.myqr.db.RoomImpl
import com.qr.myqr.main.UiBean
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopInterstitial
import com.qr.myqr.revenue.ad.TopNative
import com.qr.myqr.toActivity

class CreateActivity : BasePage() {
    private val daoCreate by lazy { RoomImpl.createDao }
    override val binding by lazy { ActivityCreateQrBinding.inflate(layoutInflater) }
    private lateinit var uiBean: UiBean
    private lateinit var inputContent: InputContent

    override fun initView() {
        uiBean = intent.getSerializableExtra("bean") as UiBean
        binding.run {
            icBack.setOnClickListener { onBackPressed() }
            ivType.setImageResource(uiBean.icon)
            tvTitle.text = uiBean.name
            createBtn.setOnClickListener {
                val content = inputContent.getContent()
                if (content.isEmpty()) {
                    Toast.makeText(
                        this@CreateActivity,
                        getString(R.string.input_content_tips),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    showIns {
                        toCreateResultActivity(content)
                    }
                }
            }
            initFragment()
        }

        showNavAd()
    }

    private fun initFragment() {
        when (uiBean.icon) {
            R.drawable.ic_wifi -> {
                commitFragment(WiFiInputFragment())
            }
            R.drawable.ic_tel, R.drawable.ic_wathsapp -> {
                commitFragment(PhoneFragment())
            }
            R.drawable.ic_tiktok, R.drawable.ic_ins, R.drawable.ic_twitter, R.drawable.ic_pay, R.drawable.ic_facebook -> {
                commitFragment(UrlFragment(uiBean))
            }
            else -> {
                commitFragment(InputTextFragment())
            }
        }
    }

    private fun commitFragment(fragment: BaseInputFragment) {
        inputContent = fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commitNow()
    }

    private fun toCreateResultActivity(s: String) {
        val bean = daoCreate.getScanEntityByType(uiBean.type.ordinal)
        if (bean == null) {
            daoCreate.add(CreateEntity().apply {
                type = uiBean.type.ordinal
                content = s
            })
        } else {
            bean.content = s
            daoCreate.update(bean)
        }
        toActivity<CreateResultActivity>(bundle = Bundle().apply {
            putString("content", s)
        })
        finish()
    }

    private fun showIns(nextDo: () -> Unit) {
        if (!StartupProvider.isOneShotEnable()) {
            nextDo.invoke()
            return
        }
        AdsLoader.loadAd(this, AdPos.insClick, object :AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (ad !is TopInterstitial) return
                if (!ad.show(this@CreateActivity)) {
                    nextDo.invoke()
                }
            }

            override fun onLoadErr(msg: String) {
                nextDo.invoke()
            }

            override fun onDismiss() {
                nextDo.invoke()
            }
        }, onlyCache = true)
    }

    private fun showNavAd() {
        AdsLoader.preloadAd(this, AdPos.navResult)
        AdsLoader.loadAd(this, AdPos.navCreate, object :AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (ad !is TopNative) return
                ad.showAd(this@CreateActivity, binding.nativeAdView, binding.nativeSelfRender.root)
            }
        })
    }
}