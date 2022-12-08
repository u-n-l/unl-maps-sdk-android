package com.app.unl_map_sdk.data

/**
 * [Geometry] is an KotlinData Class to store data Related to GeoJsonSource like coordinates and type of [Geometry].
 *
 * @property coordinates stores the [Array] of Coordinates.
 * @property type stores the GeoJsonSource Type as String.
 * @constructor Create empty Geometry
 */
data class Geometry(
    val coordinates: Array<DoubleArray>,
    val type: String
)