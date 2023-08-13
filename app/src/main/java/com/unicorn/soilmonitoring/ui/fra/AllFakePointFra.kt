package com.unicorn.soilmonitoring.ui.fra

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.setup
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraFakePointAllBinding
import com.unicorn.soilmonitoring.databinding.ItemFakePointBinding
import com.unicorn.soilmonitoring.databinding.ItemParkBinding
import com.unicorn.soilmonitoring.model.FakePoint
import com.unicorn.soilmonitoring.model.Park
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.resources.color


class AllFakePointFra : BaseFra<FraFakePointAllBinding>() {

    override fun initViews() {

        binding.run {

            val scanCount = 3
            val layoutManager = GridLayoutManager(requireContext(), scanCount) // 则代表列表一行铺满要求跨度为3
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < 0) return 1 // 如果添加分割线可能导致position为负数
                    // 根据类型设置列表item跨度
                    return when (rvTaskAll.bindingAdapter.getItemViewType(position)) {
                        R.layout.item_fake_point -> 1 // 设置指定类型的跨度为1, 假设spanCount为3则代表此类型占据宽度为二分之一
                        else -> scanCount
                    }
                }
            }

            rvTaskAll.layoutManager = layoutManager
            rvTaskAll

                // 原来的间隔方案
//                .divider { // 水平间距
//                orientation = DividerOrientation.GRID
//                setDivider(16, true)
//                startVisible = true
//                endVisible= true
//            }

                .setup {

                    addType<FakePoint>(R.layout.item_fake_point)
                    addType<Park>(R.layout.item_park)

                    onBind {
                        when (val model = getModel<Any>()) {
                            is FakePoint -> {
                                getBinding<ItemFakePointBinding>().run {
                                    tvDescription.text = model.description

                                    val isChecked = model in getCheckedModels<FakePoint>()
                                    val backgroundColorNormalRes =
                                        if (isChecked) splitties.material.colors.R.color.blue_50 else splitties.material.colors.R.color.grey_50
                                    val borderColorNormalRes =
                                        if (isChecked) splitties.material.colors.R.color.blue_400 else splitties.material.colors.R.color.grey_100
                                    root.helper.run {
                                        backgroundColorNormal =
                                            context.color(backgroundColorNormalRes)
                                        borderColorNormal = context.color(borderColorNormalRes)
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

                        tvConfirm.visibility = if (checkedCount > 0) VISIBLE else INVISIBLE
                    }

                    onToggle { position, toggleMode, _ ->
                        // 管理按钮
                        tvManage.text = if (toggleMode) "取消" else "选取今日采样点"
                        // 如果取消管理模式则取消全部已选择
                        if (!toggleMode) checkedAll(false)
                    }

                }.models = Park.all()
        }

    }

    override fun initIntents() {
        binding.run {
            tvManage.setOnClickListener {
                rvTaskAll.bindingAdapter.toggle()
            }

            tvConfirm.setOnClickListener {
                rvTaskAll.bindingAdapter.getCheckedModels<FakePoint>()
                    .joinToString { it.description }.let { ToastUtils.showLong(it) }
                rvTaskAll.bindingAdapter.toggle()
            }
        }
    }
}