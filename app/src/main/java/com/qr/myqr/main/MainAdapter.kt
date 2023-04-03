package com.qr.myqr.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qr.myqr.R
import com.qr.myqr.databinding.ItemHomeBinding
import java.io.Serializable

private val data = arrayListOf<UiBean>().apply {
    add(UiBean(R.drawable.ic_text, "Text", R.drawable.ic_text_bg, Type.Text))
    add(UiBean(R.drawable.ic_website, "Website", R.drawable.ic_website_bg, Type.Website))
    add(UiBean(R.drawable.ic_tel, "Tel", R.drawable.ic_tel_bg, Type.Tel))
    add(UiBean(R.drawable.ic_sms, "SMS", R.drawable.ic_sms_bg, Type.SMS))
    add(UiBean(R.drawable.ic_wifi, "Wi-Fi", R.drawable.ic_wifi_bg, Type.WiFi))
    add(UiBean(R.drawable.ic_email, "E-mail", R.drawable.ic_email_bg, Type.Email))
    add(UiBean(R.drawable.ic_wathsapp, "Whatsapp", R.drawable.ic_wathsapp_bg, Type.Whatsapp))
    add(UiBean(R.drawable.ic_twitter, "Twitter", R.drawable.ic_twitter_bg2, Type.Twitter))
    add(UiBean(R.drawable.ic_pay, "Paypal", R.drawable.ic_pay_bg, Type.Paypal))
    add(UiBean(R.drawable.ic_ins, "Instagram", R.drawable.ic_ins_bg, Type.Instagram))
    add(UiBean(R.drawable.ic_facebook, "Facebook", R.drawable.ic_facb_bg, Type.Facebook))
    add(UiBean(R.drawable.ic_tiktok, "TikTOK", R.drawable.ic_tiktok_bg, Type.TikTOK))
}

class MainAdapter(val itemClick: (bean: UiBean) -> Unit) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    inner class ViewHolder(val item: ItemHomeBinding) : RecyclerView.ViewHolder(item.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean = data[position]
        holder.item.run {
            icon.setImageResource(bean.icon)
            text.text = bean.name
            ivBg.setImageResource(bean.bg)
            ivBg.setOnClickListener {
                itemClick.invoke(bean)
            }
        }
    }

    override fun getItemCount() = data.size


}

data class UiBean(val icon: Int, val name: String, val bg: Int, val type: Type) : Serializable {
    fun isFacebook(): Boolean {
        return icon == R.drawable.ic_facebook
    }
}

enum class Type {
    Text, Website, Tel, SMS, WiFi, Email, Whatsapp, Twitter, Paypal, Instagram, Facebook, TikTOK
}

fun getUiBeanByTypeIndex(index: Int): UiBean? {
    if (index >= Type.values().size) {
        return null
    }
    return getUiBeanByType(Type.values()[index])
}

fun getUiBeanByType(type: Type): UiBean {
    return data.find { it.type == type } ?: UiBean(
        R.drawable.ic_text, "Text", R.drawable.ic_text_bg, Type.Text
    )
}