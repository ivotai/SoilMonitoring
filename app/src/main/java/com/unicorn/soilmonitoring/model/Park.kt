package com.unicorn.soilmonitoring.model

import com.drake.brv.item.ItemExpand

class Park(val description: String) : ItemExpand {
    // 同级别分组的索引位置
    override var itemGroupPosition: Int = 0

    // 当前条目是否展开
    override var itemExpand: Boolean = true

    // 返回子列表
    override fun getItemSublist(): List<FakePoint> {
        return sublist
    }

    var sublist: ArrayList<FakePoint> = ArrayList()

    companion object {

        val all
            get() = fun(): List<Any> {
                val parks = ArrayList<Park>()
                parks.apply {
                    add(Park("批次").apply {
                        itemGroupPosition = 0
                        sublist = ArrayList()
                        sublist.add(FakePoint(no = "批次1", park = this))
                        sublist.add(FakePoint(no = "批次2", park = this))
                        sublist.add(FakePoint(no = "批次3", park = this))
                        sublist.add(FakePoint(no = "批次4", park = this))
                        sublist.add(FakePoint(no = "批次5", park = this))
                    })
                    add(Park("状态").apply {
                        itemGroupPosition = 1
                        sublist = ArrayList()
                        sublist.add(FakePoint(no = "未完成", park = this))
                        sublist.add(FakePoint(no = "已完成", park = this))
                        sublist.add(FakePoint(no = "全部", park = this))
                    })
                    add(Park("排序").apply {
                        itemGroupPosition = 2
                        sublist = ArrayList()
                        sublist.add(FakePoint(no = "由近道远", park = this))
                        sublist.add(FakePoint(no = "名称", park = this))
                        sublist.add(FakePoint(no = "采样编号", park = this))
                    })
                }
                return parks
            }

    }

}