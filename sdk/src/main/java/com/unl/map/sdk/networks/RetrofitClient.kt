package com.unl.map.sdk.networks


 import com.google.gson.GsonBuilder
 import okhttp3.Interceptor
 import okhttp3.OkHttpClient
 import okhttp3.logging.HttpLoggingInterceptor
 import retrofit2.Retrofit
 import retrofit2.converter.gson.GsonConverterFactory
 import java.util.concurrent.TimeUnit


object RetrofitClient {
    var BASE_URL = "https://api.unl.global/v1/"  //version 1


    val unlMapApi: UnlMapApi by lazy {
        RETROFIT.create(UnlMapApi::class.java)
    }

    private val LOGGING_INTERCEPTOR by lazy {
        HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
        )
    }

    private val NETWORK_INTERCEPTOR by lazy {
        Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
    }

    private val OK_HTTP_CLIENT by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(LOGGING_INTERCEPTOR)
            .addNetworkInterceptor(NETWORK_INTERCEPTOR)
            .build()
    }

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val RETROFIT by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OK_HTTP_CLIENT)
            .build()
    }
}