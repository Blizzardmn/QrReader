package com.qr.myqr.create

import com.qr.myqr.R
import com.qr.myqr.basic.BaseInputFragment
import com.qr.myqr.databinding.FragmentLayoutWifiBinding

class WiFiInputFragment : BaseInputFragment() {
    private val wifiInfo = "WiFi:T:%s;P:%s;S:%s;"

    override val viewBinding by lazy { FragmentLayoutWifiBinding.inflate(layoutInflater) }

    override fun init() {
        viewBinding.run {
            check1.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    check2.isChecked = false
                    check3.isChecked = false
                    check4.isChecked = false
                }
            }
            check2.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    check1.isChecked = false
                    check3.isChecked = false
                    check4.isChecked = false
                }
            }
            check3.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    check2.isChecked = false
                    check1.isChecked = false
                    check4.isChecked = false
                }
            }
            check4.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    check2.isChecked = false
                    check1.isChecked = false
                    check3.isChecked = false
                }
            }
        }
    }

    private fun getType(): String {
        viewBinding.run {
            return if (check2.isChecked) {
                getString(R.string.wpa)
            } else if (check3.isChecked) {
                getString(R.string.wep)
            } else if (check4.isChecked) {
                getString(R.string.none)
            } else {
                getString(R.string.mpa_wpa2)
            }
        }

    }

    override fun getContent(): String {
        if (viewBinding.etSsid.text?.isNotEmpty() == true) {
            return String.format(
                wifiInfo,
                getType(),
                viewBinding.etPwd.text.toString(),
                viewBinding.etSsid.text.toString(),

            )
        } else {
//            Toast.makeText(context, "", Toast.LENGTH_LONG).show()
            return ""
        }
    }
}