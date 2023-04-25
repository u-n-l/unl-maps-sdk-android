package com.unl.map.sdk

import android.content.Context
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.module.http.HttpRequestUtil
import com.unl.map.sdk.data.EnvironmentType
import com.unl.map.sdk.prefs.DataManager
import com.unl.map.sdk.prefs.PrefsManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Use of UnlMap requires a UnlMap API KEY access token and VPM ID. Obtain an access token on the UnlMap account Dashboard .
 *
 * Warning: Please note that you are responsible for getting permission to use the map data, and for ensuring your use adheres to the relevant terms of use.
 *
 * Unl map is Used to create Instance of MapBox and also used to set Header params in TileSelector Style
 *
 * @constructor
 *
 * @param context parameter used  for Mapbox Initialization
 * @param api_key parameter used to Authorize Map Credentials
 * @param vpm_id  parameter used to Authorize UNL Credentials
 */
class UnlMap(context: Context, api_key: String, vpm_id: String,envType : String) {
    init {
        Mapbox.getInstance(context)
        PrefsManager.initialize(context)
        DataManager.saveApiKey(api_key)
        DataManager.saveVpmId(vpm_id)
        DataManager.saveEnvironment(envType)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        HttpRequestUtil.setOkHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(SigV4Interceptor(api_key, vpm_id))
                .addInterceptor(interceptor)
                .build()
        )
    }
}