package com.unicorn.soilmonitoring.ui.fra

import android.graphics.Color
import android.graphics.Typeface
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.GridLayoutManager
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraFakePointAllBinding
import com.unicorn.soilmonitoring.databinding.ItemFakePointBinding
import com.unicorn.soilmonitoring.databinding.ItemParkBinding
import com.unicorn.soilmonitoring.model.FakePoint
import com.unicorn.soilmonitoring.model.Park
import com.unicorn.soilmonitoring.ui.Fal
import com.unicorn.soilmonitoring.ui.Far
import com.unicorn.soilmonitoring.ui.Fas
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.resources.color


class FakePointAllFra(val title: String) : BaseFra<FraFakePointAllBinding>() {

    override fun initViews() {

        binding.run {
            titleBar.setTitle(title)

            val scanCount = 3
            val layoutManager = GridLayoutManager(requireContext(), scanCount) // 则代表列表一行铺满要求跨度为3
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < 0) return 1 // 如果添加分割线可能导致position为负数
                    // 根据类型设置列表item跨度
                    return when (rv.bindingAdapter.getItemViewType(position)) {
                        R.layout.item_fake_point -> 1 // 设置指定类型的跨度为1, 假设spanCount为3则代表此类型占据宽度为二分之一
                        else -> scanCount
                    }
                }
            }

            rv.layoutManager = layoutManager
            rv
//                 原来的间隔方案
                .divider { // 水平间距
                    orientation = DividerOrientation.GRID
                    setDivider(12, true)
                    startVisible = true
                    endVisible = true
                }

                .setup {

                    addType<FakePoint>(R.layout.item_fake_point)
                    addType<Park>(R.layout.item_park)

                    onCreate {
                        // onCreateViewHolder只会调用一次，所以在这里设置字体
                        // 这时还没有 model 所以只能根据 viewType 来设置
                        if (itemViewType == R.layout.item_park) {
                            getBinding<ItemParkBinding>().run {
                                tvDescription.typeface = Typeface.createFromAsset(requireActivity().assets, "SanJiNengLiangHeiJianTi-2.ttf")
                            }
                        }
                    }

                    onBind {
                        when (val model = getModel<Any>()) {
                            is FakePoint -> {
                                getBinding<ItemFakePointBinding>().run {
//                                    tvDescription.text = model.description

                                    val isChecked = model in getCheckedModels<FakePoint>()
                                    val backgroundColorNormalInt =
                                        if (isChecked) context.color(splitties.material.colors.R.color.blue_50) else context.color(splitties.material.colors.R.color.grey_100)
                                    val borderColorNormalInt =
                                        if (isChecked) context.color(splitties.material.colors.R.color.blue_400) else Color.parseColor(
                                            "#F6F6F6"
                                        )
                                    root.helper.run {
                                        backgroundColorNormal = backgroundColorNormalInt
                                        borderColorNormal = borderColorNormalInt
                                    }
                                }
                            }

                            is Park -> {
                                getBinding<ItemParkBinding>().run {
                                    tvDescription.text = model.description
                                }
                            }
                        }
                    }

                    // 长按列表进入编辑模式
                    onLongClick(R.id.root) {
                        if (getModel<Any>() is FakePoint) {
                            if (!toggleMode) {
                                toggle()
                                setChecked(layoutPosition, true)
                            }
                        }
                    }

                    // 点击列表触发选中
                    onClick(R.id.root) {
                        if (getModel<Any>() is FakePoint) {
                            // 如果当前未处于选择模式下 点击无效
                            if (toggleMode) checkedSwitch(layoutPosition)
                        }
                    }

                    // 监听列表选中
                    onChecked { position, isChecked, _ ->
                        notifyItemChanged(position)

                        // 显示打钩
                        titleBar.getCiv2().visibility =
                            if (toggleMode && checkedCount > 0) VISIBLE else INVISIBLE
                    }

                    onToggle { position, toggleMode, _ ->
                        // 显示打叉
                        titleBar.getCiv1().visibility = if (toggleMode) VISIBLE else INVISIBLE
                        // 如果取消管理模式则取消全部已选择
                        if (!toggleMode) checkedAll(false)
                    }

                }.models = Park.all()
        }

    }

    override fun initIntents() {
        binding.run {
            titleBar.getCiv1().run {
                icon?.icon = Fal.Icon.fal_times
                setOnClickListener { rv.bindingAdapter.toggle() }
            }
            titleBar.getCiv2().run {

                icon?.icon = Fas.Icon.fas_check

                setOnClickListener {
//                    sendEvent(rv.bindingAdapter.getCheckedModels<FakePoint>())
                    rv.bindingAdapter.toggle()
                }
            }
        }
    }




}