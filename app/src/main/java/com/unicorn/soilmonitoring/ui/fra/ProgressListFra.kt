package com.unicorn.soilmonitoring.ui.fra

import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.FraCollectProgressBinding
import com.unicorn.soilmonitoring.databinding.ItemCollectProgressBinding
import com.unicorn.soilmonitoring.model.CollectProgress
import com.unicorn.soilmonitoring.ui.act.CollectListAct
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.fragments.start
import splitties.resources.color

class ProgressListFra : BaseFra<FraCollectProgressBinding>() {

    override fun initViews() {
        binding.run {

            // 设置标题栏
            titleBar.run {
                titleBar.statusPadding()
                setTitle("采样记录")
                setTitleColor(color(R.color.white))
            }

            rv.grid(1).divider {
                orientation = DividerOrientation.GRID
                setDivider(32, true)
                includeVisible = true
            }.setup {
                addType<CollectProgress>(R.layout.item_collect_progress)
                onBind {
                    val model = getModel<CollectProgress>()
                    val binding = getBinding<ItemCollectProgressBinding>()
                    binding.tvKey1.text = model.key
                    binding.tvKey2.text = model.key2
                    binding.linearProgressIndicator.progress = model.progress
                }

                onFastClick(R.id.root) {
                    start<CollectListAct> {
                        putExtra("title", getModel<CollectProgress>().key)
                    }
                }

            }.models = listOf(
                CollectProgress("批次1", "7",70),
                CollectProgress("批次2", "10",100),
                CollectProgress("批次3","10", 100),
                CollectProgress("批次4", "10",100),
                CollectProgress("批次5", "10",100),
            )

        }
    }
}