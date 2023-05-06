package com.qr.myqr.revenue.conf

import com.qr.myqr.revenue.AdPos

data class PosConf(@AdPos val space: String, val lists: ArrayList<AdConf> = arrayListOf())