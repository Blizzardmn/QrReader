package com.qr.myqr.data

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.qr.myqr.appIns

object FirebaseEvent {

    fun event(event: String) {
        FirebaseAnalytics.getInstance(appIns).logEvent(event, null)
    }

    fun event(event: String, key: String, value: String) {
        val bundle = Bundle()
        bundle.putString(key, value)
        FirebaseAnalytics.getInstance(appIns).logEvent(event, bundle)
    }

}