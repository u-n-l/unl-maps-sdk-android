package com.unl.map.sdk.networks


 import com.google.gson.GsonBuilder
 import com.unl.map.sdk.UnlMap
 import com.unl.map.sdk.data.API_KEY
 import com.unl.map.sdk.data.EnvironmentType
 import com.unl.map.sdk.prefs.DataManager
 import okhttp3.Interceptor
 import okhttp3.OkHttpClient
 import okhttp3.logging.HttpLoggingInterceptor
 import retrofit2.Retrofit
 import retrofit2.converter.gson.GsonConverterFactory
 import java.util.concurrent.TimeUnit


object RetrofitClient {

    var BASE_URL_PROD = "https://api.unl.global/v1/"
    var BASE_URL_SANDBOX = "https://sandbox.api.unl.global/v1/"


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
                .addHeader(API_KEY, DataManager.getApiKey()?:"")
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
     var  env = DataManager.getEnvironment()?:""
        if(env.equals(EnvironmentType.PROD))
        {
            Retrofit.Builder()
                .baseUrl(BASE_URL_PROD)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(OK_HTTP_CLIENT)
                .build()

        }else{

            Retrofit.Builder()
                .baseUrl(BASE_URL_SANDBOX)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(OK_HTTP_CLIENT)
                .build()
        }

    }
}