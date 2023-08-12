package com.unicorn.soilmonitoring.ui.fra

import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraTaskAllBinding
import com.unicorn.soilmonitoring.databinding.ItemTaskAllBinding
import com.unicorn.soilmonitoring.model.FakePoint
import com.unicorn.soilmonitoring.ui.base.BaseFra

class TaskAllFra : BaseFra<FraTaskAllBinding>() {

    override fun initViews() {

        binding.run {
            rvTaskAll.linear().setup {
                addType<FakePoint>(R.layout.item_task_all)
                onBind {
                    getBinding<ItemTaskAllBinding>().tvText.text = getModel<FakePoint>().description
                }
            }.models = FakePoint.all
        }

    }

    override fun initIntents() {
        super.initIntents()
    }
}