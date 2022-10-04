package com.app.unl_map_sdk


import okhttp3.Interceptor
import okhttp3.Response

class SigV4Interceptor(var api_key: String, var vpm_id: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .header("x-unl-api-key", api_key)
            .header("x-unl-vpm-id", vpm_id)
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }

}