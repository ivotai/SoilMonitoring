package com.unicorn.soilmonitoring.ui.act

import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.setup
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.ActSampleCollectBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectDictBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectInputBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectParentBinding
import com.unicorn.soilmonitoring.model.Dict
import com.unicorn.soilmonitoring.model.SampleCollectInput
import com.unicorn.soilmonitoring.model.SampleCollectParent
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.resources.color

class SampleCollectAct : BaseAct<ActSampleCollectBinding>() {

    override fun initViews() {
        immersive()

        binding.run {
            // 设置标题栏
            titleBar.run {
                titleBar.statusPadding()
                setTitle("采样")
                setTitleColor(color(R.color.white))
            }

            //
            val scanCount = 2
            val layoutManager = GridLayoutManager(this@SampleCollectAct, scanCount)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < 0) return 1 // 如果添加分割线可能导致position为负数
                    return when (rv.bindingAdapter.getItemViewType(position)) {
                        R.layout.item_sample_collect_dict -> 1
                        else -> scanCount
                    }
                }
            }

            rv.layoutManager = layoutManager
            rv.divider {
                orientation = DividerOrientation.GRID
                setDivider(16, true)
                startVisible = true
                endVisible = true
            }.setup {
                addType<SampleCollectParent>(R.layout.item_sample_collect_parent)
                addType<SampleCollectInput>(R.layout.item_sample_collect_input)
                addType<Dict>(R.layout.item_sample_collect_dict)

                onCreate {
                    if (itemViewType == R.layout.item_sample_collect_input) {
                        getBinding<ItemSampleCollectInputBinding>().run {
                            // todo text change
                        }
                    }
                }

                onBind {
                    when (val model = getModel<Any>()) {
                        is SampleCollectParent -> {
                            getBinding<ItemSampleCollectParentBinding>().run {
                                tvDescription.text = model.description
                            }
                        }

                        is SampleCollectInput -> {
                            getBinding<ItemSampleCollectInputBinding>().run {
                                tv.hint = model.value
                            }
                        }

                        is Dict -> {
                            getBinding<ItemSampleCollectDictBinding>().run {
                                tv.text = model.value

                                val isChecked = model in getCheckedModels<Dict>()
                                val backgroundColorNormalInt =
                                    if (isChecked) context.color(splitties.material.colors.R.color.blue_400) else context.color(
                                        splitties.material.colors.R.color.grey_100
                                    )
//                                val borderColorNormalInt =
//                                    if (isChecked) context.color(splitties.material.colors.R.color.blue_400) else Color.parseColor(
//                                        "#F6F6F6"
//                                    )
                                root.helper.run {
                                    backgroundColorNormal = backgroundColorNormalInt
                                    cornerRadius = ConvertUtils.dp2px(100f).toFloat()
                                }
                            }
                        }
                    }
                }

                onClick(R.id.root) {
                    val model = getModel<Any>()
                    if (model is Dict) {
                        val isChecked = model in getCheckedModels<Dict>()
                        if (isChecked) {
                            setChecked(modelPosition, false)
                        } else {
                            // 取消其他选中
                            model.parent.sublist.forEach {
                                // 这个 position 可能有问题，严格提防 headCount
                                val position = models!!.indexOf(it)
                                setChecked(position, false)
                            }
                            setChecked(modelPosition, true)
                        }
                    }
                }

                onChecked { position, isChecked, _ ->
                    notifyItemChanged(position)
                }

            }.models = SampleCollectParent.all()

        }
    }


}