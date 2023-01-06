package com.unl.map.sdk.data

import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.unl.map.sdk.UnlMap
import com.unl.map.sdk.data.ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_5

object Constants {

    /**
     * [BASE_URL] is the base url for [UnlMap] Style
     */
    private const val BASE_URL = "https://platform.unl.global/"
    const val TERRAIN = "${BASE_URL}map_styles_terrain.json"
    const val BASE = "${BASE_URL}map_styles_base.json"
    const val TRAFFIC = "${BASE_URL}map_styles_traffic.json"
    const val SATELLITE = "${BASE_URL}map_styles_satellite.json"
    const val VECTORIAL = "${BASE_URL}map_styles_vectorial.json"
}

/**
 * [getFormattedCellDimensions] method is used to get Dimensions of a cell in *GridView_ according to selected [CellPrecision].
 *
 * @param cellPrecision is selected [CellPrecision].
 * @return [String]
 */
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

/**
 * [getMinGridZoom] is used to get Minimum [ZoomLevel] to show *GridView_ according to selected [CellPrecision].
 *
 * @param cellPrecision is selected [CellPrecision].
 * @return [ZoomLevel]
 */
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

/**
 * [getZoomLevels] is method to get Values of [ZoomLevel].
 *
 * @return [HashMap]<[ZoomLevel],[Int]>.
 */
fun getZoomLevels(): HashMap<ZoomLevel, Int> {
    val zooms = HashMap<ZoomLevel,Int>()
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_10] = 20
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_9] = 18
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_8] = 16
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_7] = 14
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_6] = 12
    zooms[MIN_GRID_ZOOM_GEOHASH_LENGTH_5] = 10
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_4] = 8
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_3] = 4
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_2] = 3
    zooms[ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_1] = 2
    return zooms
}

/**
 * [getCellPrecisions] is method to get Values of [CellPrecision].
 *
 * @return [HashMap]<[CellPrecision],[Int]>.
 */
fun getCellPrecisions():  HashMap<CellPrecision,Int> {
    val precisions = HashMap<CellPrecision,Int>()
    precisions[CellPrecision.GEOHASH_LENGTH_10] = 10
    precisions[CellPrecision.GEOHASH_LENGTH_9] = 9
    precisions[CellPrecision.GEOHASH_LENGTH_8] = 8
    precisions[CellPrecision.GEOHASH_LENGTH_7] = 7
    precisions[CellPrecision.GEOHASH_LENGTH_6] = 6
    precisions[CellPrecision.GEOHASH_LENGTH_5] = 5
    precisions[CellPrecision.GEOHASH_LENGTH_4] = 4
    precisions[CellPrecision.GEOHASH_LENGTH_3] = 3
    precisions[CellPrecision.GEOHASH_LENGTH_2] = 2
    precisions[CellPrecision.GEOHASH_LENGTH_1] = 1
    return precisions
}


/**
 * [ZoomLevel] is an [Enum] for Min Zoom Level for GridView visibility.
 *
 * @constructor Create empty Zoom level
 */
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

/**
 * [CellPrecision] is the [Enum] for Precisions
 *
 * @constructor Create empty Cell precision
 */
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

/**
 * [SourceIDs] is an [Enum] for [GeoJsonSource] Ids
 *
 * @constructor Create empty Source ids
 */
enum class SourceIDs {
    GRID_SOURCE_ID,CELL_SOURCE_ID
}
/**
 * [LayerIDs] is an [Enum] for [Layer] Ids.
 *
 * @constructor Create empty Source ids.
 */
enum class LayerIDs {
    GRID_LAYER_ID,CELL_LAYER_ID,CELL_POP_LAYER_ID
}

/**
 * [GRID_ERROR]  is for Logging the Errors of Grid Controls.
 */
const val GRID_ERROR="GRID_ERROR"
/**
 * [TILE_ERROR]  is for Logging the Errors of Tile Controls.
 */
const val TILE_ERROR="TILE_ERROR"
/**
 * [CELL_ERROR]  is for Logging the Errors of Cell Selector.
 */
const val CELL_ERROR="CELL_ERROR"

/**
 * These Constants  is for SigV4Interceptor Property name.
 */
const val API_KEY="x-unl-api-key"
const val VPM_ID="x-unl-vpm-id"

/**
 * These Constants  is for [GeoJsonSource] Property name.
 */
const val GRID_PROP_NAME="Grid Lines"
const val LINE_STRING="LineString"
const val FEATURE="FEATURE"
const val VISIBILITY="visibility"
const val DEFAULT_GRID_LINE_WIDTH=1f
const val DEFAULT_ZOOM_LEVEL=14.0
const val MAX_ZOOM_LEVEL=20.0
const val MIN_ZOOM_LEVEL=2.0
const val LOCATION_POP_SIZE=1.3f
const val LOCATION_POP_TEXT_SIZE=40F
const val LOCATION_POP_MARGIN=0.000030


