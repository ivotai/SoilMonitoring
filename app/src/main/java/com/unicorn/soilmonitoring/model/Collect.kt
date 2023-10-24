package com.unicorn.soilmonitoring.model

import android.graphics.Bitmap.Config
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome

class Collect(
    val models: MutableList<Any> = mutableListOf(
        CollectGroup("地理位置"),
        CollectField(
            label = "行政区",
            value = "崇明区",
            inputType = InputType.NOT_EDITABLE,
            collectFieldType = CollectFieldType.MIDDLE
        ),
        CollectField(
            label = "绿地类型",
            value = "公园",
            inputType = InputType.NOT_EDITABLE,
            collectFieldType = CollectFieldType.MIDDLE
        ),
        CollectField(
            label = "采样地点",
            value = "瀛洲公园，青色墙正对香樟树下",
            inputType = InputType.NOT_EDITABLE,
            collectFieldType = CollectFieldType.BOTTOM
        ),
        SupportDivider(),

        CollectGroup("采样袋（瓶）编号识别", FontAwesome.Icon.faw_camera),
        CollectField(
            label = "采样编号",
            inputType = InputType.TEXT,
            collectFieldType = CollectFieldType.MIDDLE
        ),
        CollectField(
            label = "报告编号",
            inputType = InputType.TEXT,
            collectFieldType = CollectFieldType.BOTTOM
        ),
        SupportDivider(),

        CollectGroup("定位及温湿度", FontAwesome.Icon.faw_sync),
        CollectField(
            label = "GPS定位",
            inputType = InputType.TEXT,
            collectFieldType = CollectFieldType.MIDDLE,
            value = "经度：${com.unicorn.soilmonitoring.app.Config.selfPoint.latLng.latitude}\n纬度：${com.unicorn.soilmonitoring.app.Config.selfPoint.latLng.longitude}"
        ),
        CollectField(
            label = "温湿度", inputType = InputType.TEXT, collectFieldType = CollectFieldType.BOTTOM,
            value = "温度：19℃\n湿度：72%"
        ),
        SupportDivider(),

        CollectGroup("土壤采样"),
        CollectField(
            label = "点位类型",
            inputType = InputType.SELECT,
            collectFieldType = CollectFieldType.MIDDLE
        ),
        CollectField(
            label = "剖面类型",
            inputType = InputType.SELECT,
            collectFieldType = CollectFieldType.MIDDLE
        ),
        CollectField(
            label = "采样深度",
            inputType = InputType.SELECT,
            collectFieldType = CollectFieldType.MIDDLE
        ),
        CollectField(
            label = "植被类型及长势",
            inputType = InputType.TEXT,
            collectFieldType = CollectFieldType.MIDDLE
        ),
        CollectField(
            label = "土壤扰动情况",
            inputType = InputType.TEXT,
            collectFieldType = CollectFieldType.MIDDLE
        ),
        CollectField(
            label = "样品描述",
            inputType = InputType.TEXT,
            collectFieldType = CollectFieldType.BOTTOM
        ),
        SupportDivider(),
        CollectLocalMedia(),
    )


) {
    init {
        models.forEachIndexed { index, any ->
            if (any is CollectField) {
                any.modelPosition = index
            }
            if (any is CollectLocalMedia) {
                any.modelPosition = index
            }
        }
    }

}