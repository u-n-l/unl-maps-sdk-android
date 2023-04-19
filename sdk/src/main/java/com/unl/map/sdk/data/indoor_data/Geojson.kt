package com.unl.map.sdk.data.indoor_data

data class Geojson(
    val feature_type: String,
    val geometry: Geometry,
    val id: String,
    val properties: Properties,
    val type: String
)