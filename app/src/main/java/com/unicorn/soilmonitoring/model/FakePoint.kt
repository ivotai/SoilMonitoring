package com.unicorn.soilmonitoring.model

data class FakePoint(
    val no: String = "HNC150${(1..99).random()}",
    val park: Park,
    val isGather: Boolean = listOf(true, false).random()
) {

}