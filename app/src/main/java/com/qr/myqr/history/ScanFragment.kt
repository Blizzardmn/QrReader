package com.qr.myqr.history

import android.app.AlertDialog
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.qr.myqr.appIns
import com.qr.myqr.basic.BaseFragment
import com.qr.myqr.data.StartupProvider
import com.qr.myqr.databinding.FragmentScanHistoryBinding
import com.qr.myqr.db.RoomImpl
import com.qr.myqr.main.getUiBeanByTypeIndex
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopInterstitial
import com.qr.myqr.scan.ScannerResultActivity
import com.qr.myqr.toActivity
import kotlinx.coroutines.launch

class ScanFragment : BaseFragment() {
    private lateinit var mAdapter: HistoryAdapter
    private val mScanDao by lazy { RoomImpl.scanDao }
    override val viewBinding by lazy { FragmentScanHistoryBinding.inflate(layoutInflater) }


    override fun init() {
        viewBinding.run {
            mAdapter = HistoryAdapter(getUiData())
            rv.adapter = mAdapter.apply {
                itemClick = {
                    showIns {
                        toResultPage(it.content)
                    }
                }
                onLongClickListener = {
                    AlertDialog.Builder(context).setMessage("Are your sure delete this record?")
                        .setPositiveButton("Delete") { dialog, _ ->
                            dialog.dismiss()
                            lifecycleScope.launch {
                                mScanDao.delById(it.entityId)
                                mAdapter.removeItem(it)
                            }
                        }.setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }.create().show()
                }
            }
            rv.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showIns(nextDo: () -> Unit) {
        val activityCtx = activity
        if (activityCtx == null || !StartupProvider.isOneShotEnable()) {
            nextDo.invoke()
            return
        }
        AdsLoader.loadAd(activityCtx, AdPos.insClick, object :AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (ad !is TopInterstitial) return
                if (!ad.show(activityCtx)) {
                    nextDo.invoke()
                }
            }

            override fun onLoadErr(msg: String) {
                nextDo.invoke()
            }

            override fun onDismiss() {
                nextDo.invoke()
            }
        }, onlyCache = true)
    }

    private fun getUiData(): ArrayList<HistoryUiBean> {
        val list = arrayListOf<HistoryUiBean>()
        for (entity in mScanDao.getAll()) {
            val uiBean = getUiBeanByTypeIndex(entity.type)
            uiBean?.let {
                list.add(HistoryUiBean(it, entity.content, entity.id))
            }
        }
        return list
    }

    private fun toResultPage(string: String) {
        activity?.let {
            it.toActivity<ScannerResultActivity>(bundle = Bundle().apply {
                putString("scanResult", string)
            })
        }
    }
}