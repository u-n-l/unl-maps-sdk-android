package com.unl.map.sdk.data

/**
 * [MyFeature] is an [KotlinData] Class to store data Related to GeoJsonSource like [Geometry], Properties and Type..
 *
 * @property geometry is used to store [Geometry] Object.
 * @property properties is used to store Properties like color of Layer,Width of line etc.
 * @property type is used to store type of feature (Describes the type like Line or polygon etc)
 * @constructor Create empty My feature
 */
data class MyFeature(
    val geometry: Geometry,
    val properties: Props,
    val type: String
)