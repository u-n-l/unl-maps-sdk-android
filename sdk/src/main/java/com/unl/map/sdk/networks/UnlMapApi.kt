package com.unl.map.sdk.networks

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UnlMapApi {
    companion object {

        //GET
        const val INDOOR_MAP_DATA = "projects/{projectId}/records"

    }

    @GET(INDOOR_MAP_DATA)
    fun getIndoorMapData(@Path("projectId") vpmId: String): Call<Any>

}