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

        this.getMapAsync {
            mapbox = it
            it.setStyle(Style.Builder()
                .fromUri("https://alpha.studio.unl.global/map_styles_base.json")) {
                mapbox?.uiSettings?.setAttributionMargins(15, 0, 0, 15)
                mapbox?.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(34.126256, 74.832149))
                    .zoom(12.0)
                    .build()
            }
        }
    }


}