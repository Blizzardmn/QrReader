package com.qr.myqr.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerViewAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    val fragmentList = arrayListOf<Fragment>().apply {
        add(ScanFragment())
        add(CreateFragment())
    }

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}