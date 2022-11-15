package com.app.unl_map_sdk

import android.content.Context
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.module.http.HttpRequestUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class UnlMap(context: Context, api_key: String, vpm_id: String) {
        init {
        Mapbox.getInstance(context)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        HttpRequestUtil.setOkHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(SigV4Interceptor(api_key,vpm_id))
                .addInterceptor(interceptor)
                .build()
        )
    }
}