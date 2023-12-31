package com.unicorn.soilmonitoring.ui.fra

import android.view.Gravity
import com.baidu.ar.it
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.drake.statusbar.statusPadding
import com.google.android.material.datepicker.MaterialDatePicker
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.app.Config
import com.unicorn.soilmonitoring.app.Point
import com.unicorn.soilmonitoring.app.PointStatus
import com.unicorn.soilmonitoring.databinding.FraCollectTaskBinding
import com.unicorn.soilmonitoring.databinding.ItemCollectProgressBinding
import com.unicorn.soilmonitoring.databinding.ItemRealPointBinding
import com.unicorn.soilmonitoring.event.DrawCloseEvent
import com.unicorn.soilmonitoring.event.MapEvent
import com.unicorn.soilmonitoring.event.NavigationEvent
import com.unicorn.soilmonitoring.model.Progress
import com.unicorn.soilmonitoring.ui.act.AddCollectAct
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.fragments.start
import splitties.resources.color


class CollectTaskFra : BaseFra<FraCollectTaskBinding>() {

    override fun initViews() {
        binding.run {
            constraintLayout.statusPadding()

            rv.grid(1).divider { // 水平间距
                orientation = DividerOrientation.GRID
                setDivider(16, true)
                includeVisible = true
            }.setup {

//                addType<Progress>(R.layout.item_collect_progress)
                addType<Point>(R.layout.item_real_point)

                onBind {
                    when (val model = getModel<Any>()) {
//                        is Progress -> {
//                            val total = Config.points.size
//                            val taken = Config.points.count { it.pointStatus == PointStatus.TAKEN }
//                            val progress = 100 * taken / total
//                            getBinding<ItemCollectProgressBinding>().run {
//                                tvProgress.text = "$taken/$total"
//                                linearProgressIndicator.setProgress(progress, true)
//                            }
//                        }

                        is Point -> {
                            getBinding<ItemRealPointBinding>().run {
                                tvDescription.text =
                                    "${model.key} (距离${model.distance.toInt()}米)"
                                tvStatus.text = model.pointStatus.text
                                tvStatus.setTextColor(
                                    color(
                                        if (model.pointStatus == PointStatus.TAKEN) R.color.primary else splitties.material.colors.R.color.grey_400
                                    )
                                )

                            }
                        }
                    }
                }

                onClick(R.id.tv_map_event) {
                    sendEvent(MapEvent(getModel()))
                }
                onClick(R.id.tv_navigation) {
                    sendEvent(NavigationEvent(getModel()))
                }
                onClick(R.id.tv_gather) {
                    start<AddCollectAct> { }
                }

            }
        }

    }

    override fun initIntents() {
        binding.run {
            tvFilterByDate.setOnClickListener {
                drawerLayout.openDrawer(Gravity.RIGHT)
            }

            //
            swipeRefreshLayout.setColorSchemeResources(R.color.primary)
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false

//                val models = binding.rv.bindingAdapter.models!!
//                val progress = models.filterIsInstance<Progress>()
//                if (progress.isEmpty()){
//                    binding.rv.bindingAdapter.models = listOf(Progress(Config.points)) + Config.points
                    // 不知道为什么错
//                    binding.rv.bindingAdapter.addModels(listOf(Progress(Config.points)),true,0)
//                }
            }
        }
    }

    override fun initEvents() {
        receiveEvent<List<Point>> {
            binding.rv.bindingAdapter.models =  Config.points
        }
        receiveEvent<DrawCloseEvent> {
            binding.drawerLayout.closeDrawer(Gravity.RIGHT)
        }
    }

}