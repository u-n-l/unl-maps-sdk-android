package com.app.unl_map_sdk.data

import com.app.unl_map_sdk.data.ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_5

object Constants {
    val BASE_URL = "https://alpha.platform.unl.global/"
    val TERRAIN = "${BASE_URL}map_styles_terrain.json"
    val BASE = "${BASE_URL}map_styles_base.json"
    val TRAFFIC = "${BASE_URL}map_styles_traffic.json"
    val SATELLITE = "${BASE_URL}map_styles_satellite.json"
    val VECTORIAL = "${BASE_URL}map_styles_vectorial.json"
}

fun getFormattedCellDimensions(cellPrecision: CellPrecision): String {
    when (cellPrecision) {
        CellPrecision.GEOHASH_LENGTH_1 ->
            return "5,009.4km x 4,992.6km"
        CellPrecision.GEOHASH_LENGTH_2 ->
            return "1,252.3km x 624.1km"
        CellPrecision.GEOHASH_LENGTH_3 ->
            return "156.5km x 156km"
        CellPrecision.GEOHASH_LENGTH_4 ->
            return "39.1km x 19.5km"
        CellPrecision.GEOHASH_LENGTH_5 ->
            return "4.9km x 4.9km"
        CellPrecision.GEOHASH_LENGTH_6 ->
            return "1.2km x 609.4m"
        CellPrecision.GEOHASH_LENGTH_7 ->
            return "152.9m x 152.4m"
        CellPrecision.GEOHASH_LENGTH_8 ->
            return "38.2m x 19m"
        CellPrecision.GEOHASH_LENGTH_10 ->
            return "1.2m x 59.5cm"
        else -> return "4.8m x 4.8m"
    }
}

fun getMinGridZoom(cellPrecision: CellPrecision): ZoomLevel {
    when (cellPrecision) {
        CellPrecision.GEOHASH_LENGTH_10 ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_10
        CellPrecision.GEOHASH_LENGTH_8 ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_8
        CellPrecision.GEOHASH_LENGTH_7 ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_7
        CellPrecision.GEOHASH_LENGTH_6 ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_6
        CellPrecision.GEOHASH_LENGTH_5 ->
            return MIN_GRID_ZOOM_GEOHASH_LENGTH_5
        CellPrecision.GEOHASH_LENGTH_4 ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_4
        CellPrecision.GEOHASH_LENGTH_3 ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_3
        CellPrecision.GEOHASH_LENGTH_2 ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_2
        CellPrecision.GEOHASH_LENGTH_1 ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_1
        else ->
            return ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_9
    }
}

fun getZoomLevels(): ArrayList<Int> {
    var zooms = ArrayList<Int>()
    zooms.add(20)
    zooms.add(18)
    zooms.add(16)
    zooms.add(14)
    zooms.add(12)
    zooms.add(10)
    zooms.add(8)
    zooms.add(4)
    zooms.add(3)
    zooms.add(2)
    return zooms

}

fun getCellPrecisions(): ArrayList<Int> {
    var precisions = ArrayList<Int>()
    precisions.add(10)
    precisions.add(9)
    precisions.add(8)
    precisions.add(7)
    precisions.add(6)
    precisions.add(5)
    precisions.add(4)
    precisions.add(3)
    precisions.add(2)
    precisions.add(1)
    return precisions
}

enum class ZoomLevel {
    MIN_GRID_ZOOM_GEOHASH_LENGTH_10,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_9,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_8,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_7,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_6,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_5,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_4,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_3,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_2,
    MIN_GRID_ZOOM_GEOHASH_LENGTH_1
}

enum class CellPrecision {
    GEOHASH_LENGTH_10,
    GEOHASH_LENGTH_9,
    GEOHASH_LENGTH_8,
    GEOHASH_LENGTH_7,
    GEOHASH_LENGTH_6,
    GEOHASH_LENGTH_5,
    GEOHASH_LENGTH_4,
    GEOHASH_LENGTH_3,
    GEOHASH_LENGTH_2,
    GEOHASH_LENGTH_1
}