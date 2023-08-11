package com.unicorn.soilmonitoring.ui.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.dylanc.viewbinding.inflate
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.ItemParentBinding
import com.unicorn.soilmonitoring.databinding.ItemPointBinding
import com.unicorn.soilmonitoring.databinding.UiRecyclerViewBinding
import com.unicorn.soilmonitoring.ui.app.Config
import com.unicorn.soilmonitoring.ui.app.Parent
import com.unicorn.soilmonitoring.ui.app.Point

class PointRecyclerView(context: Context) : ConstraintLayout(context) {

    private val binding = inflate<UiRecyclerViewBinding>()

    init {
        binding.run {
            recyclerView.linear().setup {
                addType<Parent>(R.layout.item_parent)
                addType<Point>(R.layout.item_point)
                onBind {
                    getBindingOrNull<ItemParentBinding>()?.run {
                        tvParentDescription.text = getModel<Parent>().description
                    }
                    getBindingOrNull<ItemPointBinding>()?.run {
                        tvPointDescription.text = getModel<Point>().description
                    }
                }
                R.id.root.onFastClick {
                    getBindingOrNull<ItemParentBinding>()?.run {
                        expandOrCollapse()
                    }
                    getBindingOrNull<ItemPointBinding>()?.run {
                        sendEvent(getModel<Point>())
                    }
                }
            }.models = Config.points
        }
    }

}