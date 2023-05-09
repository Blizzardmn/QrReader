package com.qr.myqr.data

import com.qr.myqr.calculateDays
import com.tencent.mmkv.MMKV

class OneDayCache {

    companion object {
        val ins: OneDayCache by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            OneDayCache()
        }
    }

    private val mmkv = MMKV.mmkvWithID("cache_qr_one_day")

    fun checkIfAnotherDay(): Boolean {
        val lastTms = mmkv.decodeLong("today_startup_timestamp", 0L)
        val current = System.currentTimeMillis()
        if (lastTms == 0L) {
            mmkv.encode("today_startup_timestamp", current)
        } else {
            val days = calculateDays(lastTms, current)
            if (days != 0) {
                mmkv.clearAll()
                mmkv.encode("today_startup_timestamp", current)
                return true
            }
        }
        return false
    }

    fun setLastAlertTimestamp() {
        val key = "last_alert_timestamp"
        mmkv.encode(key, System.currentTimeMillis())
    }
    fun getLastAlertTimestamp(): Long {
        val key = "last_alert_timestamp"
        return mmkv.decodeLong(key, 0L)
    }

    fun addAlertCounters() {
        val key = "last_alert_counters"
        val times = mmkv.decodeInt(key, 0) + 1
        mmkv.encode(key, times)
    }
    fun getAlertCounters(): Int {
        val key = "last_alert_counters"
        return mmkv.decodeInt(key, 0)
    }
}