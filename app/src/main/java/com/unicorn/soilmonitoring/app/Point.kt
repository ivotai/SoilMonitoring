package com.unicorn.soilmonitoring.app

import com.baidu.mapapi.model.LatLng

data class Point(
    val key: String,
    val latLng: LatLng,
    val pointStatus: PointStatus = PointStatus.UN_TAKEN,
    val distance : Double = 0.0
)