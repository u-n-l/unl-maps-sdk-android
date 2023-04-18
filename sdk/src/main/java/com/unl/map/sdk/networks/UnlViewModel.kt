package com.unl.map.sdk.networks


import android.util.Log
import androidx.lifecycle.ViewModel
import com.unl.map.sdk.data.API_ERROR
import com.unl.map.sdk.data.indoor_data.IndoorMapList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UnlViewModel : ViewModel() {

    val indoorMapData = SingleLiveEvent<IndoorMapList>()
    val singleIndoorMapData = SingleLiveEvent<Any>()

    fun getIndoorMapData(vpmId:String) {
        RetrofitClient.unlMapApi.getIndoorMapData(vpmId)
            .enqueue(object : Callback<IndoorMapList> {
                override fun onFailure(
                    call: Call<IndoorMapList>,
                    t: Throwable?,
                ) {
                    Log.e(API_ERROR,t?.message?:"")
                }

                override fun onResponse(
                    call: Call<IndoorMapList>,
                    response: Response<IndoorMapList>,
                ) {
                    if (response.isSuccessful) {
                        indoorMapData.value = response.body()
                    }else{
                        Log.e(API_ERROR,response.code().toString())
                    }
                }
            })
    }
    fun getSingleIndoorMapData(vpmId:String,recordId:String) {
        RetrofitClient.unlMapApi.singleIndoorMapData(vpmId,recordId)
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
                        singleIndoorMapData.value = response.body()
                    }else{
                        Log.e(API_ERROR,response.code().toString())
                    }
                }
            })
    }

}