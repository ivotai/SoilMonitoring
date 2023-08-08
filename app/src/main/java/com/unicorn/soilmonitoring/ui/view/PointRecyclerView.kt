package com.unicorn.soilmonitoring.ui.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.baidu.mapapi.model.LatLng
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.dylanc.viewbinding.inflate
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.ItemPointBinding
import com.unicorn.soilmonitoring.databinding.UiRecyclerViewBinding
import com.unicorn.soilmonitoring.ui.app.DataHelper

class PointRecyclerView(context: Context) : ConstraintLayout(context) {

    private val binding = inflate<UiRecyclerViewBinding>()

    init {
        binding.run {
            recyclerView.linear().setup {
                addType<LatLng>(R.layout.item_point)
                onBind {
                    val binding = getBinding<ItemPointBinding>()
                    val latLng = getModel<LatLng>()
                    binding.tvPointDescription.text = latLng.latitude.toString()
                }
            }.models = DataHelper.getPoints()
        }
    }

}