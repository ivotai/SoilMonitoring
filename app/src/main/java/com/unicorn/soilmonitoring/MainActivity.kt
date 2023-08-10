package com.unicorn.soilmonitoring

//import com.baidu.mapapi.map.TitleOptions

import android.content.Intent
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.OverlayOptions
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
import com.unicorn.soilmonitoring.databinding.ActivityMainBinding
import com.unicorn.soilmonitoring.ui.app.DataHelper
import com.unicorn.soilmonitoring.ui.app.Point
import com.unicorn.soilmonitoring.ui.app.PointStatus
import com.unicorn.soilmonitoring.ui.base.BaseAct
import com.unicorn.soilmonitoring.ui.view.PointRecyclerView
import io.reactivex.rxjava3.core.Observable
import splitties.resources.color
import java.util.concurrent.TimeUnit


class MainActivity : BaseAct<ActivityMainBinding>() {


    override fun initViews() {
        binding.tvPoint.setOnClickListener {
//            addMarkers()

//            s()

//            showPointDialog()
        }
        animateMapStatus(DataHelper.getParents()[0].sublist[0])

//        search3()

        // 延迟添加，解决标签在缩放后才显示
        Observable.timer(500, TimeUnit.MILLISECONDS).subscribe {
            addMarkers()
        }


// 获取导航控制类
// 引擎初始化
        // 获取导航控制类
// 引擎初始化
        WalkNavigateHelper.getInstance().initNaviEngine(this, object : IWEngineInitListener {
            override fun engineInitSuccess() {
                //引擎初始化成功的回调
                s3()
            }

            override fun engineInitFail() {
                //引擎初始化失败的回调
            }
        })



    }


    private fun s3(){
        val params = WalkNaviLaunchParam().apply {
            startNodeInfo(WalkRouteNodeInfo().apply {
                location=(DataHelper.getParents()[0].sublist[0].latLng)
            })
            endNodeInfo(WalkRouteNodeInfo().apply {
                location=(DataHelper.getParents()[0].sublist[2].latLng)
            })
        }
        WalkNavigateHelper.getInstance().routePlanWithRouteNode(params, object : IWRoutePlanListener {
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
                //算路失败的回调
            }
        })
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


        mSearch.walkingSearch(
            WalkingRoutePlanOption().apply {
                from(PlanNode.withLocation(DataHelper.getParents()[0].sublist[0].latLng))
                to(PlanNode.withLocation(DataHelper.getParents()[0].sublist[2].latLng))
            }
        )

    }


    private val pointDialog by lazy {
        MaterialDialog(this, BottomSheet()).apply {
            title(text = "采样点总览")
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


    private fun search3() {
        SuggestionSearch.newInstance().apply {
            setOnGetSuggestionResultListener {
                it
            }
            requestSuggestion(
                SuggestionSearchOption().city("上海").keyword("植物园")
            )
        }
    }

    override fun initEvents() {
        receiveEvent<Point> {
            pointDialog.dismiss()
            animateMapStatus(it)
        }
    }

    private fun animateMapStatus(point: Point) {
        val builder = MapStatus.Builder()
        builder.target(point.latLng).zoom(20.0f)
        binding.mapView.map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private fun addMarkers() {

        DataHelper.getParents().forEach { parent -> parent.sublist.forEach { addMarker(it) } }
    }

    private fun addMarker(point: Point) {
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
            MarkerOptions().position(point.latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
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
        super.onDestroy()
        binding.mapView.onDestroy()
    }

}

