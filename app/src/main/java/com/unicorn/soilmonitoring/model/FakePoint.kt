package com.unicorn.soilmonitoring.model

data class FakePoint(
    val description: String
) {

    companion object {

        val all
            get() = fun(): List<FakePoint> {
                val list = ArrayList<FakePoint>()
                for (i in 1..100) {
                    list.add(FakePoint("采样点$i"))
                }
                return list
            }

    }

}