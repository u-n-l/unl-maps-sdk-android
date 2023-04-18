package com.unl.map.sdk.data.indoor_data

data class Item(
    val createdAt: String,
    val createdByUserId: String,
    val geohash: String,
    val geojson: Geojson,
    val identityType: String,
    val latitude: Double,
    val longitude: Double,
    val parentId: Any,
    val recordId: String,
    val updatedAt: String,
    val venueId: String
)