package com.unl.map.sdk.helpers.grid_controls

import android.content.Context
import android.graphics.*
import android.util.Log
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyValue
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.unl.map.R
import com.unl.map.sdk.data.*
import com.unl.map.sdk.views.PrecisionDialog
import com.unl.map.sdk.views.UnlMapView
import kotlinx.coroutines.*
import unl.core.Bounds
import unl.core.UnlCore


/**
 * [loadGrids] is an Extension method which helps us to get Grid Lines from [UnlCore] Lib.
 *
 * To get the lines we need current [CellPrecision] and Maps current visible [Bounds].
 *
 * @param isVisibleGrids isVisibleGrids is used to check whether used has clicked Grid Button on Map or not.
 * @param unlMapView UnlMapView is used for instance of [UnlMapView].
 * @param cellPrecision CellPrecision is used to get GridLines from [UnlCore] Lib.
 */
@OptIn(DelicateCoroutinesApi::class)
fun MapboxMap?.loadGrids(
    isVisibleGrids: Boolean,
    unlMapView: UnlMapView,
    cellPrecision: CellPrecision,
) {

    /**
     * [latLngBounds] is used to store Map's current Visible [Bounds].
     */
    val latLngBounds = this?.projection?.visibleRegion?.latLngBounds

    /**
     * [mapBoundsWidth] is used to store the width of Visible portion of Map.
     */
    val mapBoundsWidth = latLngBounds?.northEast?.longitude!! - latLngBounds.southWest.longitude

    /**
     * [mapBoundsHeight] is used to store the height of Visible portion of Map.
     */
    val mapBoundsHeight = latLngBounds.northEast.latitude - latLngBounds.southWest.latitude

    /**
     * [bounds] is used to store the Map's Visible region with extra boundaries
     * Here we add 4 more Map bounds to current visible bounds as per height and width of the Map.
     */
    val bounds = Bounds(latLngBounds.latNorth + mapBoundsWidth,
        latLngBounds.lonEast + mapBoundsHeight,
        latLngBounds.latSouth - mapBoundsWidth,
        latLngBounds.lonWest - mapBoundsHeight)

    /**
     * [zoomLevel] is used to store Map's current zoom Level.
     */
    val zoomLevel = this?.cameraPosition?.zoom!!

    /**
     * [minZoom] is use to store Minimum Zoom Level to show GridLines.
     */
    val minZoom = getZoomLevels()[getMinGridZoom(cellPrecision)]
    this.getStyle { style ->

        /**
         * Here we have used a conditional constraint to check whether ZoomLevel of my Map is Greater or equal to Minimum Zoom to show Grid
         *
         * And [isVisibleGrids] is used whether a user clicked on Grid Button or not.
         */
        if (zoomLevel >= minZoom!! && isVisibleGrids) {
            style.getLayer(LayerIDs.GRID_LAYER_ID.name)
                ?.setProperties(PropertyValue(VISIBILITY, Property.VISIBLE))

            /**
             *[UnlCore.gridLines] is an static method in [UnlCore] Lib. and returns the all possible line coordinates shown to user according to selected
             * [CellPrecision] and Map [Bounds].
             */
            val lines = UnlCore.gridLines(bounds, getCellPrecisions()[cellPrecision]!!)
            /**
             *[GlobalScope.executeAsyncTask] helps us to convert GridLines to [GeoJsonSource] in background Task.
             */
            GlobalScope.executeAsyncTask(onPreExecute = {
                /**
                 * onPreExecute method will run before doInBackground method.
                 *
                 * And Also works on Main Thread.
                 */
            }, doInBackground = {
                /**
                 * doInBackground method will run before onPostExecute method.
                 *
                 * And Also works on Background Thread.
                 */
                val features = ArrayList<Feature>()
                /**
                 * [lines.distinctBy] method is used to iterate latLngs of lines
                 * And will be formatted in [GeoJsonSource] format.
                 */
                lines.distinctBy {
                    val props = Props(name = GRID_PROP_NAME)
                    val geometry = Geometry(type = LINE_STRING, coordinates = it)
                    val feature =
                        MyFeature(type = FEATURE, properties = props, geometry = geometry)
                    val dt = Gson().toJson(feature)
                    features.add(Feature.fromJson(dt))
                }
                val fc: FeatureCollection = FeatureCollection.fromFeatures(features)

                /**
                 * return the [FeatureCollection] Object.
                 */
                fc
            }, onPostExecute = {
                /**
                 * onPostExecute method will run after doInBackground method.
                 *
                 * And Also works on Main Thread.
                 */
                unlMapView.activity?.runOnUiThread {
                    unlMapView.mapbox.drawLines(it, unlMapView.context)
                }

            })
        } else {
            /**
             * This condition used to check if there is any layers on Map then set the visibility of the Layers None.
             */
            if (style.layers.size > 0) {
                style.getLayer(LayerIDs.GRID_LAYER_ID.name)
                    ?.setProperties(PropertyValue(VISIBILITY, Property.NONE))
                style.getLayer(LayerIDs.CELL_LAYER_ID.name)
                    ?.setProperties(PropertyValue(VISIBILITY, Property.NONE))

            }
        }
    }
}


/**
 * [drawLines] is an Extension method for [MapboxMap] and is used to draw the Grid Lines on [UnlMapView].
 *
 * @param featureCollection featureCollection contains data related to GridLines.
 * @param context context is used to get access of [colors.xml] file.
 */
fun MapboxMap?.drawLines(featureCollection: FeatureCollection, context: Context) {
    this?.getStyle { style ->
        /**
         *  Here the purpose of this condition is to check whether list of [Feature] is null or not.
         */
        if (featureCollection.features() != null) {
            /**
             *  Here the purpose of this condition is to check whether list of [Feature] is empty.
             */
            if (featureCollection.features()!!.isNotEmpty()) {

                /**
                 * Src IS [GeoJsonSource] Object for Grid Data.
                 */
                val src = style.getSource(SourceIDs.GRID_SOURCE_ID.name)
                /**
                 * Here the purpose of this condition is to check whether a Grid is already created or not
                 * if created then update the data only if not then we will create in else statement.
                 */
                if (src != null) {
                    (src as GeoJsonSource).setGeoJson(featureCollection)
                } else {
                    /**
                     * Binding Source Id and Source data to [UnlMapView] style.
                     */
                    style.addSource(GeoJsonSource(SourceIDs.GRID_SOURCE_ID.name,
                        featureCollection))
                    /**
                     * Creating a Layer and
                     * Binding Layer Id and Source  to [UnlMapView] style with properties like *line size* and *line color*.
                     */
                    style.addLayer(LineLayer(LayerIDs.GRID_LAYER_ID.name,
                        SourceIDs.GRID_SOURCE_ID.name)
                        .withProperties(
                            PropertyFactory.lineWidth(DEFAULT_GRID_LINE_WIDTH),
                            PropertyFactory.lineColor(ContextCompat.getColor(context,
                                R.color.default_grid_line_color))))
                }
            } else {
                Log.e(GRID_ERROR, "No Feature Found  in FeatureCollection")
            }

        } else {
            Log.e(GRID_ERROR, "Null values in FeatureCollection")
        }
    }
}

/**
 * [setGridControls] is an Extension method for [UnlMapView] to control Grid or Grid button.
 *
 * @param context context is used for inflate GridControls on [MapView]
 * @param showGrid showGrid is a boolean param and is used to enable/disable GridControls on [UnlMapView].
 */
fun UnlMapView.setGridControls(
    context: Context,
    showGrid: Boolean = false,
) {
    /**
     * Here the purpose of this condition is to check whether a user need the GridControls on the Map or not.
     * */
    if (showGrid) {
        val imageView = MapView.inflate(context, R.layout.item_grid_selector, null)
        val imageViewParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        /**
         * Here the purpose of this static Int values is to set the margins for our GridController Button.
         * */
        imageViewParams.setMargins(10, 10, 0, 0)
        imageView?.layoutParams = imageViewParams
        this.addView(imageView)
        /**
         * Use of Click Event Listener is to show [PrecisionDialog] to user so user can selected the [CellPrecision].
         * */
        imageView.setOnClickListener {
            val frag = PrecisionDialog(this,cellPrecision)
            fm.let { frag.show(it!!, com.unl.map.sdk.views.PrecisionDialog.TAG) }
        }
    }
}

/**
 * [getCell] method is to create [GridCell] Object from [UnlCore.encode] method and that Object contains locationId and siz of the cell
 *
 *
 * @param latLng param for [UnlCore.encode] method.
 * @param precision param to get Cell Size ,
 * @return [GridCell]
 */

fun getCell(latLng: LatLng, precision: CellPrecision): GridCell? {
    val size = getFormattedCellDimensions(precision)
    return try {
        val locationId = UnlCore.encode(latLng.latitude, latLng.longitude)
        GridCell(locationId = locationId, size = size)
    } catch (e: Exception) {
        Log.e(CELL_ERROR, "location ID not available")
        null
    }
}

/**
 * [locationIdToLngLat] method is used get [LatLng] from [UnlCore] Lib. according to selected location Id.
 *
 * @param locationId is used for [UnlCore.decode] method to get [LatLng]
 * @return [LatLng]
 */

fun locationIdToLngLat(locationId: String): LatLng? {
    return try {
        val decodedGeohashCoods = UnlCore.decode(locationId).coordinates
        return LatLng(decodedGeohashCoods.lat, decodedGeohashCoods.lon)
    } catch (e: Exception) {
        Log.e(CELL_ERROR, "decodedGeohash not available")
        null
    }
}


/**
 * [locationIdToBoundsCoordinates] method is used to get list of points/boundaries of selected Cell from Grid
 *
 *on the basis of [locationId] from [UnlCore] Lib. using [UnlCore.bounds] method.
 *
 * @param locationId is used to get [Bounds] of selected Grid Cell.
 * @return List<List<Point>>
 */
fun locationIdToBoundsCoordinates(locationId: String): List<List<Point>>? {
    return try {
        val unlCoreBounds = UnlCore.bounds(locationId)
        val points: ArrayList<List<Point>> = ArrayList()
        val outerPoints: ArrayList<Point> = ArrayList()
        outerPoints.add(Point.fromLngLat(unlCoreBounds.w, unlCoreBounds.n))
        outerPoints.add(Point.fromLngLat(unlCoreBounds.w, unlCoreBounds.s))
        outerPoints.add(Point.fromLngLat(unlCoreBounds.e, unlCoreBounds.s))
        outerPoints.add(Point.fromLngLat(unlCoreBounds.e, unlCoreBounds.n))
        points.add(outerPoints)
        return points
    } catch (e: Exception) {
        Log.e(CELL_ERROR, "Bounds not available")
        null
    }
}

/**
 * {CoroutineScope.executeAsyncTask} method is used to create a background Task using [CoroutineScope].
 *
 * @param R
 * @param onPreExecute
 * @param doInBackground
 * @param onPostExecute
 * @receiver
 * @receiver
 * @receiver
 */
fun <R> CoroutineScope.executeAsyncTask(
    onPreExecute: () -> Unit,
    doInBackground: () -> FeatureCollection,
    onPostExecute: (result: FeatureCollection) -> R,
) = launch {
    onPreExecute() // runs in Main Thread
    val result = withContext(Dispatchers.IO) {
        doInBackground() // runs in background thread without blocking the Main Thread
    }
    onPostExecute(result) // runs in Main Thread
}