package com.unicorn.soilmonitoring.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.drake.channel.receiveEvent
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.colorInt
import com.unicorn.soilmonitoring.app.setUpWithViewPager2
import com.unicorn.soilmonitoring.databinding.ActMainBinding
import com.unicorn.soilmonitoring.model.FakePoint
import com.unicorn.soilmonitoring.ui.base.BaseAct
import com.unicorn.soilmonitoring.ui.fra.FakePointAllFra
import com.unicorn.soilmonitoring.ui.fra.TaskTodayFra
import me.majiajie.pagerbottomtabstrip.item.NormalItemView
import splitties.resources.color

class MainAct : BaseAct<ActMainBinding>() {

    override fun initViews() {
        super.initViews()

        val titles = listOf("采样点", "今日任务")

        binding.run {
            object : FragmentStateAdapter(this@MainAct) {

                override fun getItemCount(): Int = titles.size

                override fun createFragment(position: Int): Fragment {
                    return if (position == 0) FakePointAllFra(title = titles[position])
                    else TaskTodayFra()
                }

            }.run {
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
                    titles[0],
                    GoogleMaterial.Icon.gmd_location_pin,
                    GoogleMaterial.Icon.gmd_location_pin,
                )
            ).addItem(
                newItem(
                    titles[1],
                    GoogleMaterial.Icon.gmd_today,
                    GoogleMaterial.Icon.gmd_today,
                )
            ).build()
            navigationController.setUpWithViewPager2(viewPager2 = viewPager2)
        }
        initTab(binding.vp)
    }


    override fun initEvents() {
        receiveEvent<List<FakePoint>> {
            binding.vp.currentItem = 1
        }
    }

}