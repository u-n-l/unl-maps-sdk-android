package com.app.unl_map_sdk.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.app.unl_map_sdk.adapters.TilesAdapter
import com.app.unl_map_sdk.data.*
import com.app.unl_map_sdk.helpers.grid_controls.*
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Polygon
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyValue
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


class UnlMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : MapView(context, attrs), TilesAdapter.ItemSelectedListener, PrecisionDialog.PrecisionListener {
    var clickedLngLat: LatLng? = null
    var tilesRecycler: RecyclerView? = null
    var isVisibleTiles: Boolean = false
    var isVisibleGrids: Boolean = false
    var cellPrecision: CellPrecision? = CellPrecision.GEOHASH_LENGTH_9
    var fm: FragmentManager? = null
    lateinit var ivTile: ImageView
    lateinit var ivArrow: ImageView
    var tileSelectorView: View? = null
    lateinit var tileSelectorLayoutParams: LayoutParams

    var mapbox: MapboxMap? = null

    init {
        this.getMapAsync {
            mapbox = it
            mapbox?.uiSettings?.setAttributionMargins(15, 0, 0, 15)
            mapbox?.uiSettings?.isAttributionEnabled = false
            mapbox?.uiSettings?.isLogoEnabled = false
            mapbox?.addOnCameraIdleListener {
                mapbox?.loadGrids(isVisibleGrids, context, this, cellPrecision!!)
            }
            mapbox?.addOnMapClickListener {
                mapbox?.getStyle { style ->
                    var src = style.getSource(SourceIDs.CELL_SOURCE_ID.name)
                    var zoomLevel =mapbox?.cameraPosition?.zoom!!
                    var minZoom = getZoomLevels()[getMinGridZoom(cellPrecision!!)]
                    if (isVisibleGrids && zoomLevel >= minZoom!!) {
                        var clickedCell =
                            getCell(it, cellPrecision ?: CellPrecision.GEOHASH_LENGTH_9)
                        clickedLngLat = locationIdToLngLat(clickedCell?.locationId ?: "")
                        if (src != null) {
                            src=src as GeoJsonSource
                            src.setGeoJson(Feature.fromGeometry(Polygon.fromLngLats(
                                locationIdToBoundsCoordinates(clickedCell?.locationId ?: "")
                                    ?: arrayListOf())))
                            style.getLayer(LayerIDs.CELL_LAYER_ID.name)
                                ?.setProperties(PropertyValue("visibility", "visible"))
                        } else {
                            src =
                                GeoJsonSource(SourceIDs.CELL_SOURCE_ID.name, Polygon.fromLngLats(
                                    locationIdToBoundsCoordinates(clickedCell?.locationId ?: "")
                                        ?: arrayListOf()))

                            style.addSource(src)
                            var fillLayer = FillLayer(LayerIDs.CELL_LAYER_ID.name,
                                SourceIDs.CELL_SOURCE_ID.name).withProperties(PropertyFactory.fillColor(
                                Color.parseColor("#3bb2d0")))
                            style.addLayer(fillLayer)
                        }
                    } else {
                        style.getLayer(LayerIDs.CELL_LAYER_ID.name)
                            ?.setProperties(PropertyValue("visibility", "none"))
                    }
                }
                isVisibleGrids
            }
            mapbox?.cameraPosition = CameraPosition.Builder()
                .target(LatLng(LATITUDE, LONGITUDE))
                .zoom(ZOOM)
                .build()
            setGridControls(context)
        }
    }

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
            mapbox?.loadGrids(isVisibleGrids, context, this, cellPrecision!!)
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

    companion object {
        private const val LATITUDE = 45.525727
        private const val LONGITUDE = -122.681125
        private const val ZOOM = 14.0
    }

    override fun onPrecisionSelected(cellPrecision: CellPrecision) {
        isVisibleGrids = true
        this.cellPrecision = cellPrecision
        mapbox?.loadGrids(isVisibleGrids, context, this, cellPrecision)

    }

    override fun onPrecisionCanceled() {
        isVisibleGrids = false
        mapbox?.getStyle { style ->
            if (style.layers.size > 0) {
                style.removeLayer(LayerIDs.GRID_LAYER_ID.name)
                style.removeSource(SourceIDs.GRID_SOURCE_ID.name)
            }
        }
    }

}


