package com.unicorn.soilmonitoring.ui.act

import android.graphics.Color
import com.blankj.utilcode.util.SizeUtils
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
import com.unicorn.soilmonitoring.databinding.ActAddCollectBinding
import com.unicorn.soilmonitoring.databinding.GroupCollectFieldBinding
import com.unicorn.soilmonitoring.databinding.ItemCollectFieldBinding
import com.unicorn.soilmonitoring.model.Collect
import com.unicorn.soilmonitoring.model.CollectField
import com.unicorn.soilmonitoring.model.CollectFieldType
import com.unicorn.soilmonitoring.model.CollectGroup
import com.unicorn.soilmonitoring.model.SupportDivider
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.resources.color

class AddCollectAct : BaseAct<ActAddCollectBinding>() {

    override fun initViews() {
        immersive(darkMode = true)

        binding.run {
            // 设置标题栏
            titleBar.run {
                titleBar.statusPadding()
                setTitle("采样记录")
            }

            rv.linear()
                .divider {
                    setColorRes(splitties.material.colors.R.color.grey_100)
                    setDivider(width = 1, dp = false)
                    setMargin(16, 16,true)
                }
                .setup {
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
                            if (item.icon != null)
                            binding.iiv.icon = IconicsDrawable(context, item.icon).apply {
                                colorInt = Color.WHITE
                                sizeDp = 24
                            }
                        }
                        is SupportDivider -> {
                            // do nothing
                        }
                    }
                }
            }




        }.models = collect.models
    }

    private val collect = Collect()

    private fun onFieldValueChange(
        item: CollectField, value: String
    ) {
        binding.rv.bindingAdapter.apply {
            // 刷新界面
            item.value = value
            notifyItemChanged(item.modelPosition)


        }
    }

}