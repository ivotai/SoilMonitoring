package com.unicorn.soilmonitoring

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener
import com.baidu.mapapi.search.poi.PoiCitySearchOption
import com.baidu.mapapi.search.poi.PoiDetailResult
import com.baidu.mapapi.search.poi.PoiDetailSearchResult
import com.baidu.mapapi.search.poi.PoiIndoorResult
import com.baidu.mapapi.search.poi.PoiResult
import com.baidu.mapapi.search.poi.PoiSearch
import com.baidu.mapapi.search.weather.WeatherDataType
import com.baidu.mapapi.search.weather.WeatherSearch
import com.baidu.mapapi.search.weather.WeatherSearchOption
import com.unicorn.soilmonitoring.databinding.ActivityMainBinding
import com.unicorn.soilmonitoring.ui.base.BaseAct


class MainActivity : BaseAct<ActivityMainBinding>() {

    override fun initViews() {
        binding.tvSearch.setOnClickListener {
            search()
        }
    }

    private fun search() {

        val districtID = "110105" // 天安门区域ID

        val weatherSearchOption = WeatherSearchOption()
            .weatherDataType(WeatherDataType.WEATHER_DATA_TYPE_ALL)
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

    private fun search2() {
        val p = PoiSearch.newInstance();
        val listener: OnGetPoiSearchResultListener = object : OnGetPoiSearchResultListener {
            override fun onGetPoiResult(poiResult: PoiResult) {
                poiResult
            }

            override fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult) {
                poiDetailSearchResult
            }

            override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult) {

            }

            //废弃
            override fun onGetPoiDetailResult(poiDetailResult: PoiDetailResult) {}
        }
        p.setOnGetPoiSearchResultListener(listener)
        p.searchInCity(
            PoiCitySearchOption()
                .city("北京") //必填
                .keyword("美食") //必填
                .pageNum(0)
        )
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