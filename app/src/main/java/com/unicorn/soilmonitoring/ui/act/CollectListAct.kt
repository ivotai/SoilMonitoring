package com.unicorn.soilmonitoring.ui.act

import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.app.Config
import com.unicorn.soilmonitoring.databinding.ActCollectListBinding
import com.unicorn.soilmonitoring.databinding.ItemCollectBinding
import com.unicorn.soilmonitoring.event.CollectsChangeEvent
import com.unicorn.soilmonitoring.model.Collect
import com.unicorn.soilmonitoring.model.CollectField
import com.unicorn.soilmonitoring.model.CollectLocalMedia
import com.unicorn.soilmonitoring.model.CollectParent
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.activities.start
import splitties.resources.color

class CollectListAct : BaseAct<ActCollectListBinding>() {

    private val t by lazy { intent.getStringExtra("title") }

    override fun initViews() {
        immersive()

        binding.run {

            ToastUtils.showShort(t)

            // 设置标题栏
            titleBar.run {
                titleBar.statusPadding()
                titleBar.setTitle(t!!)
                setTitleColor(color(R.color.white))
            }

            rv.linear().divider {
                setDivider(1, false)
            }.setup {
                addType<Collect>(R.layout.item_collect)

                onBind {
                    val model = getModel<Collect>()
                    val binding = getBinding<ItemCollectBinding>()
                    binding.run {
                        val no = (model.models[6] as CollectField).value
                        if (no.isNotEmpty()) {
                            tvCollectNo.text = (model.models[6] as CollectField).value
                        } else {
                            tvCollectNo.text = "NJP32304"
                        }
                    }
                    val collectLocalMedia =
                        model.models.filterIsInstance<CollectLocalMedia>().first()
                    if (collectLocalMedia.localMedias.isNotEmpty()) Glide.with(context)
                        .load(collectLocalMedia.localMedias[0].path).into(binding.imageView)
                    else Glide.with(context).load(R.drawable.collect).into(binding.imageView)
                }

                onFastClick(R.id.tvDetail) {
                    val model = getModel<Collect>()
                    if (model in com.unicorn.soilmonitoring.app.Config.currentCollectList) {
                        context.start<AddCollectAct> {
                            putExtra("modelPosition", modelPosition)
                        }
                    }
                }

            }.models =
                if (t == "批次1") com.unicorn.soilmonitoring.app.Config.currentCollectList
                else (1..20).toList().map { Collect() }
        }
    }

    override fun initEvents() {
        receiveEvent<CollectsChangeEvent> {
            binding.rv.bindingAdapter.notifyDataSetChanged()
        }
    }

}