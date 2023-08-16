package com.unicorn.soilmonitoring.ui.fra

import android.graphics.Point
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraTaskTodayBinding
import com.unicorn.soilmonitoring.databinding.ItemRealPointBinding
import com.unicorn.soilmonitoring.event.MapEvent
import com.unicorn.soilmonitoring.event.NavigationEvent
import com.unicorn.soilmonitoring.event.NavigationOutEvent
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

                addType<com.unicorn.soilmonitoring.app.Point>(R.layout.item_real_point)

                onBind {
                    val model = getModel<com.unicorn.soilmonitoring.app.Point>()
                    getBinding<ItemRealPointBinding>().run {
                        tvDescription.text = "${model.description}"
                    }
                }

                onClick(R.id.tv_route) {
                    sendEvent(NavigationOutEvent(getModel()))
                }
                onClick(R.id.tv_navigation) {
                    sendEvent(NavigationEvent(getModel()))
                }
                onClick(R.id.tv_gather) {
                    ToastUtils.showShort("跳转到采样界面")
                }
                onClick(R.id.root){
                    sendEvent(MapEvent(getModel()))
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