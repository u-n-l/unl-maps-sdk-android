package com.unl.map.sdk.networks

import com.unl.map.sdk.data.indoor_data.IndoorMapList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UnlMapApi {
    companion object {

        //GET
        const val INDOOR_MAP_DATA = "projects/{projectId}/records"
        const val SINGLE_INDOOR_MAP_DATA = "projects/{projectId}/imdf/{imdfId}"
    }
    @GET(INDOOR_MAP_DATA)
    fun getIndoorMapData(@Path("projectId") vpmId: String): Call<IndoorMapList>

    @GET(SINGLE_INDOOR_MAP_DATA)
    fun singleIndoorMapData(@Path("projectId") vpmId: String,@Path("imdfId") recordId: String): Call<Any>

}