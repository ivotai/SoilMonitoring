package com.unicorn.soilmonitoring.ui

import com.tbruyelle.rxpermissions3.RxPermissions
import com.unicorn.soilmonitoring.databinding.ActLoginBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.activities.start
import splitties.views.onClick

class LoginAct : BaseAct<ActLoginBinding>() {

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
            start<MainAct> { }
            finish()
        }
//        start<MainAct> { }
//        finish()
    }

}