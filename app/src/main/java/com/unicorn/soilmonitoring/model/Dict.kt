package com.unicorn.soilmonitoring.model

class Dict(
    var value: String,
    var parent: SampleCollectParent,
    var isChecked: Boolean = false
) {}