package com.unicorn.soilmonitoring.ui.act

import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.ActAddCollectBinding
import com.unicorn.soilmonitoring.model.Collect
import com.unicorn.soilmonitoring.model.CollectField
import com.unicorn.soilmonitoring.model.CollectGroup
import com.unicorn.soilmonitoring.model.SupportDivider
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.resources.color

class AddCollectAct : BaseAct<ActAddCollectBinding>() {

    override fun initViews() {
        immersive()

        binding.run {
            // 设置标题栏
            titleBar.run {
                titleBar.statusPadding()
                setTitle("采样")
                setTitleColor(color(R.color.white))
            }

            rv.linear().divider {
                setColorRes(splitties.material.colors.R.color.grey_200)
                setDivider(width = 1, dp = false)
            }.setup {
                addType<CollectField>(R.layout.item_collect_field)
                addType<SupportDivider>(R.layout.item_divider)
                addType<CollectGroup>(R.layout.group_collect_field)
            }
        }.models = collect.models
    }

    private val collect = Collect()

    private fun onFieldValueChange(
        item: CollectField, value: String
    ) {
        binding.rv.bindingAdapter.apply {
            // 刷新界面
            item.value = value
            notifyItemChanged(item.modelPosition)


        }
    }

}