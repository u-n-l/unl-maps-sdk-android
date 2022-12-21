package com.unl.map.sdk.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Polygon
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyValue
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.unl.map.sdk.adapters.TilesAdapter
import com.unl.map.sdk.data.*
import com.unl.map.sdk.helpers.grid_controls.*


/**
 * [UnlMapView] provides an embeddable map interface.
 * You use this class to display map information and to manipulate the map contents from your application.
 * You can center the map on a given coordinate, specify the size of the area you want to display,
 * and style the features of the map to fit your application's use case.
 *
 * @constructor
 *
 * @param context
 * @param attrs
 */
class UnlMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : MapView(context, attrs),
    TilesAdapter.ItemSelectedListener,
    PrecisionDialog.PrecisionListener {
    /**
     * [clickedLngLat]  is used to store the [LatLng] of selected Cell from the Grid.
     */
    var clickedLngLat: LatLng? = null

    /**
     * [tilesRecycler]  is an Instance of Tiles List or we can say TilesRecyclerView.
     */
    var tilesRecycler: RecyclerView? = null

    /**
     * [isVisibleTiles] is a Boolean variable which is used to enable/disable TileSelector Visibility and the default Value for this is false.
     */
    var isVisibleTiles: Boolean = false

    /**
     * [isVisibleGrids] is a Boolean variable which is used to enable/disable Grid Visibility and the default Value for this is false.
     */
    var isVisibleGrids: Boolean = false

    /**
     * [CellPrecision] is Enum and is for Grid Controls and the default value is 9.
     */
    var cellPrecision: CellPrecision = CellPrecision.GEOHASH_LENGTH_9

    /**
     * Fm is [FragmentManager] and it is to load Grid Control PopUp.
     */
    var fm: FragmentManager? = null

    /**
     * activity is [Activity] and it is to load Grid Control PopUp
     */
    var activity: Activity? = null

    /**
     * Iv tile is an [ImageView] and is used for enable/disable TileSelector
     */
    lateinit var ivTile: ImageView
    lateinit var ivArrow: ImageView

    /**
     * [tileSelectorView] is the Whole Tile Selector Controls to Switch views eg. "SATELLITE VIEW" or "VECTORIAL VIEW" etc
     */
    var tileSelectorView: View? = null
    lateinit var tileSelectorLayoutParams: LayoutParams

    var mapbox: MapboxMap? = null
    init {
        this.getMapAsync {
            mapbox = it
            mapbox?.uiSettings?.isAttributionEnabled = false
            mapbox?.uiSettings?.isLogoEnabled = false
            /**
             * Here we added a Map Camera Idle Listener to recognize whether the user drag offs the screen and then
             * load Grid on the Map.
             */
            mapbox?.addOnCameraIdleListener {
                mapbox?.loadGrids(isVisibleGrids, this, cellPrecision)
            }

            /**
             * Added A ClickListener to map so we can recognize the click event for cell Selection
             */
            mapbox?.addOnMapClickListener {
                mapbox?.getStyle { style ->

                    var src = style.getSource(SourceIDs.CELL_SOURCE_ID.name)
                    val zoomLevel = mapbox?.cameraPosition?.zoom!!
                    val minZoom = getZoomLevels()[getMinGridZoom(cellPrecision)]!!
                    /**
                     * Here a condition is placed on the basis of [isVisibleGrids] an [minZoom]
                     * Because we need to show Cell Selector only if Grid is Visible to user.
                     */
                    if (isVisibleGrids && zoomLevel >= minZoom) {
                        val clickedCell =
                            getCell(it, cellPrecision)
                        clickedLngLat = locationIdToLngLat(clickedCell?.locationId ?: "")
                        /**
                         * Here the purpose of condition is to check whether there is any source already created or not.
                         * If already created then we only update the Data for [GeoJsonSource] otherwise we are creating the new [GeoJsonSource]
                         * for Cell Selection.
                         */
                        if (src != null) {
                            try {
                                src = src as GeoJsonSource
                                src.setGeoJson(Feature.fromGeometry(Polygon.fromLngLats(
                                    locationIdToBoundsCoordinates(clickedCell?.locationId ?: "")
                                        ?: arrayListOf())))
                                style.getLayer(LayerIDs.CELL_LAYER_ID.name)
                                    ?.setProperties(PropertyValue(VISIBILITY, Property.VISIBLE))
                            } catch (e: Exception) {
                                Log.e(CELL_ERROR, "Error While Updating Grid Cell Source")
                            }
                        } else {
                            try {
                                /**
                                 * Here we create a new [GeoJsonSource] data to draw [Polygon] for selected Cell
                                 */
                                src =
                                    GeoJsonSource(SourceIDs.CELL_SOURCE_ID.name,
                                        Polygon.fromLngLats(
                                            locationIdToBoundsCoordinates(clickedCell?.locationId
                                                ?: "")
                                                ?: arrayListOf()))

                                style.addSource(src)
                                /**
                                 * Here we create [FillLayer] for Selected cell and add to Style of Map.
                                 * And also provide properties like *FillColor*.
                                 */
                                val fillLayer = FillLayer(LayerIDs.CELL_LAYER_ID.name,
                                    SourceIDs.CELL_SOURCE_ID.name).withProperties(PropertyFactory.fillColor(
                                    ContextCompat.getColor(context,
                                        com.unl.map.R.color.cell_default_color)))
                                style.addLayer(fillLayer)
                            } catch (e: Exception) {
                                Log.e(CELL_ERROR, "Error While Adding Grid Cell Source")
                            }
                        }
                    } else {
                        /**
                         * Here we get the Cell [FillLayer] and set visibility to None, so it shouldn't be shown to user
                         */
                        style.getLayer(LayerIDs.CELL_LAYER_ID.name)
                            ?.setProperties(PropertyValue(VISIBILITY, Property.NONE))
                    }
                }
                isVisibleGrids
            }
            setGridControls(context)
        }
    }

    /**
     * Load style is used to load style for [UnlMapView] using [TileEnum].
     *
     * @param tileData is an [Enum] and contains values for Map styles.
     */
    override fun loadStyle(tileData: TileEnum) {
        var url = ""
        when (tileData) {
            TileEnum.TERRAIN -> {
                url = Constants.TERRAIN
            }
            TileEnum.BASE -> {
                url = Constants.BASE
            }
            TileEnum.TRAFFIC -> {
                url = Constants.TRAFFIC
            }
            TileEnum.VECTORIAL -> {
                url = Constants.VECTORIAL
            }
            TileEnum.SATELLITE -> {
                url = Constants.SATELLITE
            }
        }

        mapbox?.setStyle(Style.Builder()
            .fromUri(url)) {
//            addSymbolAnnotation(it)
            mapbox?.loadGrids(isVisibleGrids, this, cellPrecision)
            if (isVisibleTiles) {
                tilesRecycler?.visibility = GONE
                ivArrow.visibility = GONE
            } else {
                tilesRecycler?.visibility = VISIBLE
                ivArrow.visibility = VISIBLE
            }
            isVisibleTiles = !isVisibleTiles
        }
    }

    /**
     * On precision selected method will be called when we hit the select button in [PrecisionDialog] and
     * will load Grid according to selected [CellPrecision] value
     *
     * @param cellPrecision is selected [CellPrecision] value from [PrecisionDialog]
     */
    override fun onPrecisionSelected(cellPrecision: CellPrecision) {
        isVisibleGrids = true
        this.cellPrecision = cellPrecision
        mapbox?.loadGrids(isVisibleGrids, this, cellPrecision)
    }

    private fun addSymbolAnnotation(style: Style) {

        // Add icon to the style
        addAirplaneImageToStyle(style)
        // Create a SymbolManager.
        val symbolManager = SymbolManager(this, mapbox!!, style)
        // Set non-data-driven properties.
        symbolManager.iconAllowOverlap = true
        symbolManager.iconIgnorePlacement = true
        // Create a symbol at the specified location.
        val symbolOptions = SymbolOptions()
            .withLatLng(LatLng(30.7120452, 76.8144185))
            .withIconImage("airport")
            .withIconSize(1.3f)

        // Use the manager to draw the annotations.
        symbolManager.create(symbolOptions)

    }
    private fun addAirplaneImageToStyle(style: Style) {
        val factory = LayoutInflater.from(context)
        val myView: View = factory.inflate(com.unl.map.R.layout.item_cell_label, null)
        style.addImage(
            "airport",
            myView.getBitmapFromView()!!,
            true
        )
    }

}