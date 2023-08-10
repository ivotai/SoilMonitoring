package com.unicorn.soilmonitoring

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.baidu.mapapi.walknavi.WalkNavigateHelper
import com.blankj.utilcode.util.ToastUtils


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
        setContentView(view)

        mNaviHelper.setTTsPlayer { s, b ->
            /**
             * 诱导文本回调
             * @param s 诱导文本
             * @param b 是否抢先播报
             * @return
             */
            ToastUtils.showShort(s)
            0
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