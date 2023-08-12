package com.unicorn.soilmonitoring.app

import com.baidu.mapapi.model.LatLng

object Config {

    val locationScanSpan = 1000

    val defaultZoom = 17.0f

    val points = ArrayList<Point>()

    var selfPoint = Point("自身", LatLng(0.0, 0.0))

}