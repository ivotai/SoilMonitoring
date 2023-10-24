package com.unicorn.soilmonitoring.ui.fra

import com.bumptech.glide.Glide
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.statusbar.statusPadding
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.app.Config
import com.unicorn.soilmonitoring.app.PointStatus
import com.unicorn.soilmonitoring.databinding.FraCollectListBinding
import com.unicorn.soilmonitoring.databinding.ItemCollectBinding
import com.unicorn.soilmonitoring.databinding.ItemParentBinding
import com.unicorn.soilmonitoring.event.CollectsChangeEvent
import com.unicorn.soilmonitoring.model.Collect
import com.unicorn.soilmonitoring.model.CollectField
import com.unicorn.soilmonitoring.model.CollectLocalMedia
import com.unicorn.soilmonitoring.model.CollectParent
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

            rv.linear().divider {
                setDivider(1, false)
            }.setup {
                addType<Collect>(R.layout.item_collect)
                addType<CollectParent>(R.layout.item_parent)

                onBind {
                    val model = getModel<Any>()
                    when {
                        model is CollectParent -> {
                            val binding = getBinding<ItemParentBinding>()
                            val icon =
                                if (model.itemExpand) FontAwesome.Icon.faw_chevron_circle_down else FontAwesome.Icon.faw_chevron_circle_right
                            binding.iiv.icon = IconicsDrawable(context, icon).apply {
                                colorInt = color(splitties.material.colors.R.color.grey_700)
                                sizeDp = 24
                            }
                            binding.tvParentDescription.text = model.key
                        }

                        model is Collect -> {
                            val binding = getBinding<ItemCollectBinding>()
                            binding.run {
                                val no = (model.models[6] as CollectField).value
                                if (no.isNotEmpty()) {
                                    tvCollectNo.text = (model.models[6] as CollectField).value
                                }else{
                                    tvCollectNo.text = "NJP32304"
                                }
                            }
                            val collectLocalMedia =
                                model.models.filterIsInstance<CollectLocalMedia>().first()
                            if (collectLocalMedia.localMedias.isNotEmpty()) Glide.with(context)
                                .load(collectLocalMedia.localMedias[0].path).into(binding.imageView)
                            else
                                Glide.with(context).load(R.drawable.collect).into(binding.imageView)
                        }
                    }
                }

                onFastClick(R.id.tvDetail) {
//                    ToastUtils.showShort(modelPosition.toString())
                    val model = getModel<Collect>()
                    if (model in Config.currentCollectList) {
                        context.start<AddCollectAct> {
                            putExtra("modelPosition", modelPosition - 1)
                        }
                    }
                }

                onFastClick(R.id.root) {
                    expandOrCollapse()
                }

            }.models = getData()
        }
    }

    private fun getData(): List<CollectParent> {
        val collectParentList = mutableListOf<CollectParent>()
//        val total = Config.points.size
//        val taken = Config.points.count { it.pointStatus == PointStatus.TAKEN }
//        val progress = 100 * taken / total
        collectParentList.add(CollectParent("批次1(${50}%)").apply {
            itemGroupPosition = 0
            itemExpand = false
            sublist = Config.currentCollectList
        })
        collectParentList.add(CollectParent("批次2(100%)").apply {
            itemGroupPosition = 1
            itemExpand = false
            sublist = MutableList(5) { Collect() }
        })
        collectParentList.add(CollectParent("批次3(100%)").apply {
            itemGroupPosition = 2
            itemExpand = false
            sublist = MutableList(5) { Collect() }
        })
        collectParentList.add(CollectParent("批次4(100%)").apply {
            itemGroupPosition = 3
            itemExpand = false
            sublist = MutableList(5) { Collect() }
        })
        collectParentList.add(CollectParent("批次5(100%)").apply {
            itemGroupPosition = 4
            itemExpand = false
            sublist = MutableList(5) { Collect() }

        })
        return collectParentList
    }


    override fun initEvents() {
        receiveEvent<CollectsChangeEvent> {
            val adapter = binding.rv.bindingAdapter
            val parent = adapter.models!!.filterIsInstance<CollectParent>().first()
            if (it.isAdd) {
                // 这里的代码超级搞
                if (parent.itemExpand) {
                    adapter.addModels(listOf(it.collect), false, 1)
                    parent.sublist.add(0, it.collect)
//                    adapter.notifyItemInserted(0)
                } else {
                    parent.sublist.add(0, it.collect)
                }
//                parent.sublist.add(0,it.collect)
//                adapter.addModels(listOf(it.collect),false,1)
//                val list = listOf(it.collect) + Config.currentCollectList
//                parent.sublist = list
//                Config.currentCollectList = list
//                adapter.notifyDataSetChanged()
//                parent.sublist.add(0, it.collect)
//                adapter.notifyItemInserted(0)
            } else {
                adapter.notifyItemChanged(Config.currentCollectList.indexOf(it.collect) + 1)
            }

//            adapter.notifyDataSetChanged()
//            adapter.notifyItemChanged(1)
//            binding.rv.adapter?.notifyItemRangeChanged(1, Config.currentCollectList.size)
        }
    }

}