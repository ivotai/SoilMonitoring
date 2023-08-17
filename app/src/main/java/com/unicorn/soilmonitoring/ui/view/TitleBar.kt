package com.unicorn.soilmonitoring.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ActivityUtils
import com.dylanc.viewbinding.inflate
import com.mikepenz.iconics.view.IconicsImageView
import com.mikepenz.iconics.view.IconicsTextView
import com.unicorn.soilmonitoring.databinding.LayoutTitleBarBinding


// 自定义 View 的四个属性
/*
    1. 代码里创建，一个参数
    2. xml里创建，两个参数
    3. 后面两个参数是主题里的默认值，不管
 */
class TitleBar(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    private val binding = inflate<LayoutTitleBarBinding>()

    init {
        binding.apply {
            civ1.setOnClickListener {
                ActivityUtils.getTopActivity().finish()
            }
        }
    }

    fun setTitle(title: String) {
        binding.apply {
            ctv.text = title
        }
    }

    fun setTitleColor(color: Int) {
        binding.apply {
            ctv.setTextColor(color)
        }
    }

    fun getCiv1(): IconicsTextView {
        return binding.civ1.apply {
        }
    }

    fun getCiv2(): IconicsTextView {
        return binding.civ2.apply {
        }
    }

}
