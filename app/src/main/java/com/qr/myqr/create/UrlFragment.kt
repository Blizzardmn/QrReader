package com.qr.myqr.create

import androidx.core.widget.addTextChangedListener
import com.qr.myqr.R
import com.qr.myqr.basic.BaseInputFragment
import com.qr.myqr.databinding.FragmentUrlBinding
import com.qr.myqr.main.UiBean

class UrlFragment(val uiBean: UiBean) : BaseInputFragment() {
    private val URL_TIKTOK = "https://www.tiktok.com"
    private val URL_INS = "https://www.instagram.com"
    private val URL_TWITTER = "https://twitter.com"
    private val URL_PAYPAL = "https://www.paypal.com"
    private val URL_FACEBOOK = "https://www.facebook.com"
    private var normalUrl = getUrl()
    private var userName = ""
    override val viewBinding by lazy { FragmentUrlBinding.inflate(layoutInflater) }

    override fun init() {
        viewBinding.run {
            if (uiBean.isFacebook()) {
                check1.text = getString(R.string.facebook_id)
            }
            check1.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    check2.isChecked = false
                    etUrl.setText(userName)
                }
            }
            check2.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    check1.isChecked = false
                    etUrl.setText(normalUrl)
                }
            }
            etUrl.addTextChangedListener {
                if (check1.isChecked) {
                    userName = etUrl.text.toString()
                } else {
                    normalUrl = etUrl.text.toString()
                }
            }
        }
    }

    override fun getContent(): String {
        if (viewBinding.check1.isChecked) {
            return getUserNameUrl()
        } else {
            return viewBinding.etUrl.text.toString()
        }
    }

    private fun getUrl(): String {
        return when (uiBean.icon) {
            R.drawable.ic_tiktok -> {
                URL_TIKTOK
            }
            R.drawable.ic_ins -> {
                URL_INS
            }
            R.drawable.ic_twitter -> {
                URL_TWITTER
            }
            R.drawable.ic_pay -> {
                URL_PAYPAL
            }
            R.drawable.ic_facebook -> {
                URL_FACEBOOK
            }
            else -> {
                ""
            }
        }
    }

    private fun getUserNameUrl(): String {
        userName = viewBinding.etUrl.text.toString()
        return when (uiBean.icon) {
            R.drawable.ic_tiktok -> {
                "$URL_TIKTOK/@$userName"
            }
            R.drawable.ic_ins -> {
                "$URL_INS/$userName"
            }
            R.drawable.ic_twitter -> {
                "$URL_TWITTER/$userName"
            }
            R.drawable.ic_pay -> {
                "$URL_PAYPAL/$userName"
            }
            R.drawable.ic_facebook -> {
                "$URL_FACEBOOK/profile.php?id=$userName"
            }
            else -> {
                ""
            }
        }
    }

}