package com.unicorn.soilmonitoring.app

import com.drake.brv.item.ItemExpand

class Parent(val description:String) : ItemExpand {
    // 同级别分组的索引位置
    override var itemGroupPosition: Int = 0

    // 当前条目是否展开
    override var itemExpand: Boolean = false

    // 返回子列表
    override fun getItemSublist(): List<Point> {
        return sublist
    }

     var sublist: List<Point> = listOf()
}