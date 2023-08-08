package com.unicorn.soilmonitoring.ui.app

import com.baidu.mapapi.model.LatLng

object DataHelper {

    public fun getPoints(): List<LatLng> {
        return listOf(
            LatLng(31.153691, 121.450991),
            LatLng(31.153891, 121.450991),
            LatLng(31.154091, 121.450991),
        )
    }

}