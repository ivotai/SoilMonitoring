package com.unicorn.soilmonitoring.ui.act

import com.afollestad.materialdialogs.utils.MDUtil.textChanged
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.app.toast
import com.unicorn.soilmonitoring.databinding.ActSamplingBinding
import com.unicorn.soilmonitoring.databinding.ItemInputBinding
import com.unicorn.soilmonitoring.model.Dict
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.resources.color

class SamplingAct : BaseAct<ActSamplingBinding>() {


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
            rv.linear().setup {
                addType<Dict>(R.layout.item_input)

                onCreate {
                    if (itemViewType == R.layout.item_input){
                        getBinding<ItemInputBinding>().run {
                            etInput.textChanged {
                            val model =   getModel<Dict>()
                                model.text= it.toString()
                            }
                        }

                    }
                }

                onBind {
                    val model = getModel<Dict>()
                    getBinding<ItemInputBinding>().run {
                        etInput.setText(model.text)
                    }
                }

            }.models = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10").map { Dict(it) }
        }
    }

}