package com.app.unl_map_sdk

import android.content.Context
import com.mapbox.mapboxsdk.Mapbox

class UnlMap(context: Context) {
    init {
        Mapbox.getInstance(context)
    }
}