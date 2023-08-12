package com.unicorn.soilmonitoring.ui.fra

import android.view.View.GONE
import android.view.View.VISIBLE
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraTaskAllBinding
import com.unicorn.soilmonitoring.databinding.ItemTaskAllBinding
import com.unicorn.soilmonitoring.model.FakePoint
import com.unicorn.soilmonitoring.ui.base.BaseFra

class TaskAllFra : BaseFra<FraTaskAllBinding>() {

    override fun initViews() {

        binding.run {
            rvTaskAll.linear().setup {
                addType<FakePoint>(R.layout.item_task_all)
                onBind {
                    val model = getModel<FakePoint>()
                    getBinding<ItemTaskAllBinding>().run {
                        tvDescription.text = model.description
                        cbCheck.visibility = if (model.visibility) VISIBLE else GONE
                        cbCheck.isChecked = model.isChecked
                    }
                }

                // 点击列表触发选中
                onClick(R.id.cb_check, R.id.root) {
                    // 如果当前未处于选择模式下 点击无效
                    if (!toggleMode && it == R.id.root) {
                        return@onClick
                    }
                    var checked = getModel<FakePoint>().isChecked
                    if (it == R.id.root) checked = !checked
                    setChecked(layoutPosition, checked)
                }

                // 监听列表选中
                onChecked { position, isChecked, isAllChecked ->
                    val model = getModel<FakePoint>(position)
                    model.isChecked = isChecked
                    notifyItemChanged(position)
                }

                onToggle { position, toggleMode, _ ->
                    val model = getModel<FakePoint>(position)
                    model.visibility = toggleMode
                    notifyItemChanged(position)

                    // 管理按钮
                    tvManage.text = if (toggleMode) "取消" else "管理"

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