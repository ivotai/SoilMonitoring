package com.unicorn.soilmonitoring.event

import com.unicorn.soilmonitoring.model.Collect

class CollectsChangeEvent(val isAdd: Boolean = true, val collect: Collect) {}