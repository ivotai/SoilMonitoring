package com.unicorn.soilmonitoring.ui

import com.google.android.material.tabs.TabLayoutMediator
import com.unicorn.soilmonitoring.databinding.ActTaskBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct

class TaskAct : BaseAct<ActTaskBinding>() {

    override fun initViews() {
        super.initViews()

        binding.run {
            TaskFragmentStateAdapter(this@TaskAct).run {
                viewPager2.offscreenPageLimit = itemCount - 1
                viewPager2.adapter = this
                TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                    tab.text = titles[position]
                }.attach()
            }
        }
    }

}