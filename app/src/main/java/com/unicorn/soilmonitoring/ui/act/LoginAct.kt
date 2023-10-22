package com.unicorn.soilmonitoring.ui.act

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.tbruyelle.rxpermissions3.RxPermissions
import com.unicorn.soilmonitoring.R
import com.unicorn.soilmonitoring.databinding.ActLoginBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.activities.start
import splitties.resources.color

class LoginAct : BaseAct<ActLoginBinding>() {

    override fun initViews() {
        // 状态栏透明
        immersive(darkMode = true)
        binding.root.statusPadding()

        // 按钮点击效果
        binding.apply {
            btnLogin.helper.backgroundColorPressed = ColorUtils.blendARGB(
                color(R.color.primary), Color.WHITE, 0.3f
            )
        }
    }

    override fun initIntents() {
        fun requestPermissions() {
            RxPermissions(this).request(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA,
            ).subscribe { granted ->
                if (!granted) finish()
            }
        }
        requestPermissions()

        binding.apply {
            btnLogin.setOnClickListener {
                val username = etUsername.text.toString().trim()
                if (username == "") {
                    start<MainAct1> { }
                } else {
                    // 2 代表 pm
                    start<MainAct2> { }
                }
            }
        }
    }

}