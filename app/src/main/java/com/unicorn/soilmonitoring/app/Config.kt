package com.unicorn.soilmonitoring.app

import android.graphics.Color
import com.baidu.mapapi.model.LatLng
import com.unicorn.soilmonitoring.model.Collect

object Config {

    val locationScanSpan = 1000

    val defaultZoom = 17.0f

    val points = ArrayList<Point>()

    var selfPoint = Point("本人", LatLng(0.0, 0.0))

    val collectList = ArrayList<Collect>()

    var city = "上海"

}