package com.unicorn.soilmonitoring.ui.fra

import com.bumptech.glide.Glide
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.app.Config
import com.unicorn.soilmonitoring.databinding.FraCollectListBinding
import com.unicorn.soilmonitoring.databinding.ItemCollectBinding
import com.unicorn.soilmonitoring.event.CollectsChangeEvent
import com.unicorn.soilmonitoring.model.Collect
import com.unicorn.soilmonitoring.model.CollectField
import com.unicorn.soilmonitoring.model.CollectLocalMedia
import com.unicorn.soilmonitoring.ui.act.AddCollectAct
import com.unicorn.soilmonitoring.ui.base.BaseFra
import splitties.activities.start
import splitties.resources.color

class CollectListFra : BaseFra<FraCollectListBinding>() {

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
                setDivider(16, true)
                includeVisible = true
            }.setup {
                addType<Collect>(R.layout.item_collect)

                onBind {
                    val binding = getBinding<ItemCollectBinding>()
                    val model = getModel<Collect>()
                    binding.run {
                        tvCollectNo.text = (model.models[6] as CollectField).value
                    }
                    val collectLocalMedia =
                        model.models.filterIsInstance<CollectLocalMedia>().first()
                    if (collectLocalMedia.localMedias.isNotEmpty())
                        Glide.with(context).load(collectLocalMedia.localMedias[0].path)
                            .into(binding.imageView)
                }

                onFastClick(R.id.tvDetail) {
                    context.start<AddCollectAct> {
                        putExtra("modelPosition", modelPosition)
                    }
                }
            }.models = Config.collectList
        }
    }

    override fun initEvents() {
        receiveEvent<CollectsChangeEvent> {
            binding.rv.adapter?.notifyDataSetChanged()
        }
    }

}