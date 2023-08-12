package com.unicorn.soilmonitoring.model

data class FakePoint(
    val description: String
) {

    companion object {

        val all
            get() = listOf(
                FakePoint("上海植物园采样点1"),
                FakePoint("上海植物园采样点2"),
                FakePoint("上海植物园采样点3"),
                FakePoint("上海植物园采样点4"),
                FakePoint("上海植物园采样点5"),
                FakePoint("上海植物园采样点6"),
                FakePoint("上海植物园采样点7"),
                FakePoint("上海植物园采样点8"),
                FakePoint("上海植物园采样点9"),
                FakePoint("上海植物园采样点10"),
            )

    }

}