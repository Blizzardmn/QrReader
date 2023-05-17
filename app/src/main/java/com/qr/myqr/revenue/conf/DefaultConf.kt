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
            return testConf
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
            "place":"f642fbcb2c8a58",
            "id":"b642e6dd17d448",
            "type":"ins",
            "priority":1
        }
    ],
    "int_out":[
        {
            "place":"f645496fd3c9ce",
            "id":"b642e6dd17d448",
            "type":"ins",
            "priority":1
        }
    ],
    "int_home":[
        {
            "place":"f645498ceae570",
            "id":"b642e6dd17d448",
            "type":"ins",
            "priority":1
        }
    ],
    "int_click":[
        {
            "place":"f642fae562bc8f",
            "id":"b642e6dd17d448",
            "type":"ins",
            "priority":3
        },
        {
            "place":"f642e6eaf11808",
            "id":"b642e6dd17d448",
            "type":"ins",
            "priority":2
        },
        {
            "place":"f6434c3736c1be",
            "id":"b642e6dd17d448",
            "type":"ins",
            "priority":1
        }
    ],
    "nav_create":[
        {
            "place":"f645495adceda0",
            "id":"b642e69a09e4ed",
            "type":"nav",
            "priority":1
        }
    ],
    "nav_result":[
        {
            "place":"f6434c04fd58db",
            "id":"b642e69a09e4ed",
            "type":"nav",
            "priority":4
        },
        {
            "place":"f6434bfe860624",
            "id":"b642e69a09e4ed",
            "type":"nav",
            "priority":3
        },
        {
            "place":"f6434c0c2c534e",
            "id":"b642e69a09e4ed",
            "type":"nav",
            "priority":2
        },
        {
            "place":"f6434c0abe7810",
            "id":"b642e69a09e4ed",
            "type":"nav",
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
    ],
    "banner_main":[
        {
            "place":"f642e6c2345bdb",
            "id":"b642e69a03682e",
            "type":"ban",
            "priority":1
        }
    ],
    "banner_other":[
        {
            "place":"f6434c13eb7298",
            "id":"b642e69a03682e",
            "type":"ban",
            "priority":2
        },
        {
            "place":"f6434c17ac5019",
            "id":"b642e69a03682e",
            "type":"ban",
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