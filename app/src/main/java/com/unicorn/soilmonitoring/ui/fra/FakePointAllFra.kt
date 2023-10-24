package com.unicorn.soilmonitoring.ui.fra

import android.graphics.Color
import android.graphics.Typeface
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraFakePointAllBinding
import com.unicorn.soilmonitoring.databinding.ItemFakePointBinding
import com.unicorn.soilmonitoring.databinding.ItemParkBinding
import com.unicorn.soilmonitoring.event.DrawCloseEvent
import com.unicorn.soilmonitoring.model.FakePoint
import com.unicorn.soilmonitoring.model.Park
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.resources.color
import splitties.views.InputType.Companion.text


class FakePointAllFra() : BaseFra<FraFakePointAllBinding>() {

    override fun initViews() {

        binding.run {
            root.statusPadding()
            // 设置标题栏
//            titleBar.run {
//                titleBar.statusPadding()
//                setTitle("采样总览")
//                setTitleColor(color(R.color.white))
//                listOf(getCiv1(), getCiv2()).forEach {
//                    it.textSize = 16f
//                    it.setTextColor(color(R.color.white))
//                }
//            }

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
                    setDivider(16, true)
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
                                tvDescription.typeface = Typeface.createFromAsset(
                                    requireActivity().assets, "SanJiNengLiangHeiJianTi-2.ttf"
                                )
                            }

//                            layoutPosition
                        }
                    }

                    onBind {
                        when (val model = getModel<Any>()) {
                            is FakePoint -> {
                                getBinding<ItemFakePointBinding>().run {
                                    tvNo.text = model.no
//                                    tvIsGather.text = if (model.isGather) "已采样" else "待采样"
//
//                                    tvIsGather.setTextColor(
//                                        if (model.isGather) context.color(splitties.material.colors.R.color.green_400) else Color.parseColor(
//                                            "#5E656F"
//                                        )
//                                    )



                                    val isChecked = model in getCheckedModels<FakePoint>()
                                    tvNo.setTextColor(
                                        if (isChecked) context.color(R.color.white) else context.color(
                                            splitties.material.colors.R.color.grey_700
                                        )
                                    )
                                    val backgroundColorNormalInt =
                                        if (isChecked) context.color(R.color.primary) else context.color(
                                            splitties.material.colors.R.color.grey_50
                                        )

                                    tvNo.helper.run {
                                        backgroundColorNormal = backgroundColorNormalInt
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



                    // 点击列表触发选中
                    onClick(R.id.tv_no) {
                        checkedSwitch(modelPosition)
//                        notifyItemChanged(modelPosition)
                    }

                    // 监听列表选中
                    onChecked { position, isChecked, _ ->
                        notifyItemChanged(position)

                        // 显示打钩
//                        titleBar.getCiv2().visibility =
//                            if (toggleMode && checkedCount > 0) VISIBLE else INVISIBLE
                    }

                    onToggle { position, toggleMode, _ ->
                        // 显示打叉
//                        titleBar.getCiv1().visibility = if (toggleMode) VISIBLE else INVISIBLE
                        // 如果取消管理模式则取消全部已选择
                        if (!toggleMode) checkedAll(false)
                    }

                }.models = Park.all()
        }

    }

    override fun initIntents() {
        binding.run {
            tvFinish.setOnClickListener {
                sendEvent(DrawCloseEvent())
            }
//            titleBar.getCiv1().run {
//                text = "取消"
//                setOnClickListener { rv.bindingAdapter.toggle() }
//            }
//            titleBar.getCiv2().run {
//                text = "确认"
//                setOnClickListener {
//                    rv.bindingAdapter.getCheckedModels<FakePoint>().joinToString { it.no }
//                        .let { ToastUtils.showLong("设定当前采样为 $it") }
//                    rv.bindingAdapter.toggle()
//                }
//            }
        }
    }


}