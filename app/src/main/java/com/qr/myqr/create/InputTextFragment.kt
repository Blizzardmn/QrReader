package com.qr.myqr.create

import com.qr.myqr.basic.BaseInputFragment
import com.qr.myqr.databinding.FragmentInputTextBinding

class InputTextFragment : BaseInputFragment() {
    override val viewBinding by lazy { FragmentInputTextBinding.inflate(layoutInflater) }

    override fun init() {

    }

    override fun getContent(): String {
        return viewBinding.edit.text.toString()
    }

}