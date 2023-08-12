package com.unicorn.soilmonitoring.ui

import com.google.android.material.tabs.TabLayoutMediator
import com.unicorn.soilmonitoring.databinding.ActTaskBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct

class TaskAct : BaseAct<ActTaskBinding>() {

    override fun initViews() {
        super.initViews()

        binding.run {
            val taskFragmentStateAdapter = TaskFragmentStateAdapter(this@TaskAct)
            viewPager2.offscreenPageLimit = taskFragmentStateAdapter.itemCount - 1
            viewPager2.adapter = taskFragmentStateAdapter

            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = taskFragmentStateAdapter.titles[position]
            }.attach()
        }
    }

}