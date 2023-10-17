package com.unicorn.soilmonitoring.ui.act

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.sdk.model.Word
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.GsonUtils
import com.bumptech.glide.Glide
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.unicorn.soilmonitoring.GlideEngine
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.app.OrcResult
import com.unicorn.soilmonitoring.databinding.ActSampleCollectBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectDictBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectImageBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectInputBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectLocalMediaBinding
import com.unicorn.soilmonitoring.databinding.ItemSampleCollectParentBinding
import com.unicorn.soilmonitoring.event.OrcResultEvent
import com.unicorn.soilmonitoring.model.Dict
import com.unicorn.soilmonitoring.model.SampleCollectInput
import com.unicorn.soilmonitoring.model.SampleCollectLocalMedia
import com.unicorn.soilmonitoring.model.SampleCollectParent
import me.saket.cascade.CascadePopupMenu
import splitties.resources.color


class SampleCollectAct : BaiduOrcAct<ActSampleCollectBinding>() {
    override fun onOrcResult(result: String) {
       val orcResult = GsonUtils.fromJson(result,OrcResult::class.java)
        orcResult.words_result.forEach {
            if (it.words.length == 14 ){
                sendEvent(OrcResultEvent(it.words))
            }
        }
    }

    @SuppressLint("ResourceType")
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
                        // 选项(dict)内容占1，其他占2
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
                //
                addType<SampleCollectLocalMedia>(R.layout.item_sample_collect_local_media)
                addType<SampleCollectParent>(R.layout.item_sample_collect_parent)
                addType<SampleCollectInput>(R.layout.item_sample_collect_input)
                addType<Dict>(R.layout.item_sample_collect_dict)

                onCreate {
                    if (itemViewType == R.layout.item_sample_collect_input) {
                        getBinding<ItemSampleCollectInputBinding>().run {
                            // todo text change
                        }
                    }

                    if (itemViewType == R.layout.item_sample_collect_local_media) {
                        getBinding<ItemSampleCollectLocalMediaBinding>().run {
                            rv.linear(orientation = HORIZONTAL).divider {
                                setDivider(8, true)
                            }.setup {
                                addType<LocalMedia>(R.layout.item_sample_collect_image)

                                onBind {
                                    getBinding<ItemSampleCollectImageBinding>().run {
                                        val localMedia = getModel<LocalMedia>()
                                        if (localMedia.path == null) {
                                            Glide.with(this@SampleCollectAct)
                                                .load(R.drawable.ps_ic_placeholder).into(iv)
                                        } else {
                                            Glide.with(this@SampleCollectAct).load(localMedia.path)
                                                .into(iv)
                                        }
                                    }
                                }

                                onClick(R.id.iv) {
                                    val sampleCollectLocalMedia =
                                        this@onCreate.getModel<SampleCollectLocalMedia>()
                                    val localMedia = getModel<LocalMedia>()
                                    if (localMedia.path == null) {
                                        PictureSelector.create(this@SampleCollectAct)
                                            .openGallery(sampleCollectLocalMedia.selectMimeType)
                                            .setImageEngine(GlideEngine.createGlideEngine())
                                            .forResult(object :
                                                OnResultCallbackListener<LocalMedia?> {
                                                override fun onResult(result: ArrayList<LocalMedia?>) {
                                                    val t = ArrayList<LocalMedia>()
                                                    result.forEach {
                                                        t.add(it!!)
                                                    }
                                                    t.add(LocalMedia())
                                                    rv!!.models = t
                                                    // 保存下来
                                                    sampleCollectLocalMedia.list = t
                                                }

                                                override fun onCancel() {}
                                            })
                                    } else {
                                        val list = ArrayList<LocalMedia>()
                                        models!!.forEachIndexed { index, any ->
                                            any as LocalMedia
                                            any.let { if (it.path != null) list.add(it) }
                                        }
                                        PictureSelector.create(this@SampleCollectAct).openPreview()
                                            .setImageEngine(GlideEngine.createGlideEngine())
                                            .startActivityPreview(
                                                modelPosition, false, list
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
                onBind {
                    when (val model = getModel<Any>()) {
                        is SampleCollectLocalMedia -> {
                            getBinding<ItemSampleCollectLocalMediaBinding>().rv.bindingAdapter.models =
                                model.list
                        }

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
                                    if (isChecked) context.color(splitties.material.colors.R.color.green_400) else context.color(
                                        splitties.material.colors.R.color.grey_100
                                    )
                                val textColor =
                                    if (isChecked) context.color(R.color.white) else context.color(
                                        R.color.black
                                    )
                                tv.helper.run {
                                    backgroundColorNormal = backgroundColorNormalInt
                                    cornerRadius = ConvertUtils.dp2px(100f).toFloat()
                                    shadowRadius = if (isChecked) ConvertUtils.dp2px(1f) else 0
                                }
                                tv.setTextColor(textColor)
                            }
                        }
                    }
                }

                onClick(R.id.tv) {
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

            }.models = SampleCollectParent.all

        }
    }


    override fun initIntents() {
        super.initIntents()

        binding.apply {
            titleBar.getCiv2().visibility = View.VISIBLE
            titleBar.getCiv2().setTextColor(Color.WHITE)
            titleBar.getCiv2().setOnClickListener {
                val popup = CascadePopupMenu(this@SampleCollectAct, titleBar.getCiv2())
                popup.menu.run {
                    add("识别采样袋编号").setOnMenuItemClickListener {
                        startOrc()
                        return@setOnMenuItemClickListener true
                    }
                    add("查看历次采样记录").setOnMenuItemClickListener { true }
                    add("查看采样点变更信息").setOnMenuItemClickListener { true }
                    add("从历史数据中导入").setOnMenuItemClickListener { true }
                    add("保存本次采样记录").setOnMenuItemClickListener { true }
                }
                popup.show()
            }
        }
//   takePhoto()
    }

    override fun initEvents() {
       receiveEvent<OrcResultEvent> {
         val sampleCollectInputs =  binding.rv.models!!.filter { it is SampleCollectInput } as List<SampleCollectInput>
           sampleCollectInputs[1].value = it.no
           binding.rv.adapter!!.notifyItemChanged(7)
       }
    }
}