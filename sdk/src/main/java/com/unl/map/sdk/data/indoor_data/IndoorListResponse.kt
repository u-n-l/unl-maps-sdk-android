package com.unl.map.sdk.data.indoor_data

data class IndoorListResponse(
    val hasMore: Boolean,
    val items: List<Item>,
    val limit: Int
)