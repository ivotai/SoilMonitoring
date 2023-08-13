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

    var sublist: List<FakePoint> = ArrayList()

    companion object {

        val all
            get() = fun(): List<Any> {
                val parks = ArrayList<Park>()
                for (i in 1..10) {
                    val park = Park("园区$i")
                    park.itemGroupPosition = i
                    val sublist = ArrayList<FakePoint>()
                    for (j in 1..10) {
                        sublist.add(FakePoint("采样点$i-$j", park))
                    }
                    park.sublist = sublist
                    parks.add(park)
                }
                return parks
            }

    }

}