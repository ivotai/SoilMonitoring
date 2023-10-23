package com.unicorn.soilmonitoring.model

import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome

class Collect (
    val models: MutableList<Any> = mutableListOf(
        CollectGroup("地理位置"),
        CollectField(label = "行政区", value = "崇明区", collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "绿地类型", value="公园", collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "采样地点", value="瀛洲公园，青色墙正对香樟树下", collectFieldType = CollectFieldType.BOTTOM),
        SupportDivider(),

        CollectGroup("采样袋（瓶）编号识别",FontAwesome.Icon.faw_camera),
        CollectField(label = "采样编号", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "报告编号", inputType=InputType.TEXT, collectFieldType = CollectFieldType.BOTTOM),
        SupportDivider(),

        CollectGroup("定位及温湿度",FontAwesome.Icon.faw_sync),
        CollectField(label = "GPS定位", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "温湿度", inputType=InputType.TEXT, collectFieldType = CollectFieldType.BOTTOM),
        SupportDivider(),

        CollectGroup("土壤采样"),
         CollectField(label = "点位类型", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "剖面类型", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "采样深度", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "植被类型及长势", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "土壤扰动情况", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "样品描述", inputType=InputType.TEXT, collectFieldType = CollectFieldType.BOTTOM),
    )


){
    init {
        models.forEachIndexed { index, any ->
            if (any is CollectField) {
                any.modelPosition = index
            }
        }
    }

}