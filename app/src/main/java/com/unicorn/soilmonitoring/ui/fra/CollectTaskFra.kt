package com.unicorn.soilmonitoring.ui.fra

import android.graphics.Point
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraCollectTaskBinding
import com.unicorn.soilmonitoring.databinding.ItemRealPointBinding
import com.unicorn.soilmonitoring.event.MapEvent
import com.unicorn.soilmonitoring.event.NavigationEvent
import com.unicorn.soilmonitoring.ui.act.SampleCollectAct
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.fragments.start
import splitties.resources.color

class CollectTaskFra : BaseFra<FraCollectTaskBinding>() {

    override fun initViews() {
        binding.run {

            // 设置标题栏
            titleBar.run {
                titleBar.statusPadding()
                setTitle("采样任务")
                setTitleColor(color(R.color.white))
            }

            rv.grid(1).divider { // 水平间距
                orientation = DividerOrientation.GRID
                setDivider(16, true)
                includeVisible = true
            }.setup {

                addType<com.unicorn.soilmonitoring.app.Point>(R.layout.item_real_point)

                onBind {
                    val model = getModel<com.unicorn.soilmonitoring.app.Point>()
                    getBinding<ItemRealPointBinding>().run {
                        tvDescription.text = model.description
                    }
                }

                onClick(R.id.tv_map_event) {
                    sendEvent(MapEvent(getModel()))
                }
                onClick(R.id.tv_navigation) {
                    sendEvent(NavigationEvent(getModel()))
                }
                onClick(R.id.tv_gather) {
                    start<SampleCollectAct> { }
                }

            }
        }

    }

    override fun initEvents() {
        receiveEvent<List<Point>> {
            binding.rv.bindingAdapter.models = it
        }
    }

}