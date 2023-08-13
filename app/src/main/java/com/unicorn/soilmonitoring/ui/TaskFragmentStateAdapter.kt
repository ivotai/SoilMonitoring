package com.unicorn.soilmonitoring.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unicorn.soilmonitoring.ui.fra.AllFakePointFra
import com.unicorn.soilmonitoring.ui.fra.TaskTodayFra

class TaskFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    val titles = listOf("今日任务", "全部采样点")

    override fun getItemCount(): Int = titles.size

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) TaskTodayFra()
        else AllFakePointFra()
    }

}