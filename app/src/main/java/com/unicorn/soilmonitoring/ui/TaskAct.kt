package com.unicorn.soilmonitoring.ui

import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ColorUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.colorInt
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.app.setUpWithViewPager2
import com.unicorn.soilmonitoring.databinding.ActTaskBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct
import me.majiajie.pagerbottomtabstrip.item.NormalItemView
import splitties.resources.color

class TaskAct : BaseAct<ActTaskBinding>() {

    override fun initViews() {
        super.initViews()

        binding.run {
            TaskFragmentStateAdapter(this@TaskAct).run {
                vp.isUserInputEnabled = false
                vp.offscreenPageLimit = itemCount - 1
                vp.adapter = this
            }
        }

        fun initTab(viewPager2: ViewPager2) {

            fun newItem(vTitle: String, iconDefault: IIcon, iconChecked: IIcon) =
                NormalItemView(this).apply {
                    val defaultColor = color(splitties.material.colors.R.color.grey_400)
                    val checkedColor = color(splitties.material.colors.R.color.light_green_400)
                    title = vTitle
                    setDefaultDrawable(IconicsDrawable(context, iconDefault).apply {
                        colorInt = defaultColor
                    })
                    setTextDefaultColor(defaultColor)
                    setSelectedDrawable(IconicsDrawable(context, iconChecked).apply {
                        colorInt = checkedColor
                    })
                    setTextCheckedColor(checkedColor)
                }

            val navigationController = binding.tab.custom().addItem(
                newItem(
                    "今日任务",
                    GoogleMaterial.Icon.gmd_today,
                    GoogleMaterial.Icon.gmd_today,
                )
            ).addItem(
                newItem(
                    "全部采样点",
                    GoogleMaterial.Icon.gmd_location_pin,
                    GoogleMaterial.Icon.gmd_location_pin,
                )
            ).build()
            navigationController.setUpWithViewPager2(viewPager2 = viewPager2)
        }
        initTab(binding.vp)
    }

}