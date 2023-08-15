package com.unicorn.soilmonitoring.app

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.blankj.utilcode.util.ToastUtils
import me.majiajie.pagerbottomtabstrip.NavigationController


fun NavigationController.setUpWithViewPager2(viewPager2: ViewPager2) {

    addSimpleTabItemSelectedListener { index, _ -> viewPager2.setCurrentItem(index, false) }
    val n: Int = viewPager2.currentItem
    if (selected != n) {
        setSelect(n)
    }
    viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (selected != position) {
                setSelect(position)
            }
        }
    })
}

fun TextView.trimText() = text.toString().trim()

fun String?.toast() = this.let { ToastUtils.showShort(it) }

fun ViewPager2.removeEdgeEffect() {
    (this.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
}


//fun Long.toDisplayDateFormat(): String = DateTime(this).toString(displayDateFormat)