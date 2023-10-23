package com.unicorn.soilmonitoring.model

class Collect (
    val models: MutableList<Any> = mutableListOf(
        CollectGroup("编号"),
        CollectField(label = "采样编号", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "报告编号", inputType=InputType.TEXT, collectFieldType = CollectFieldType.BOTTOM),

        CollectGroup("自动采集部分"),
        CollectField(label = "GPS定位", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "温湿度", inputType=InputType.TEXT, collectFieldType = CollectFieldType.BOTTOM),

        CollectGroup("地理位置"),
        CollectField(label = "行政区", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "绿地类型", inputType=InputType.TEXT, collectFieldType = CollectFieldType.MIDDLE),
        CollectField(label = "采样地点", inputType=InputType.TEXT, collectFieldType = CollectFieldType.BOTTOM),

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