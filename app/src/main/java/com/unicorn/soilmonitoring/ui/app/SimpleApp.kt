package com.unicorn.soilmonitoring.ui.app

import android.app.Application
import android.util.Log
import com.baidu.location.LocationClient
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.tts.WNTTSManager
import com.baidu.mapapi.tts.WNTTSManager.IOnTTSPlayStateChangedListener
import com.baidu.mapapi.tts.WNTTsInitConfig


class SimpleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //
        LocationClient.setAgreePrivacy(true);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.setAgreePrivacy(this, true)

        SDKInitializer.initialize(this);

        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);



//        val config = WNTTsInitConfig.Builder()
//            .context(applicationContext)
//            .appKey("6EYApg2o7Pg7rKh49QgX4uXA")
//            .secretKey("leyVf4f1N8gZte1yOY7DEFaWkOWlsSdY")
////            .authSn("sn")
//            .build()
//
//        WNTTSManager.getInstance().initTTS(config)
//        WNTTSManager.getInstance()
//            .setOnTTSStateChangedListener(object : IOnTTSPlayStateChangedListener {
//                override fun onPlayEnd(s: String) {
//                    ""
//                }
//
//                override fun onPlayError(errCode: Int, error: String) {
//                    ""
//                }
//
//                override fun onPlayStart() {
//
//                }
//            })
    }

}