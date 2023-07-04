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
                                placeId = idJson.optString("place"),
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
            return releaseConf
        }

        private const val releaseConf = """{
    "open":[
        {
            "place":"f642f78eed0f6f",
            "id":"b642e6e2e4e327",
            "type":"open",
            "priority":2
        },
        {
            "place":"f64940ef981b1f",
            "id":"b64940e0b99f22",
            "type":"ins",
            "priority":1
        }
    ],
    "int_out":[
        {
            "place":"f64940f04a1bfb",
            "id":"b64940e0b99f22",
            "type":"ins",
            "priority":1
        }
    ],
    "int_home":[
        {
            "place":"f64940f0f5e637",
            "id":"b64940e0b99f22",
            "type":"ins",
            "priority":1
        }
    ],
    "int_click":[
        {
            "place":"f64940f19cfd04",
            "id":"b64940e0b99f22",
            "type":"ins",
            "priority":1
        }
    ],
    "nav_out":[
        {
            "place":"f645a0df8ad379",
            "id":"b642e69a09e4ed",
            "type":"nav",
            "priority":1
        }
    ]
}"""

        private val testConf = """
        {
            "open":[
                {
                    "place":"",
                    "id":"b62b035d8cb432",
                    "type":"open",
                    "priority":2
                }
            ],
            "int_out":[
                {
                    "place":"",
                    "id":"b62b014563dc89",
                    "type":"ins",
                    "priority":1
                }
            ],
            "int_home":[
                {
                    "place":"",
                    "id":"b62b014563dc89",
                    "type":"ins",
                    "priority":1
                }
            ],
            "int_click":[
                {
                    "place":"",
                    "id":"b62b014563dc89",
                    "type":"ins",
                    "priority":3
                }
            ],
            "nav_create":[
                {
                    "place":"",
                    "id":"b62fb58f6b79e7",
                    "type":"nav",
                    "priority":1
                }
            ],
            "nav_result":[
                {
                    "place":"",
                    "id":"b62fb58f6b79e7",
                    "type":"nav",
                    "priority":4
                }
            ],
            "nav_out":[
                {
                    "place":"",
                    "id":"b62fb58f6b79e7",
                    "type":"nav",
                    "priority":1
                }
            ],
            "banner_main":[
                {
                    "place":"",
                    "id":"b62b01a4cd4853",
                    "type":"ban",
                    "priority":1
                }
            ],
            "banner_other":[
                {
                    "place":"",
                    "id":"b62b01a4cd4853",
                    "type":"ban",
                    "priority":2
                }
            ]
        }
    """.trimIndent()
    }
}