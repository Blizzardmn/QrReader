package com.qr.myqr.create

import com.qr.myqr.basic.BaseInputFragment
import com.qr.myqr.databinding.FragmentPhoneBinding

class PhoneFragment : BaseInputFragment() {
    override val viewBinding by lazy { FragmentPhoneBinding.inflate(layoutInflater) }

    override fun init() {
    }

    override fun getContent(): String {
        return viewBinding.etPhone.text.toString()
    }
}