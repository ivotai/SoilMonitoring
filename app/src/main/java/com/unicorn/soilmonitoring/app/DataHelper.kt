package com.unicorn.soilmonitoring.app

import com.baidu.mapapi.model.LatLng

object DataHelper {

    public fun getParents(): List<Parent> {
        val parent1 = Parent("上海植物园")
        parent1.itemGroupPosition = 0
        parent1.sublist = listOf(
            Point("上海植物园采样点1", LatLng(31.152891, 121.450991)),
            Point("上海植物园采样点2", LatLng(31.153891, 121.450991), PointStatus.TAKEN),
            Point("上海植物园采样点3", LatLng(31.154891, 121.450991)),
        )
        return listOf(parent1)
    }

}