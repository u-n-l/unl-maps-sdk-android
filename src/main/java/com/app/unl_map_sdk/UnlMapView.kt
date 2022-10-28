package com.app.unl_map_sdk

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.contains
import androidx.recyclerview.widget.RecyclerView
import com.app.unl_map_sdk.adapters.TilesAdapter
import com.app.unl_map_sdk.data.Constants
import com.app.unl_map_sdk.data.TileEnum
import com.app.unl_map_sdk.helpers.LoadGeoJson
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import unl.core.Bounds
import unl.core.UnlCore


class UnlMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : MapView(context, attrs), TilesAdapter.ItemSelectedListener {
    private var tilesRecycler: RecyclerView? = null
    private var isVisibleTiles: Boolean = false
    private var isVisibleGrids: Boolean = false
    private lateinit var ivTile: ImageView
    private lateinit var ivArrow: ImageView
    private var tileSelectorView: View? = null
    private lateinit var tileSelectorLayoutParams: LayoutParams

    var mapbox: MapboxMap? = null

    init {
//        enableTileSelector()
        this.getMapAsync {
            mapbox = it
            mapbox?.uiSettings?.setAttributionMargins(15, 0, 0, 15)
            mapbox?.uiSettings?.isAttributionEnabled = false
            mapbox?.uiSettings?.isLogoEnabled = false
            mapbox?.addOnCameraIdleListener {
                Log.e("EVENT", "Map Move End")
                loadGrids()
            }
            mapbox?.cameraPosition = CameraPosition.Builder()
                .target(LatLng(LATITUDE, LONGITUDE))
                .zoom(ZOOM)
                .build()

        }
    }

    private fun loadGrids() {
        var latLngBounds = mapbox?.projection?.visibleRegion?.latLngBounds
        var bounds = Bounds(latLngBounds?.latNorth!!,
            latLngBounds.lonEast,
            latLngBounds.latSouth,
            latLngBounds.lonWest)
        var zoomLevel = mapbox?.cameraPosition?.zoom!!
        if (zoomLevel >= 17 && isVisibleGrids) {
            var lines = UnlCore.gridLines(bounds, 9)
            LoadGeoJson(this, this.context, lines).execute(lines);
        } else {
            mapbox?.getStyle { style ->
                if (style.layers.size > 0) {
                    style.removeLayer("linelayer")
                    style.removeSource("line-source")
                }
            }
        }
    }

    fun enableTileSelector(boolean: Boolean = true) {

        tileSelectorView = inflate(context, R.layout.layout_tile_selector, null)
        var imageView = inflate(context, R.layout.item_grid_selector, null)
        tilesRecycler = tileSelectorView?.findViewById<RecyclerView>(R.id.recyclerView)
        ivTile = tileSelectorView?.findViewById<ImageView>(R.id.ivTile)!!
        var imageViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imageViewParams.setMargins(10, 10, 0, 0)
        imageView?.layoutParams = imageViewParams
        addView(imageView)
        ivArrow = tileSelectorView?.findViewById<ImageView>(R.id.imageView)!!
        tilesRecycler?.adapter = TilesAdapter(context, TileEnum.values(), this)
        tileSelectorLayoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        tileSelectorView?.layoutParams = tileSelectorLayoutParams
        if (boolean) {
            addView(tileSelectorView)
        } else {
            if (contains(tileSelectorView!!))
                removeView(tileSelectorView)
        }
        imageView.setOnClickListener {
            isVisibleGrids = !isVisibleGrids
            if (!isVisibleGrids) {
                mapbox?.getStyle { style ->
                    if (style.layers.size > 0) {
                        style.removeLayer("linelayer")
                        style.removeSource("line-source")
                    }
                }
            } else {
                loadGrids()
            }
        }

        ivTile.setOnClickListener {
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


    fun setTileSelectorGravity(gravity: Int) {
        if (tileSelectorView != null) {
            tileSelectorLayoutParams.gravity = gravity
            tileSelectorView?.layoutParams = tileSelectorLayoutParams
        }
    }

    override fun onItemSelected(tileData: TileEnum) {
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
            loadGrids()
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
        private const val GEOJSON_SOURCE_ID = "line"
        private const val LATITUDE = 45.525727
        private const val LONGITUDE = -122.681125
        private const val ZOOM = 14.0
    }


    fun drawLines(featureCollection: FeatureCollection) {
        if (mapbox != null) {
            mapbox?.getStyle { style ->
                if (featureCollection.features() != null) {
                    if (featureCollection.features()!!.size > 0) {
                        var src = style.getSource("line-source")
                        if (src != null) {
                            (src as GeoJsonSource).setGeoJson(featureCollection)
                        } else {
                            style.addSource(GeoJsonSource("line-source", featureCollection))
                            style.addLayer(LineLayer("linelayer", "line-source")
                                .withProperties(
                                    PropertyFactory.lineWidth(1f),
                                    PropertyFactory.lineColor(Color.parseColor("#C0C0C0"))))
                        }

                    }
                }
            }
        }
    }
}
