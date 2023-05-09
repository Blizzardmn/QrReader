package com.qr.myqr.history

import android.app.AlertDialog
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.qr.myqr.basic.BaseFragment
import com.qr.myqr.create.CreateResultActivity
import com.qr.myqr.data.StartupProvider
import com.qr.myqr.databinding.FragmentCreateHistoryBinding
import com.qr.myqr.db.RoomImpl
import com.qr.myqr.main.getUiBeanByTypeIndex
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopInterstitial
import com.qr.myqr.toActivity
import kotlinx.coroutines.launch

class CreateFragment : BaseFragment() {
    private lateinit var mAdapter: HistoryAdapter
    private val mDaoCreate by lazy { RoomImpl.createDao }
    override val viewBinding by lazy { FragmentCreateHistoryBinding.inflate(layoutInflater) }

    override fun init() {
        viewBinding.run {
            mAdapter = HistoryAdapter(getUiData())
            rv.adapter = mAdapter.apply {
                itemClick = {
                    showIns {
                        toCreateResult(it.content)
                    }
                }
                onLongClickListener = {
                    AlertDialog.Builder(context).setMessage("Are your sure delete this record?")
                        .setPositiveButton("Delete") { dialog, _ ->
                            dialog.dismiss()
                            lifecycleScope.launch {
                                mDaoCreate.delById(it.entityId)
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
        AdsLoader.loadAd(activityCtx, AdPos.insClick, object : AdsListener() {
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
        for (entity in mDaoCreate.getAll()) {
            val uiBean = getUiBeanByTypeIndex(entity.type)
            uiBean?.let {
                list.add(HistoryUiBean(it, entity.content, entityId = entity.id))
            }
        }
        return list
    }


    private fun toCreateResult(string: String) {
        activity?.let {
            it.toActivity<CreateResultActivity>(bundle = Bundle().apply {
                putString("content", string)
            })
        }
    }
}