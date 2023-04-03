package com.qr.myqr.history

import android.app.AlertDialog
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.qr.myqr.basic.BaseFragment
import com.qr.myqr.create.CreateResultActivity
import com.qr.myqr.databinding.FragmentCreateHistoryBinding
import com.qr.myqr.db.RoomImpl
import com.qr.myqr.main.getUiBeanByTypeIndex
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
                    toCreateResult(it.content)
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