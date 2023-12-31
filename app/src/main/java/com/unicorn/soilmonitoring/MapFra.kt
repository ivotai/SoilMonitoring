package com.unicorn.soilmonitoring

import android.content.Intent
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.navi.BaiduMapNavigation
import com.baidu.mapapi.navi.NaviParaOption
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.baidu.mapapi.utils.DistanceUtil
import com.baidu.mapapi.walknavi.WalkNavigateHelper
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo
import com.blankj.utilcode.util.ToastUtils
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.unicorn.soilmonitoring.app.Config
import com.unicorn.soilmonitoring.app.MarkerHelper
import com.unicorn.soilmonitoring.app.Point
import com.unicorn.soilmonitoring.app.PointStatus
import com.unicorn.soilmonitoring.app.toast
import com.unicorn.soilmonitoring.databinding.FraMapBinding
import com.unicorn.soilmonitoring.event.MapEvent
import com.unicorn.soilmonitoring.event.NavigationEvent
import com.unicorn.soilmonitoring.ui.act.WNaviGuideActivity
import com.unicorn.soilmonitoring.ui.base.BaseFra


class MapFra : BaseFra<FraMapBinding>() {


    override fun initViews() {

        fun initMap() {
            map.run {
                // 开启地图的定位图层
                isMyLocationEnabled = true
                // 设置默认 zoom
                setMapStatus(
                    MapStatusUpdateFactory.newMapStatus(
                        MapStatus.Builder().zoom(Config.defaultZoom).build()
                    )
                )
            }
        }
        initMap()

        initLocationClient()

        map.setOnMarkerClickListener( {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false
            val key = it.extraInfo.getString("key")
            ToastUtils.showLong(key)
            false
        })

    }

    private var locationClient: LocationClient? = null

    private fun initLocationClient() {
        locationClient = LocationClient(requireContext()).apply {
            locOption = LocationClientOption().apply {
                // 百度经纬度坐标
                setCoorType("bd09ll");
                // 设置发起定位请求的间隔，int类型，单位ms
                setScanSpan(Config.locationScanSpan)
                // 是否需要地址信息
                setIsNeedAddress(true);
            }
            registerLocationListener(object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation) {

                    // todo district to districtId
//                    location.district

                    // self point
                    Config.selfPoint = Point(
                        "本人", LatLng(location.latitude, location.longitude), PointStatus.UN_TAKEN
                    )

                    // setMyLocationData
                    map.setMyLocationData(
                        MyLocationData.Builder().accuracy(location.radius)
                            .direction(location.direction).latitude(location.latitude)
                            .longitude(location.longitude).build()
                    )

                    // sug
                    if (t) return
                    map.setMapStatus(
                        MapStatusUpdateFactory.newMapStatus(
                            MapStatus.Builder()
                                .target(LatLng(location.latitude, location.longitude))
                                .zoom(Config.defaultZoom).build()
                        )
                    )
                    location.addrStr?.let { sug(it) }
                    t = true
                }
            })
            start()
        }
    }

    private var t = false
    private fun sug(keyword: String) {
        SuggestionSearch.newInstance().run {
            setOnGetSuggestionResultListener { suggestionResult ->
                Config.points.clear()
                suggestionResult.allSuggestions?.filter { it.pt != null }
                    ?.forEach { suggestionInfo ->
                        val point = Point(
                            suggestionInfo.key,
                            suggestionInfo.pt,
                            listOf(PointStatus.TAKEN, PointStatus.UN_TAKEN).random(),
                            distance = DistanceUtil.getDistance(
                                Config.selfPoint.latLng,
                                suggestionInfo.pt
                            )
                        )
                        Config.points.add(point)
                        MarkerHelper.addOverlay(
                            requireContext(),
                            point,
                            if (point.pointStatus == PointStatus.TAKEN)
                                splitties.material.colors.R.color.green_300
                            else
                                splitties.material.colors.R.color.grey_400,
                            GoogleMaterial.Icon.gmd_location_pin,
                            binding.mapView.map
                        )
                    }
                Config.points.sortWith(compareBy({ it.pointStatus }, { it.distance }))
                sendEvent(Config.points)
            }
            requestSuggestion(
                SuggestionSearchOption().city(Config.city).keyword(keyword)
            )
        }
    }


    private fun initNaviEngine(point: Point) {
        WalkNavigateHelper.getInstance()
            .initNaviEngine(requireActivity(), object : IWEngineInitListener {
                override fun engineInitSuccess() {
                    routeWalkPlanWithParam(point)
                    //引擎初始化成功的回调

                }

                override fun engineInitFail() {
                    WalkNavigateHelper.getInstance().unInitNaviEngine()
                    //引擎初始化失败的回调
                }
            })
    }


    private fun routeWalkPlanWithParam(point: Point) {
        val params = WalkNaviLaunchParam().apply {
            startNodeInfo(WalkRouteNodeInfo().apply {
                location = Config.selfPoint.latLng
            })
            endNodeInfo(WalkRouteNodeInfo().apply {
                location = point.latLng
            })
        }
        WalkNavigateHelper.getInstance()
            .routePlanWithRouteNode(params, object : IWRoutePlanListener {
                override fun onRoutePlanStart() {
                    //开始算路的回调
                }

                override fun onRoutePlanSuccess() {
                    //跳转至诱导页面
                    val intent = Intent(requireContext(), WNaviGuideActivity::class.java)
                    startActivity(intent)
                }

                override fun onRoutePlanFail(walkRoutePlanError: WalkRoutePlanError) {
                    "算路失败".toast()
                }
            })
    }


    private fun openBaiduMapWalkNavi(point: Point) {
        val startPoint = Config.selfPoint.latLng
        val para: NaviParaOption =
            NaviParaOption().startPoint(startPoint).endPoint(point.latLng).endName("三号采集点")

        try {
            val result = BaiduMapNavigation.openBaiduMapWalkNavi(para, requireContext())
            if (!result) {
                ToastUtils.showLong("未安装百度地图，启用内置导航")
                initNaviEngine(point)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        BaiduMapNavigation.finish(requireContext());
    }

    override fun initEvents() {
        receiveEvent<NavigationEvent> {
            openBaiduMapWalkNavi(it.point)
        }
        receiveEvent<MapEvent> {
            map.setMapStatus(
                MapStatusUpdateFactory.newMapStatus(
                    MapStatus.Builder().target(it.point.latLng).build()
                )
            )
        }
    }


    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onDestroy() {
        locationClient?.stop();
        map.isMyLocationEnabled = false;
        binding.mapView.onDestroy();
        super.onDestroy()
    }

    private val map get() = binding.mapView.map

}

