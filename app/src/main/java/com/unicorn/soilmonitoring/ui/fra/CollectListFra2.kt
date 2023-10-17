package com.unicorn.soilmonitoring.ui.fra

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraCollectList2Binding
import com.unicorn.soilmonitoring.model.Collect
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.resources.color

class CollectListFra2 : BaseFra<FraCollectList2Binding>() {

    override fun initViews() {
        binding.run {

            // 设置标题栏
            titleBar.run {
                titleBar.statusPadding()
                setTitle("采样记录")
                setTitleColor(color(R.color.white))
            }

            rv.grid(1).divider {
                orientation = DividerOrientation.GRID
                setDivider(16, true)
                includeVisible = true
            }.setup {
                addType<Collect>(R.layout.item_collect2)

                onBind {

                }

                onClick(R.id.itvOperation){
                    val items = listOf("已送样","完成检测","已出报告","退回重采")
                    MaterialDialog(requireContext()).show {
                        listItems(items = items)
                    }
                }
            }.models = (1..20).toList().map { Collect() }
        }
    }

}