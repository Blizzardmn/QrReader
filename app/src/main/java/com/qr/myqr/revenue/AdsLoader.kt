package com.qr.myqr.revenue

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.conf.AdConf
import com.qr.myqr.revenue.conf.DefaultConf
import com.qr.myqr.revenue.load.TopOnLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object AdsLoader: DefaultConf(), CoroutineScope by MainScope() {

    private const val adTag = "AdsLoader"

    private val syncRequesting = HashMap<String, String>()
    private val _innerAds = hashMapOf<String, ArrayList<BaseAd>>()
    private fun getCache(@AdPos pos: String): BaseAd? {
        val ads = _innerAds[pos] ?: return null
        if (ads.isNullOrEmpty()) return null
        var ad: BaseAd? = null
        for (i in ads.size - 1 downTo 0) {
            ad = ads.removeAt(i)
            if (!ad.isExpired()) break
        }
        //Log.e(adTag, "$pos getCache:$ad, container: $_innerAds")
        return ad
    }

    fun isCached(@AdPos pos: String): Boolean {
        val ads = _innerAds[pos] ?: return false
        if (ads.isNullOrEmpty()) return false
        var validCache = false
        for (i in ads.size - 1 downTo 0) {
            validCache = !ads[i].isExpired()
            if (validCache) break
        }
        return validCache
    }

    @Synchronized
    fun add2Cache(@AdPos pos: String, ad: BaseAd, forceAdd: Boolean = false) {
        var list = _innerAds[pos]
        if (list.isNullOrEmpty()) {
            list = arrayListOf()
            _innerAds[pos] = list
        }
        if (forceAdd) {
            list.add(0, ad)
            return
        }
        list.add(ad)
        Collections.sort(list, object : Comparator<BaseAd> {
            override fun compare(o1: BaseAd?, o2: BaseAd?): Int {
                return (o1?.adConf?.priority?.compareTo(o2?.adConf?.priority ?: 0) ?: 0) * -1
            }
        })
    }

    @SuppressLint("LogNotTimber")
    fun preloadAd(ctx: Context, @AdPos adPos: String, forcePreload: Boolean = false) {
        if (isCached(adPos)) {
            Log.i(adTag, "$adPos preloadAd but cached")
            return
        }
        Log.i(adTag, "$adPos preloadAd")

        loadAd(ctx, adPos, object : AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                add2Cache(adPos, ad, forcePreload)
            }
        }, newLoad = forcePreload)
    }

    /**
     * @param ctx 可能存在load 了之后又没有及时展示的广告,这里除激励视频的加载之外，统一使用applicationContext
     * @param onlyCache 仅使用缓存,如果没有缓存就报错
     * @param newLoad 不论当前是否正在请求广告,都一律重新请求
     * */
    @SuppressLint("LogNotTimber")
    fun loadAd(
        ctx: Context,
        @AdPos adPos: String,
        adsListener: AdsListener,
        onlyCache: Boolean = false,
        newLoad: Boolean = false) {
        Log.i(adTag, "$adPos loadAd")

        val cache = getCache(adPos)
        if (cache != null) {
            cache.redefineListener(adsListener)
            adsListener.onLoadedAd(cache)
            return
        }
        if (onlyCache) {
            adsListener.onLoadErr("No Cache")
            return
        }
        if (!newLoad) {
            synchronized(syncRequesting) {
                if (syncRequesting[adPos] != null) return
            }
        }
        val configs = configByPos(adPos)
        if (configs.lists.isNullOrEmpty()) {
            adsListener.onLoadErr()
            return
        }
        val idL = arrayListOf<AdConf>()
        idL.addAll(configs.lists)
        launch {
            innerLoad(ctx.applicationContext, adPos, idL, adsListener)
        }
    }

    private var topOnLoader: TopOnLoader? = null
    private fun innerLoad(ctx: Context, @AdPos adPos: String, idList: ArrayList<AdConf>, adsListener: AdsListener) {
        if (idList.isNullOrEmpty()) {
            adsListener.onLoadErr()
            return
        }

        val config = idList.removeAt(0)
        val (_, type, _) = config
        if (topOnLoader == null) topOnLoader = TopOnLoader()
        val loader = topOnLoader

        fun putImpressJson(ad: BaseAd?) {}

        fun checkResult(ad: BaseAd? = null) {
            synchronized(syncRequesting) {
                syncRequesting.remove(adPos)
            }
            putImpressJson(ad)
            Log.e(adTag, "$adPos requested ad: $ad")
            if (ad == null) {
                val cache = getCache(adPos)
                if (cache != null) {
                    adsListener.onLoadedAd(cache)
                    return
                }
                innerLoad(ctx, adPos, idList, adsListener)
                return
            }
            adsListener.onLoadedAd(ad)
        }

        when (type) {
            "open" -> {
                Log.i(adTag, "$adPos loadOpen: ${config.priority} ${config.id}")
                syncRequesting[adPos] = adPos
                loader?.loadOpen(ctx, adPos, config, adsListener) {
                    checkResult(it)
                }
            }

            "ins" -> {
                Log.i(adTag, "$adPos loadInterstitial: ${config.priority} ${config.id}")
                syncRequesting[adPos] = adPos
                loader?.loadInterstitial(ctx, adPos, config, adsListener) {
                    checkResult(it)
                }
            }

            "nav" -> {
                Log.i(adTag, "$adPos loadNative: ${config.priority} ${config.id}")
                syncRequesting[adPos] = adPos
                loader?.loadNative(ctx, adPos, config, adsListener) {
                    checkResult(it)
                }
            }

            "ban" -> {
                Log.i(adTag, "$adPos loadBanner: ${config.priority} ${config.id}")
                syncRequesting[adPos] = adPos
                loader?.loadBanner(ctx, adPos, config, adsListener) {
                    checkResult(it)
                }
            }
        }
    }
}