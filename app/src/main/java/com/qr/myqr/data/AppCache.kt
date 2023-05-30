package com.qr.myqr.data

import com.tencent.mmkv.MMKV

class AppCache {

    companion object {
        val ins: AppCache by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AppCache()
        }
    }

    private val mmkv = MMKV.mmkvWithID("qr_reader_conf_app", MMKV.MULTI_PROCESS_MODE)

    var referValue: String
        set(value) {
            mmkv.encode("installed_referrer", value)
        }
        get() {
            return mmkv.decodeString("installed_referrer", "") ?: ""
        }

    var cloakValue: String
        set(value) {
            mmkv.encode("cloak_user", value)
        }
        get() {
            return mmkv.decodeString("cloak_user", "") ?: ""
        }

    val firstInAppTms: Long
        get() {
            var l = mmkv.decodeLong("first_open_app", 0L)
            if (l <= 0L) {
                l = System.currentTimeMillis()
                mmkv.encode("first_open_app", l)
            }
            return l
        }

    var lastAdInsStyle: Boolean
        set(value) {
            mmkv.encode("last_style_ins", value)
        }
        get() {
            return mmkv.decodeBool("last_style_ins", false)
        }

    var appPropertyChanged: Boolean
        set(value) {
            mmkv.encode("app_property_changed", value)
        }
        get() {
            return mmkv.decodeBool("app_property_changed", false)
        }
}