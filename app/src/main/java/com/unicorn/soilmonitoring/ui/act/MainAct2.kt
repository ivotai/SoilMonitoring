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
import com.unicorn.soilmonitoring.databinding.ActMain1Binding
import com.unicorn.soilmonitoring.event.MapEvent
import com.unicorn.soilmonitoring.ui.Fas
import com.unicorn.soilmonitoring.ui.base.BaseAct
import com.unicorn.soilmonitoring.ui.fra.CollectListFra
import com.unicorn.soilmonitoring.ui.fra.CollectListFra2
import com.unicorn.soilmonitoring.ui.fra.CollectTaskFra
import com.unicorn.soilmonitoring.ui.fra.FakePointAllFra
import me.majiajie.pagerbottomtabstrip.item.NormalItemView
import splitties.resources.color

// 采样人员
class MainAct2 : BaseAct<ActMain1Binding>() {

    override fun initViews() {
        immersive()

        val titles = listOf("采样总览", "采样记录")

        binding.run {
            object : FragmentStateAdapter(this@MainAct2) {

                override fun getItemCount(): Int = titles.size

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> FakePointAllFra()
                        else -> CollectListFra2()
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
                    val checkedColor = color(splitties.material.colors.R.color.green_400)

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
            val navigationController = binding.tab.custom()
                .addItem(
                    newItem(
                        titles[0], Fas.Icon.fas_calendar_day, Fas.Icon.fas_calendar_day
                    )
                )
                .addItem(
                    newItem(
                        titles[1], Fas.Icon.fas_calendar_check, Fas.Icon.fas_calendar_check
                    )
                ).build()
            navigationController.setUpWithViewPager2(viewPager2 = viewPager2)
        }
        initTab(binding.vp)
    }

}