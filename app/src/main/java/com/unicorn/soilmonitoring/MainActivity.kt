package com.unicorn.soilmonitoring

import com.unicorn.soilmonitoring.databinding.ActivityMainBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct


class MainActivity : BaseAct<ActivityMainBinding>() {

    override fun initViews() {
    }

    override fun onPause() {
        super.onPause()
        binding.bmapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.bmapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.bmapView.onDestroy()
    }

}