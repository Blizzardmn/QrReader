package com.qr.myqr.page

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import com.qr.myqr.basic.BaseCompatActivity

class ExchangeActivity: BaseCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        //设定一像素的activity
        window.setGravity(Gravity.START or Gravity.TOP)
        window.attributes = window.attributes.apply {
            x = 0
            y = 0
            height = 1
            width = 1
        }

        val intent = Intent(Settings.ACTION_SETTINGS)
        startActivity(intent)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}