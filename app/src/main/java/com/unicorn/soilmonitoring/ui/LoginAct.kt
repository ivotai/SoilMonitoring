package com.unicorn.soilmonitoring.ui

import com.unicorn.soilmonitoring.MainActivity
import com.unicorn.soilmonitoring.databinding.ActLoginBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct
import splitties.activities.start

class LoginAct : BaseAct<ActLoginBinding>() {

    override fun initIntents() {
        start<MainActivity> { }
    }

}