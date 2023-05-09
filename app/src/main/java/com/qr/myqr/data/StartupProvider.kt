package com.qr.myqr.data

import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.qr.myqr.appIns
import com.qr.myqr.data.http.HttpClient
import com.qr.myqr.data.http.IHttpCallback
import com.qr.myqr.isReleaseMode
import com.qr.myqr.pop.PopManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Headers

object StartupProvider {

    fun onStart() {
        OneDayCache.ins.checkIfAnotherDay()
        AppCache.ins.firstInAppTms
        StartupReceiver.doRegister(appIns)
        GlobalScope.launch {
            queryRefer()
            queryCloak()

            while (true) {
                //定时弹窗
                if (!appIns.isAppForeground()) {
                    PopManager.pop()
                }
                delay(60_000L)
            }
        }
    }

    fun isOneShotEnable(): Boolean {
        return when (RemoteConfig.ins.getSafeMode()) {
            0L -> true
            1L -> isUserBuyer
            2L -> isUserBuyer && !isUserBlock
            else -> false
        }
    }

    fun isInsRefEnable(): Boolean {
        return isUserBuyer
    }

    private var isUserBlock = false
    private fun queryCloak() {
        val httpUrl = if (isReleaseMode) "https://api.suireader.live/ca/cc/" else "https://test.suireader.live/ca/cc/"
        val headerMap = HashMap<String, String>()
        headerMap["ISSU"] = appIns.packageName
        HttpClient.ins.getSync(httpUrl, headerMap, object :IHttpCallback {
            override fun onSuccess(headers: Headers, body: String?) {
                //1： 包名不匹配 2：命中IP 3：未命中
                isUserBlock = headers["C1"] == "2"
                var subHost = headers["C2"]
                if (subHost.isNullOrEmpty() || subHost.length < 2) return
                subHost = subHost.substring(1, subHost.length - 1)
                subHost = subHost.reversed()
            }

            override fun onError(code: Int, error: String?) {

            }
        })
    }

    var isUserBuyer = true
    var isUserBuyerFb = false
    var isUserBuyerGg = false
    var isUserCommon = false
    private fun recognizeUser(installRefer: String) {
        isUserBuyerFb = installRefer.contains("fb4a", false)
        isUserBuyerGg = installRefer.contains("gclid", false)
                || installRefer.contains("not%20set", false)
                || installRefer.contains("youtubeads", false)
                || installRefer.contains("%7B%22", false)
        isUserBuyer = isUserBuyerFb || isUserBuyerGg
        isUserCommon = !isUserBuyer
    }

    private fun queryRefer() {
        val refer = AppCache.ins.referValue
        if (refer.isNotEmpty()) {
            recognizeUser(refer)
            return
        }

        try {
            val referrerClient = InstallReferrerClient.newBuilder(appIns).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            // Connection established.
                            val response: ReferrerDetails = referrerClient.installReferrer ?: return
                            val version = response.installVersion ?: ""
                            val clickTime: Long = response.referrerClickTimestampSeconds
                            val beginTime: Long = response.installBeginTimestampSeconds
                            val serverClickTime = response.referrerClickTimestampServerSeconds
                            val serverBeginTime = response.installBeginTimestampServerSeconds
                            val referrerUrl: String = response.installReferrer ?: ""
                            val gpParam = response.googlePlayInstantParam

                            AppCache.ins.referValue = referrerUrl
                            recognizeUser(referrerUrl)
                            referrerClient.endConnection()
                            //NewUploaderImpl.doReferrer(referrerUrl, version, beginTime, serverClickTime)
                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        } catch (e: Exception) {
        }
    }
}