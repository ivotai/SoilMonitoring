package com.unicorn.soilmonitoring.ui.act

import android.graphics.Color
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.app.OrcResult
import com.unicorn.soilmonitoring.databinding.ActAddCollectBinding
import com.unicorn.soilmonitoring.databinding.GroupCollectFieldBinding
import com.unicorn.soilmonitoring.databinding.ItemCollectFieldBinding
import com.unicorn.soilmonitoring.model.Collect
import com.unicorn.soilmonitoring.model.CollectField
import com.unicorn.soilmonitoring.model.CollectFieldType
import com.unicorn.soilmonitoring.model.CollectGroup
import com.unicorn.soilmonitoring.model.InputType
import com.unicorn.soilmonitoring.model.SupportDivider

class AddCollectAct : BaiduOrcAct<ActAddCollectBinding>() {

    override fun onOrcResult(result: String) {
        val orcResult = GsonUtils.fromJson(result, OrcResult::class.java)
        orcResult.words_result.forEach {
            if (it.words.length == 14) {
                onFieldValueChange(6, "NJP32304")
                onFieldValueChange(7, it.words)
            }
        }
    }

    override fun initViews() {
        immersive(darkMode = true)

        binding.run {
            // 设置标题栏
            titleBar.run {
                titleBar.statusPadding()
                setTitle("采样记录")
            }

            rv.linear().divider {
                setColorRes(splitties.material.colors.R.color.grey_100)
                setDivider(width = 1, dp = false)
                setMargin(16, 16, true)
            }.setup {
                addType<CollectField>(R.layout.item_collect_field)
                addType<SupportDivider>(R.layout.item_divider)
                addType<CollectGroup>(R.layout.group_collect_field)

                onBind {
                    when (val item = getModel<Any>()) {
                        is CollectField -> {
                            val binding = getBinding<ItemCollectFieldBinding>()

                            // 设置圆角
                            val helper = binding.root.helper
                            val dp16 = SizeUtils.dp2px(16f).toFloat()
                            when (item.collectFieldType) {
                                CollectFieldType.TOP -> {
                                    helper.cornerRadiusTopLeft = dp16
                                    helper.cornerRadiusTopRight = dp16
                                    helper.cornerRadiusBottomLeft = 0f
                                    helper.cornerRadiusBottomRight = 0f
                                }

                                CollectFieldType.BOTTOM -> {
                                    helper.cornerRadiusTopLeft = 0f
                                    helper.cornerRadiusTopRight = 0f
                                    helper.cornerRadiusBottomLeft = dp16
                                    helper.cornerRadiusBottomRight = dp16
                                }

                                CollectFieldType.MIDDLE -> {
                                    helper.cornerRadiusTopLeft = 0f
                                    helper.cornerRadiusTopRight = 0f
                                    helper.cornerRadiusBottomLeft = 0f
                                    helper.cornerRadiusBottomRight = 0f
                                }
                            }

                            // 展示数据
                            binding.apply {
                                l.text = item.label
                                tv.hint = item.inputType.hint
                                tv.text = item.value
                            }
                        }

                        is CollectGroup -> {
                            val binding = getBinding<GroupCollectFieldBinding>()
                            binding.tv.text = item.name
                            if (item.icon != null) binding.iiv.icon =
                                IconicsDrawable(context, item.icon).apply {
                                    colorInt = Color.WHITE
                                    sizeDp = 24
                                }
                        }

                        is SupportDivider -> {
                            // do nothing
                        }
                    }
                }

                onClick(R.id.iiv) {
                    val item = getModel<Any>()
                    if (item is CollectGroup) {
                        if (item.icon == FontAwesome.Icon.faw_camera) {
                            startOrc()
                        }
                        if (item.icon == FontAwesome.Icon.faw_sync) {
                            ToastUtils.showShort("以更新定位及温湿度信息")
                        }
                    }
                }


                onFastClick(R.id.tv) {
                    val item = getModel<Any>()
                    if (item is CollectField) when (item.inputType) {
                        InputType.TEXT -> {
                            MaterialDialog(this@AddCollectAct, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                                title(text = "输入${item.label}")
                                input(prefill = item.value) { _, text ->
                                    onFieldValueChange(item.modelPosition, text.toString())
                                }
                                positiveButton(text = "确认")
                            }
                        }

                        InputType.SELECT -> {
                            val items = when (item.label) {
                                "点位类型" -> listOf("常规", "长期")
                                "剖面类型" -> listOf("剖面", "其他")
                                "采样深度" -> listOf("0-20cm", "20-40cm", "40-90cm")
                                else -> listOf("")
                            }
                            MaterialDialog(this@AddCollectAct, BottomSheet(LayoutMode.MATCH_PARENT)).show {
                                title(text = "选择${item.label}")
                                listItems(items = items) { _, index, text ->
                                    onFieldValueChange(item.modelPosition, text.toString())
                                }
                            }
                        }

                        else -> {
                            // do nothing
                        }
                    }
                }


            }


        }.models = collect.models
    }

    private val collect = Collect()

    private fun onFieldValueChange(
        modelPosition: Int, value: String
    ) {
        binding.rv.bindingAdapter.apply {
            // 刷新界面
            val item = models!![modelPosition] as CollectField
            item.value = value
            notifyItemChanged(modelPosition)


        }
    }

}