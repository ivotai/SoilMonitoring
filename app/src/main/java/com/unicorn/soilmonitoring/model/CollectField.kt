package com.unicorn.soilmonitoring.model

class CollectField(
    val label: String,
    val inputType: InputType = InputType.TEXT,
    var value: String = "",
    val allowEmpty: Boolean = true,
    var modelPosition: Int = 0,
    var collectFieldType: CollectFieldType = CollectFieldType.MIDDLE,
)