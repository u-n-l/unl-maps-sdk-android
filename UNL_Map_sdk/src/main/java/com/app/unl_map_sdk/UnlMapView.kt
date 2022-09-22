package com.app.unl_map_sdk

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.module.http.HttpRequestUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class UnlMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : MapView(context, attrs) {
    var mapbox: MapboxMap? = null

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        HttpRequestUtil.setOkHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(SigV4Interceptor())
                .addInterceptor(interceptor)
                .build()
        )
        this.getMapAsync {
            mapbox = it
            var poly = ArrayList<LatLng>()
            var map = it
            poly.add(LatLng(34.126256, 74.832149))
            poly.add(LatLng(34.126505, 74.835068))
            poly.add(LatLng(34.122597, 74.836956))
            poly.add(LatLng(34.123307, 74.832536))
            it?.addPolygon(PolygonOptions()
                .addAll(poly)
                .fillColor(Color.parseColor("#202020")))
            it.setStyle(Style.Builder()
                .fromUri("https://alpha.studio.unl.global/map_styles_base.json")) {
                map.uiSettings.setAttributionMargins(15, 0, 0, 15)
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(34.126256, 74.832149))
                    .zoom(12.0)
                    .build()
            }
        }
    }


}