package com.unicorn.soilmonitoring.app

import android.content.Context
import android.os.Bundle
import androidx.annotation.ColorRes
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.Overlay
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import splitties.resources.color


object MarkerHelper {

    fun addOverlay(
        context: Context, point: Point, @ColorRes colorRes: Int, icon: IIcon, baiduMap: BaiduMap
    ): Overlay {

        val iconicsDrawable = IconicsDrawable(context, icon).apply {
            colorInt = context.color(colorRes)
            sizeDp = 24
        }
        val extraInfo = Bundle().apply { putSerializable("key", point.key) }
        val option = MarkerOptions().position(point.latLng).perspective(true).extraInfo(extraInfo)
            .icon(BitmapDescriptorFactory.fromBitmap(iconicsDrawable.toBitmap()))
        return baiduMap.addOverlay(option)
    }

}