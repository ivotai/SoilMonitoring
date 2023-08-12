package com.unicorn.soilmonitoring.app

import com.baidu.mapapi.model.LatLng

data class Point(
    val description: String,
    val latLng: LatLng,
    val pointStatus: PointStatus = PointStatus.UN_TAKEN,
)