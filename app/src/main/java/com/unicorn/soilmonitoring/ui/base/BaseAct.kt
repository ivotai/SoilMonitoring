package com.unicorn.soilmonitoring.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ActivityBinding
import com.dylanc.viewbinding.base.ActivityBindingDelegate


abstract class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity(),
    ActivityBinding<VB> by ActivityBindingDelegate() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithBinding()
    }

}


open class BaseAct<VB : ViewBinding> : BaseBindingActivity<VB>(), Ui {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    open fun init() {
        initViews()
        initIntents()
        initEvents()
    }

    override fun initViews() {
    }

    override fun initIntents() {
    }

    override fun initEvents() {
    }

}