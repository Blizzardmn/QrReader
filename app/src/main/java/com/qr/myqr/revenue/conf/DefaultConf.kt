package com.qr.myqr.revenue.conf

import android.annotation.SuppressLint
import android.util.Log
import com.qr.myqr.data.RemoteConfig
import com.qr.myqr.revenue.AdPos
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

open class DefaultConf {

    protected fun configByPos(@AdPos space: String): PosConf {
        val config = spaceMap[space]
        if (config == null) {
            tryParseRelease()
        }

        return spaceMap[space] ?: PosConf(space)
    }

    companion object {
        private val spaceMap = HashMap<String, PosConf>()

        fun tryParseLocal() {
            tryParse(localAds())
        }

        fun tryParseRelease() {
            var adConfig = RemoteConfig.ins.getAdsConf()
            Log.i("AdsLoader", "RemoteConfig: $adConfig")
            if (adConfig.isNullOrEmpty()) {
                adConfig = localAds()
            }
            tryParse(adConfig)
        }

        @SuppressLint("LogNotTimber")
        @Synchronized
        private fun tryParse(adConfig: String) {
            try {
                val content = JSONObject(adConfig)
                val names = content.names() ?: return
                val hashMap = HashMap<String, PosConf>()

                var name: String
                var idArray: JSONArray
                var idJson: JSONObject
                var idList: ArrayList<AdConf>
                for (i in 0 until names.length()) {
                    name = names.optString(i)
                    if (name.isNullOrEmpty()) continue
                    idArray = content.optJSONArray(name) ?: continue
                    idList = arrayListOf()
                    for (j in 0 until idArray.length()) {
                        idJson = idArray.optJSONObject(j) ?: continue
                        idList.add(
                            AdConf(
                                id = idJson.optString("id"),
                                type = idJson.optString("ad_type"),
                                priority = idJson.optInt("weight")
                            )
                        )
                    }
                    idList.sortBy { it.priority * -1 }
                    hashMap[name] = PosConf(name, idList)
                }

                synchronized(spaceMap) {
                    spaceMap.clear()
                    spaceMap.putAll(hashMap)
                }
            } catch (e: Exception) {
            }
        }

        private fun localAds(): String {
            return """{
    "open":[
        {
            "id":"ca-app-pub-3940256099942544/1033173712",
            "ad_type":"ins",
            "weight":1
        }
    ],
    "ins_scan":[
        {
            "id":"ca-app-pub-3940256099942544/1033173712",
            "ad_type":"ins",
            "weight":3
        }
    ],
    "ins_clean":[
        {
            "id":"ca-app-pub-3940256099942544/1033173712",
            "ad_type":"ins",
            "weight":3
        }
    ],
    "ins_conn":[
        {
            "id":"ca-app-pub-3940256099942544/1033173712",
            "ad_type":"ins",
            "weight":3
        }
    ],
    "nav_home":[
        {
            "id":"ca-app-pub-3940256099942544/2247696110",
            "ad_type":"nav",
            "weight":1
        }
    ],
    "nav_scan":[
        {
            "id":"ca-app-pub-3940256099942544/2247696110",
            "ad_type":"nav",
            "weight":3
        }
    ],
    "nav_result":[{
            "id":"ca-app-pub-3940256099942544/2247696110",
            "ad_type":"nav",
            "weight":1
        }
    ]
}"""
        }
    }
}