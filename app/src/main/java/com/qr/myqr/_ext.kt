package com.qr.myqr

import android.content.Context


fun Context.dip2px(dipValue: Float): Int {
    val scale: Float = this.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}