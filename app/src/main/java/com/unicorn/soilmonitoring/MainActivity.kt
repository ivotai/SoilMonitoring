package com.unicorn.soilmonitoring

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.OverlayOptions
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.baidu.mapapi.search.weather.WeatherDataType
import com.baidu.mapapi.search.weather.WeatherSearch
import com.baidu.mapapi.search.weather.WeatherSearchOption
import com.unicorn.soilmonitoring.databinding.ActivityMainBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct
import com.unicorn.soilmonitoring.ui.view.PointRecyclerView


class MainActivity : BaseAct<ActivityMainBinding>() {


    override fun initViews() {
        binding.tvPoint.setOnClickListener {
            MaterialDialog(this, BottomSheet()).show {
                title(text = "数据点总览")
                customView(view = PointRecyclerView(this@MainActivity), scrollable = true)
            }
        }
        s()

//        DataHelper.getPoints().forEach { showMarker(it) }
//
//        showMarker(Pair(31.153891, 121.450991))
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

    private fun s() {
        val point = LatLng(31.153891, 121.450991)
        val builder = MapStatus.Builder()
        builder.target(point).zoom(18.0f)
        binding.bmapView.map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }


    private fun showMarker(point: LatLng) {
        //定义Maker坐标点
        //定义Maker坐标点
//构建Marker图标
//构建Marker图标
        val bitmap =
            BitmapDescriptorFactory.fromResource(com.unicorn.soilmonitoring.R.mipmap.icon_mark1)
//构建MarkerOption，用于在地图上添加Marker
//构建MarkerOption，用于在地图上添加Marker
        val option: OverlayOptions = MarkerOptions().position(point).icon(bitmap)
        binding.bmapView.map.addOverlay(option)
//在地图上添加Marker，并显示
    }

    override fun onPause() {
        super.onPause()
        binding.bmapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.bmapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.bmapView.onDestroy()
    }

}

