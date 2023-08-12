package com.unicorn.soilmonitoring.ui.fra

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraTaskAllBinding
import com.unicorn.soilmonitoring.databinding.ItemTaskAllBinding
import com.unicorn.soilmonitoring.model.FakePoint
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.resources.color


class TaskAllFra : BaseFra<FraTaskAllBinding>() {

    override fun initViews() {

        binding.run {
            rvTaskAll.grid(1).divider {
                orientation = DividerOrientation.GRID
                setDivider(16, true)
                includeVisible = true
            }.setup {
                addType<FakePoint>(com.unicorn.soilmonitoring.R.layout.item_task_all)
                onBind {
                    val model = getModel<FakePoint>()
                    getBinding<ItemTaskAllBinding>().run {
                        tvDescription.text = model.description

                        val borderColorRes =
                            if (model.isChecked) splitties.material.colors.R.color.blue_400 else splitties.material.colors.R.color.grey_100
                        val color =
                            if (model.isChecked) splitties.material.colors.R.color.blue_50 else splitties.material.colors.R.color.grey_50

                        root.helper.run {
                        backgroundColorNormal  = context.color(color)
                        borderColorNormal = context.color(borderColorRes)
                        }
                    }
                }

                // 长按列表进入编辑模式
                onLongClick(R.id.root) {
                    if (!toggleMode) {
                        toggle()
                        setChecked(layoutPosition, true)
                    }
                }

                // 点击列表触发选中
                onClick(R.id.root) {
                    // 如果当前未处于选择模式下 点击无效
                    if (toggleMode) checkedSwitch(layoutPosition)
                }

                // 监听列表选中
                onChecked { position, isChecked, _ ->
                    val model = getModel<FakePoint>(position)
                    model.isChecked = isChecked
                    notifyItemChanged(position)

                    tvConfirm.visibility = if (checkedCount > 0) VISIBLE else INVISIBLE
                }

                onToggle { position, toggleMode, _ ->
                    // 管理按钮
                    tvManage.text = if (toggleMode) "取消" else "选取今日采样点"
                    // 如果取消管理模式则取消全部已选择
                    if (!toggleMode) checkedAll(false)
                }

            }.models = FakePoint.all
        }

    }

    override fun initIntents() {
        binding.run {
            tvManage.setOnClickListener {
                rvTaskAll.bindingAdapter.toggle()
            }
        }
    }
}