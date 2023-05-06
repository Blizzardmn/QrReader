package com.qr.myqr.revenue

import com.qr.myqr.revenue.ad.BaseAd

open class AdsListener {
    open fun onLoadedAd(ad: BaseAd) {}

    open fun onLoadErr(msg: String = "") {}

    open fun onClicked() {}

    open fun onShown() {}

    open fun onDismiss() {}
}