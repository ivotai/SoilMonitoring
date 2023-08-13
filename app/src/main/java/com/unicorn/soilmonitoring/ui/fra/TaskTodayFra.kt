package com.unicorn.soilmonitoring.ui.fra

import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraTaskTodayBinding
import com.unicorn.soilmonitoring.databinding.ItemFakePointBinding
import com.unicorn.soilmonitoring.model.FakePoint
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.resources.color

class TaskTodayFra : BaseFra<FraTaskTodayBinding>() {

    override fun initViews() {

        binding.run {


            rv.linear()

                // 原来的间隔方案
//                .divider { // 水平间距
//                orientation = DividerOrientation.GRID
//                setDivider(16, true)
//                startVisible = true
//                endVisible= true
//            }

                .setup {

                    addType<FakePoint>(R.layout.item_fake_point)

                    onBind {
                        val model = getModel<FakePoint>()
                        getBinding<ItemFakePointBinding>().run {
                            tvDescription.text = "${model.park.description} - ${model.description}"

                            val backgroundColorNormalRes = splitties.material.colors.R.color.grey_50
                            val borderColorNormalRes = splitties.material.colors.R.color.blue_400
                            root.helper.run {
                                backgroundColorNormal = context.color(backgroundColorNormalRes)
                                borderColorNormal = context.color(borderColorNormalRes)
                            }
                        }
                    }

                }
        }

    }

    override fun initEvents() {
        receiveEvent<List<FakePoint>> {
            binding.rv.bindingAdapter.models = it
        }
    }

}