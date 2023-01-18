package com.unl.map.sdk.networks


import android.util.Log
import androidx.lifecycle.ViewModel
import com.unl.map.sdk.data.API_ERROR
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UnlViewModel : ViewModel() {

    val indoorMapData = SingleLiveEvent<Any>()
    
    fun getIndoorMapData(vpmId:String) {
        RetrofitClient.unlMapApi.getIndoorMapData(vpmId)
            .enqueue(object : Callback<Any> {
                override fun onFailure(
                    call: Call<Any>,
                    t: Throwable?,
                ) {
                    Log.e(API_ERROR,t?.message?:"")
                }

                override fun onResponse(
                    call: Call<Any>,
                    response: Response<Any>,
                ) {
                    if (response.isSuccessful) {
                        indoorMapData.value = response.body()
                    }else{
                        Log.e(API_ERROR,response.code().toString())
                    }
                }
            })
    }

}