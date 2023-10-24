package com.unicorn.soilmonitoring.model

enum class InputType(
    val hint: String = "",
) {
    TEXT(hint = "请输入"),
    NUMBER,
    DATE,
    TIME,
    NOT_EDITABLE,
    SELECT(hint = "请选择"),
    LOCAL_MEDIA,
    MULTISELECT,
    RADIO,
    CHECKBOX,
    TEXTAREA,
    FILE,
    IMAGE,
    SIGNATURE,
    LOCATION,
    BARCODE,
    QR,
    EMAIL,
    PHONE,
    URL,
    PASSWORD,
    HIDDEN,
    ;

}