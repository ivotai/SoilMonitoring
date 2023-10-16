package com.unicorn.soilmonitoring.ui.fra

import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraCollectListBinding
import com.unicorn.soilmonitoring.model.Collect
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.resources.color

class CollectListFra : BaseFra<FraCollectListBinding>() {

        override fun initViews() {
            binding.run {

                // 设置标题栏
                titleBar.run {
                    titleBar.statusPadding()
                    setTitle("采样记录")
                    setTitleColor(color(R.color.white))
                }

                rv.linear().divider {
                    setDivider(16, true)
                }.setup {
                    addType<Collect>(R.layout.item_collect)

                    onBind {

                    }
                }.models = (1..20).toList().map { Collect() }
            }
        }

}