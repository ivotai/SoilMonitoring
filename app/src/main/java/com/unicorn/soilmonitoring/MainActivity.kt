package com.unicorn.soilmonitoring

//import com.baidu.mapapi.map.TitleOptions

import android.Manifest
import android.content.Intent
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.map.Overlay
import com.baidu.mapapi.map.OverlayOptions
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException
import com.baidu.mapapi.navi.BaiduMapNavigation
import com.baidu.mapapi.navi.NaviParaOption
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay
import com.baidu.mapapi.search.route.BikingRouteResult
import com.baidu.mapapi.search.route.DrivingRouteResult
import com.baidu.mapapi.search.route.IndoorRouteResult
import com.baidu.mapapi.search.route.MassTransitRouteResult
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener
import com.baidu.mapapi.search.route.PlanNode
import com.baidu.mapapi.search.route.RoutePlanSearch
import com.baidu.mapapi.search.route.TransitRouteResult
import com.baidu.mapapi.search.route.WalkingRoutePlanOption
import com.baidu.mapapi.search.route.WalkingRouteResult
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.baidu.mapapi.search.weather.WeatherDataType
import com.baidu.mapapi.search.weather.WeatherSearch
import com.baidu.mapapi.search.weather.WeatherSearchOption
import com.baidu.mapapi.walknavi.WalkNavigateHelper
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo
import com.drake.channel.receiveEvent
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import com.tbruyelle.rxpermissions3.RxPermissions
import com.unicorn.soilmonitoring.databinding.ActivityMainBinding
import com.unicorn.soilmonitoring.ui.app.Config
import com.unicorn.soilmonitoring.ui.app.DataHelper
import com.unicorn.soilmonitoring.ui.app.MarkerHelper
import com.unicorn.soilmonitoring.ui.app.Point
import com.unicorn.soilmonitoring.ui.app.PointStatus
import com.unicorn.soilmonitoring.ui.base.BaseAct
import com.unicorn.soilmonitoring.ui.view.PointRecyclerView
import splitties.resources.color


class MainActivity : BaseAct<ActivityMainBinding>() {


    override fun initViews() {
        fun requestPermissions() {
            RxPermissions(this).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ).subscribe { granted ->
                if (!granted) finish()
            }
        }
        requestPermissions()

        fun initMap() {
            map.run {
                // 开启地图的定位图层
                isMyLocationEnabled = true
                // 定位跟随态
//                setMyLocationConfiguration(
//                    MyLocationConfiguration(
//                        MyLocationConfiguration.LocationMode.FOLLOWING, true, null
//                    )
//                )
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

        binding.tvPoint.setOnClickListener { showPointDialog() }
    }

    private var locationClient: LocationClient? = null

    private fun initLocationClient() {

        locationClient = LocationClient(applicationContext).apply {
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
                    location.district

                    // setMyLocationData
                    map.setMyLocationData(
                        MyLocationData.Builder()
                            .accuracy(location.radius)
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
                    sug(location.addrStr)
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
                suggestionResult.allSuggestions?.filter { it.pt != null }
                    ?.forEach { suggestionInfo ->
                        val point = Point(
                            suggestionInfo.key, suggestionInfo.pt, PointStatus.UN_TAKEN
                        )
                        Config.points.add(point)
                        MarkerHelper.addOverlay(
                            this@MainActivity,
                            point,
                            splitties.material.colors.R.color.red_300,
                            GoogleMaterial.Icon.gmd_location_pin,
                            binding.mapView.map
                        )
                    }
            }
            requestSuggestion(
                SuggestionSearchOption().city("上海").keyword(keyword)
            )
        }
    }


    private fun s2(latLng: LatLng) {
        WalkNavigateHelper.getInstance().initNaviEngine(this, object : IWEngineInitListener {
            override fun engineInitSuccess() {
                s3(latLng)
                //引擎初始化成功的回调

            }

            override fun engineInitFail() {
                //引擎初始化失败的回调
            }
        })

    }


    private fun s3(latLng: LatLng) {
        val p = Point("", latLng)
        drawerMarker(p)
        val params = WalkNaviLaunchParam().apply {
            startNodeInfo(WalkRouteNodeInfo().apply {
                location = latLng
            })
            endNodeInfo(WalkRouteNodeInfo().apply {
                location = LatLng(31.269909, 121.491697)
            })
        }
        WalkNavigateHelper.getInstance()
            .routePlanWithRouteNode(params, object : IWRoutePlanListener {
                override fun onRoutePlanStart() {
                    //开始算路的回调
                }

                override fun onRoutePlanSuccess() {
                    //算路成功
                    //跳转至诱导页面
                    val intent = Intent(this@MainActivity, WNaviGuideActivity::class.java)
                    startActivity(intent)
                }

                override fun onRoutePlanFail(walkRoutePlanError: WalkRoutePlanError) {
                    ""
                    //算路失败的回调
                }
            })
    }


    private fun s4(latLng: LatLng) {
        val startPoint = latLng
        val endPoint = LatLng(31.269609, 121.491697)

//构建导航参数

//构建导航参数
        val para: NaviParaOption =
            NaviParaOption().startPoint(startPoint).endPoint(endPoint).startName("天安门")
                .endName("百度大厦")
//调起百度地图
//调起百度地图
        try {
            BaiduMapNavigation.openBaiduMapWalkNavi(para, this)
        } catch (e: BaiduMapAppNotSupportNaviException) {
            e.printStackTrace()
            //调起失败的处理
        }
    }

    private fun s() {
        val mSearch = RoutePlanSearch.newInstance()

        val l = object : OnGetRoutePlanResultListener {
            override fun onGetWalkingRouteResult(p0: WalkingRouteResult) {
                //创建WalkingRouteOverlay实例
                //创建WalkingRouteOverlay实例
                val overlay = WalkingRouteOverlay(binding.mapView.map)
                if (p0.getRouteLines().size > 0) {
                    //获取路径规划数据,(以返回的第一条数据为例)
                    //为WalkingRouteOverlay实例设置路径数据
                    overlay.setData(p0.getRouteLines().get(0))
                    //在地图上绘制WalkingRouteOverlay
                    overlay.addToMap()
                }
            }

            override fun onGetTransitRouteResult(p0: TransitRouteResult?) {
            }

            override fun onGetMassTransitRouteResult(p0: MassTransitRouteResult?) {
            }

            override fun onGetDrivingRouteResult(p0: DrivingRouteResult?) {
            }

            override fun onGetIndoorRouteResult(p0: IndoorRouteResult?) {
            }

            override fun onGetBikingRouteResult(p0: BikingRouteResult?) {
            }
        }
        mSearch.setOnGetRoutePlanResultListener(l)


        mSearch.walkingSearch(WalkingRoutePlanOption().apply {
            from(PlanNode.withLocation(DataHelper.getParents()[0].sublist[0].latLng))
            to(PlanNode.withLocation(DataHelper.getParents()[0].sublist[2].latLng))
        })

    }


    private val pointDialog by lazy {
        MaterialDialog(this, BottomSheet()).apply {
//            title(text = "采样点总览")
            customView(view = PointRecyclerView(this@MainActivity), scrollable = true)
        }
    }

    private fun showPointDialog() {
        pointDialog.show()
    }

    private fun search() {

        val districtID = "110105" // 天安门区域ID

        val weatherSearchOption =
            WeatherSearchOption().weatherDataType(WeatherDataType.WEATHER_DATA_TYPE_ALL)
                .districtID(districtID)


        val mWeatherSearch = WeatherSearch.newInstance()
        mWeatherSearch.setWeatherSearchResultListener {
            it
            it.realTimeWeather
            val realTimeWeather = it.realTimeWeather
            realTimeWeather.temperature

        }

        mWeatherSearch.request(weatherSearchOption);
    }


    override fun initEvents() {
        receiveEvent<Point> {
            pointDialog.dismiss()
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(MapStatus.Builder().target(it.latLng).build()))
        }
    }


    private fun addMarkers() {

        DataHelper.getParents().forEach { parent -> parent.sublist.forEach { drawerMarker(it) } }
    }

    private var marker: Overlay? = null

    private fun drawerMarker(point: Point) {
        val color =
            if (point.pointStatus == PointStatus.TAKEN) splitties.material.colors.R.color.green_600 else splitties.material.colors.R.color.red_600
        val color1 = color(color)
//        val titleOptions =
//            TitleOptions().text(point.description)
//                .titleFontColor(Color.WHITE)
//                .titleBgColor(color1)


        val iconicsDrawable = IconicsDrawable(this, GoogleMaterial.Icon.gmd_location_pin).apply {
            colorInt = color1
            sizeDp = 24
        }

        val bitmap = iconicsDrawable.toBitmap()
        val option: OverlayOptions =
            MarkerOptions().position(point.latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        binding.mapView.map.addOverlay(option)
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

    val map get() = binding.mapView.map

}

