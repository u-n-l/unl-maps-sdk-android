package com.unl.map.sdk.data.indoor_data

data class IndoorMapList(
    val hasMore: Boolean,
    val items: List<Item>,
    val limit: Int
)
