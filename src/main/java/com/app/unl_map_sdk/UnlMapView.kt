package com.app.unl_map_sdk

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.unl_map_sdk.adapters.TilesAdapter
import com.app.unl_map_sdk.data.Constants
import com.app.unl_map_sdk.data.TileEnum
import com.app.unl_map_sdk.helpers.grid_controls.loadGrids
import com.app.unl_map_sdk.helpers.grid_controls.setGridControls
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style


class UnlMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : MapView(context, attrs), TilesAdapter.ItemSelectedListener {
    var tilesRecycler: RecyclerView? = null
    var isVisibleTiles: Boolean = false
    var isVisibleGrids: Boolean = false
    var activity: Activity? = null
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
                Log.e("EVENT", "Map Move End")
                mapbox?.loadGrids(isVisibleGrids, context, this)
            }
            mapbox?.cameraPosition = CameraPosition.Builder()
                .target(LatLng(LATITUDE, LONGITUDE))
                .zoom(ZOOM)
                .build()
            setGridControls(context)
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
            mapbox?.loadGrids(isVisibleGrids, context, this)
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

}


