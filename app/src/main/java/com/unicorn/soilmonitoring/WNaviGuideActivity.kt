package com.unicorn.soilmonitoring

import android.app.Activity
import android.os.Bundle
import android.view.View

import com.baidu.mapapi.walknavi.WalkNavigateHelper




class WNaviGuideActivity:Activity() {

    private lateinit var mNaviHelper: WalkNavigateHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取WalkNavigateHelper实例
        //获取WalkNavigateHelper实例
        mNaviHelper = WalkNavigateHelper.getInstance()
//获取诱导页面地图展示View
//获取诱导页面地图展示View
        val view: View = mNaviHelper.onCreate(this@WNaviGuideActivity)
        if (view != null) {
            setContentView(view)
        }
        mNaviHelper.startWalkNavi(this@WNaviGuideActivity)
    }

    override fun onResume() {
        super.onResume()
        mNaviHelper.resume()
    }

    override fun onPause() {
        super.onPause()
        mNaviHelper.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mNaviHelper.quit()
    }
}