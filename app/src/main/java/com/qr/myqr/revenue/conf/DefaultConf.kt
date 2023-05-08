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
                                type = idJson.optString("type"),
                                priority = idJson.optInt("priority")
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
            "id":"b62b035d8cb432",
            "type":"open",
            "priority":1
        }
    ],
    "ins_home":[
        {
            "id":"b62b014563dc89",
            "type":"ins",
            "priority":3
        }
    ],
    "ins_click":[
        {
            "id":"b62b023555ba04",
            "type":"ins",
            "priority":3
        }
    ],
    "nav_create":[
        {
            "id":"b62fb58f6b79e7",
            "type":"nav",
            "priority":3
        }
    ],
    "nav_result":[
        {
            "id":"b62b023b30350d",
            "type":"nav",
            "priority":1
        }
    ],
    "banner_main":[
        {
            "id":"b62b01a4cd4853",
            "type":"ban",
            "priority":3
        }
    ],
    "banner_scan":[
        {
            "id":"b62b023625cf59",
            "type":"ban",
            "priority":1
        }
    ]
}"""
        }
    }
}