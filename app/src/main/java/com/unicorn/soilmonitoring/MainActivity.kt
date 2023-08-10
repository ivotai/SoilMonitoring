package com.unicorn.soilmonitoring

import android.graphics.Bitmap
import android.graphics.Color
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.OverlayOptions
import com.baidu.mapapi.map.TitleOptions
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.baidu.mapapi.search.weather.WeatherDataType
import com.baidu.mapapi.search.weather.WeatherSearch
import com.baidu.mapapi.search.weather.WeatherSearchOption
import com.drake.channel.receiveEvent
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.color
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.colorRes
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

            showPointDialog()
        }
        animateMapStatus(DataHelper.getParents()[0].sublist[0])

//        search3()

        // 延迟添加，解决标签在缩放后才显示
        Observable.timer(500, TimeUnit.MILLISECONDS).subscribe {
            addMarkers()
        }
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
        val color = if (point.pointStatus == PointStatus.TAKEN) splitties.material.colors.R.color.red_600 else splitties.material.colors.R.color.green_600
        val color1 = color(color)
        val titleOptions =
            TitleOptions().text(point.description)
                .titleFontColor(Color.WHITE)
                .titleBgColor(color1).titleFontSize(48)


    val iconicsDrawable=    IconicsDrawable(this, GoogleMaterial.Icon.gmd_location_pin).apply {
            colorInt = color1
            sizeDp = 24
        }

val bitmap = iconicsDrawable.toBitmap()
        val option: OverlayOptions =
            MarkerOptions().position(point.latLng).titleOptions(titleOptions).icon(BitmapDescriptorFactory.fromBitmap(bitmap))
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

