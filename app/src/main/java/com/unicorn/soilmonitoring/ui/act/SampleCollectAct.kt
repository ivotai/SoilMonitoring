package com.unicorn.soilmonitoring.ui.act

import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.ActSampleCollectBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectInputBinding
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
            rv.linear().setup {
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
//                    val model = getModel<String>()
                }

                onClick(R.id.tv) {}


            }.models = SampleCollectParent.all()


        }
    }


}