package com.unicorn.soilmonitoring.model

import com.drake.brv.item.ItemExpand
import com.luck.picture.lib.entity.LocalMedia

class SampleCollectParent(val description: String) : ItemExpand {

    // todo
    // 同级别分组的索引位置
    override var itemGroupPosition: Int = 0

    // 当前条目是否展开
    override var itemExpand: Boolean = true

    // 返回子列表
    override fun getItemSublist(): List<Any> {
        return sublist
    }

    var sublist: List<Any> = ArrayList()

    companion object {

        val all
            get() = ArrayList<SampleCollectParent>().apply {
                add(SampleCollectParent("采样痕迹").apply {
                    sublist = listOf(SampleCollectRecycler(ArrayList<LocalMedia>().apply {
                        add(LocalMedia())
                    }))
                })
                add(SampleCollectParent("采样编号").apply {
                    sublist = listOf(SampleCollectInput())
                })
                add(SampleCollectParent("点位类型").apply {
                    sublist = listOf("常规", "长期").map { Dict(it, this) }
                })
                add(SampleCollectParent("剖面类型").apply {
                    sublist = listOf("剖面", "其他").map { Dict(it, this) }
                })
                add(SampleCollectParent("采样地点").apply {
                    sublist = listOf("徐汇区").map { Dict(it, this) }
                })
                add(SampleCollectParent("绿地类型").apply {
                    sublist = listOf("公园").map { Dict(it, this) }
                })
                add(SampleCollectParent("检测项目").apply {
                    sublist = listOf("LB").map { Dict(it, this) }
                })
                add(SampleCollectParent("采样深度").apply {
                    sublist = listOf("0-20cm", "20-40cm", "40-90cm").map { Dict(it, this) }
                })
                add(SampleCollectParent("植被类型及长势").apply {
                    sublist = listOf(SampleCollectInput())
                })
                add(SampleCollectParent("GPS定位").apply {
                    sublist = listOf(SampleCollectInput())
                })
                add(SampleCollectParent("土壤扰动情况").apply {
                    sublist = listOf(SampleCollectInput())
                })
                add(SampleCollectParent("样品描述").apply {
                    sublist = listOf(SampleCollectInput())
                })
                add(SampleCollectParent("温湿度").apply {
                    sublist = listOf(SampleCollectInput())
                })
            }


    }

}