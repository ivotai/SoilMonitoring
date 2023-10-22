package com.unicorn.soilmonitoring.ui.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.DeviceUtils.getModel
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.dylanc.viewbinding.inflate
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.ItemParentBinding
import com.unicorn.soilmonitoring.databinding.ItemPointBinding
import com.unicorn.soilmonitoring.databinding.UiRecyclerViewBinding
import com.unicorn.soilmonitoring.app.Config
import com.unicorn.soilmonitoring.app.Parent
import com.unicorn.soilmonitoring.app.Point

class PointRecyclerView(context: Context) : ConstraintLayout(context) {

    private val binding = inflate<UiRecyclerViewBinding>()

    init {
        binding.run {
            val bindingAdapter=recyclerView.linear().setup {
                addType<Parent>(R.layout.item_parent)
                addType<Point>(R.layout.item_point)
                onCreate {

                }
                onBind {
                    getBindingOrNull<ItemParentBinding>()?.run {
                        tvParentDescription.text = getModel<Parent>().description
                    }
                    getBindingOrNull<ItemPointBinding>()?.run {
                        tvPointDescription.text = getModel<Point>().key
                    }
                }
                R.id.root.onFastClick {
                    getBindingOrNull<ItemParentBinding>()?.run {
                        expandOrCollapse()
                    }
                    getBindingOrNull<ItemPointBinding>()?.run {
//                        sendEvent(getModel<Point>())
                        getModel<Point>()
                    }
                }


            }
                bindingAdapter.models = Config.points

        }
    }

}