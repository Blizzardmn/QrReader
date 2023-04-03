package com.qr.myqr.history

import android.app.AlertDialog
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.qr.myqr.basic.BaseFragment
import com.qr.myqr.databinding.FragmentScanHistoryBinding
import com.qr.myqr.db.RoomImpl
import com.qr.myqr.main.getUiBeanByTypeIndex
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
                    toResultPage(it.content);
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