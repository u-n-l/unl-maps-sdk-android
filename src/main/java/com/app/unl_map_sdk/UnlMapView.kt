package com.app.unl_map_sdk

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.contains
import androidx.recyclerview.widget.RecyclerView
import com.app.unl_map_sdk.adapters.TilesAdapter
import com.app.unl_map_sdk.data.Constants
import com.app.unl_map_sdk.data.TileEnum
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style

class UnlMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : MapView(context, attrs), TilesAdapter.ItemSelectedListener {
    private var tilesRecycler: RecyclerView? = null
    private var isVisibleTiles: Boolean = false
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
                Log.e("EVENT","Map Move End")
            }
            mapbox?.cameraPosition = CameraPosition.Builder()
                .target(LatLng(34.126256, 74.832149))
                .zoom(12.0)
                .build()
        }
    }

    fun enableTileSelector(boolean: Boolean = true) {

        tileSelectorView = View.inflate(context, R.layout.layout_tile_selector, null)
        tilesRecycler = tileSelectorView?.findViewById<RecyclerView>(R.id.recyclerView)
        ivTile = tileSelectorView?.findViewById<ImageView>(R.id.ivTile)!!
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
        ivTile.setOnClickListener {
            if (isVisibleTiles) {
                tilesRecycler?.visibility = View.GONE
                ivArrow.visibility = View.GONE
            } else {
                tilesRecycler?.visibility = View.VISIBLE
                ivArrow.visibility = View.VISIBLE
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
            if (isVisibleTiles) {
                tilesRecycler?.visibility = View.GONE
                ivArrow.visibility = View.GONE
            } else {
                tilesRecycler?.visibility = View.VISIBLE
                ivArrow.visibility = View.VISIBLE
            }
            isVisibleTiles = !isVisibleTiles
        }
    }


}