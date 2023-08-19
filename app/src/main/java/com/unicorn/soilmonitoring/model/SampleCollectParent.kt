package com.unicorn.soilmonitoring.model

import com.drake.brv.item.ItemExpand

class SampleCollectParent(val label: String) : ItemExpand {
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
            get() = fun(): List<SampleCollectParent> {
                val list = ArrayList<SampleCollectParent>()
                list.apply {
                    add(SampleCollectParent("采样编号").apply {
                        sublist = listOf("").map { SampleCollectInput(it) }
                    })
                    add(SampleCollectParent("点位类型").apply {
                        sublist = listOf("常规", "长期").map { Dict(it) }
                    })
                }
                return list
            }

    }

}