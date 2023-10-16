package com.unicorn.soilmonitoring.ui.act

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.unicorn.soilmonitoring.databinding.ActLoginBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.activities.start
import splitties.resources.color

class LoginAct : BaseAct<ActLoginBinding>() {

    override fun initViews() {
        binding.apply {
            btnLogin.helper.backgroundColorPressed = ColorUtils.blendARGB(
                color(splitties.material.colors.R.color.green_400), Color.WHITE, 0.3f
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

        binding.btnLogin.setOnClickListener {
            start<MainAct1> { }
//            finish()
        }
    }

}