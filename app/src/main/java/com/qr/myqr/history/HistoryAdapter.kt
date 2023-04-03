package com.qr.myqr.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qr.myqr.databinding.ItemHistoryBinding

/**
 * Date：2023/4/3
 * Describe:
 */
class HistoryAdapter(var data: ArrayList<HistoryUiBean>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var itemClick: (bean: HistoryUiBean) -> Unit = {}
    var onLongClickListener: (bean: HistoryUiBean) -> Unit = {}

    inner class ViewHolder(val item: ItemHistoryBinding) : RecyclerView.ViewHolder(item.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun removeItem(bean: HistoryUiBean){
        data.remove(bean)
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean = data[position]
        holder.item.run {
            icon.setImageResource(bean.uiBean.icon)
            tvType.text = bean.uiBean.name
            tvContent.text = bean.content
            root.setOnClickListener {
                itemClick.invoke(bean)
            }
            root.setOnLongClickListener {
                onLongClickListener.invoke(bean)
                false
            }
        }
    }
}