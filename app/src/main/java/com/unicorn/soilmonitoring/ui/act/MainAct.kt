package com.unicorn.soilmonitoring.ui.act

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.drake.channel.receiveEvent
import com.drake.statusbar.immersive
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.utils.colorInt
import com.unicorn.soilmonitoring.MapFra
import com.unicorn.soilmonitoring.app.setUpWithViewPager2
import com.unicorn.soilmonitoring.databinding.ActMainBinding
import com.unicorn.soilmonitoring.event.MapEvent
import com.unicorn.soilmonitoring.ui.Fas
import com.unicorn.soilmonitoring.ui.base.BaseAct
import com.unicorn.soilmonitoring.ui.fra.FakePointAllFra
import com.unicorn.soilmonitoring.ui.fra.TaskTodayFra
import me.majiajie.pagerbottomtabstrip.item.NormalItemView
import splitties.resources.color

class MainAct : BaseAct<ActMainBinding>() {

    override fun initViews() {
        immersive()

        val titles = listOf("采样点", "当前采样", "地图")

        binding.run {
            object : FragmentStateAdapter(this@MainAct) {

                override fun getItemCount(): Int = titles.size

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> FakePointAllFra()
                        1 -> TaskTodayFra()
                        else -> MapFra()
                    }
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
                    val checkedColor = color(splitties.material.colors.R.color.blue_600)

//                    val checkedColor = Color.parseColor("#4485E1")
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

            Fas.Icon.fas_circle
            val navigationController = binding.tab.custom().addItem(
                newItem(
                    titles[0], Fas.Icon.fas_map_pin, Fas.Icon.fas_map_pin
                )
            ).addItem(
                newItem(
                    titles[1], Fas.Icon.fas_calendar_check, Fas.Icon.fas_calendar_check
                )
            ).addItem(
                newItem(
                    titles[2], Fas.Icon.fas_map_marked_alt, Fas.Icon.fas_map_marked_alt
                )
            ).build()
            navigationController.setUpWithViewPager2(viewPager2 = viewPager2)
        }
        initTab(binding.vp)
    }


    override fun initEvents() {
        receiveEvent<MapEvent> {
            binding.vp.setCurrentItem(2, false)
        }
    }

}