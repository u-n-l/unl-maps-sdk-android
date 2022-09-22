package com.app.unl_map_sdk


import okhttp3.Interceptor
import okhttp3.Response

class SigV4Interceptor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .header("x-unl-api-key", "TyWCPuvtqc4xAE8jGZX6IWKnPRpWKQWf")
            .header("x-unl-vpm-id", "b96e28f6-a262-42e9-a337-a54ea90090f8")
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }

}