package com.unicorn.soilmonitoring.ui.fra

import com.baidu.mapapi.search.sug.SuggestionResult
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraTaskTodayBinding
import com.unicorn.soilmonitoring.databinding.ItemFakePointBinding
import com.unicorn.soilmonitoring.databinding.ItemRealPointBinding
import com.unicorn.soilmonitoring.ui.base.BaseFra

class TaskTodayFra(val title: String) : BaseFra<FraTaskTodayBinding>() {

    override fun initViews() {

        binding.run {
            titleBar.setTitle(title)

            rv.grid(1).divider { // 水平间距
                orientation = DividerOrientation.GRID
                setDivider(16, true)
                includeVisible = true
            }.setup {

                addType<SuggestionResult.SuggestionInfo>(R.layout.item_real_point)

                onBind {
                    val model = getModel<SuggestionResult.SuggestionInfo>()
                    getBinding<ItemRealPointBinding>().run {
                        tvDescription.text = "${model.key}"
                    }
                }

            }
        }

    }

    override fun initEvents() {
        receiveEvent<List<SuggestionResult.SuggestionInfo>> {
            binding.rv.bindingAdapter.models = it
        }
    }

}