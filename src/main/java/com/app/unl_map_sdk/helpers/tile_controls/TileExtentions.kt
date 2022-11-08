package com.app.unl_map_sdk.helpers.tile_controls

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.contains
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.app.unl_map_sdk.R
import com.app.unl_map_sdk.views.UnlMapView
import com.app.unl_map_sdk.adapters.TilesAdapter
import com.app.unl_map_sdk.data.TileEnum
import com.mapbox.mapboxsdk.maps.MapView

fun UnlMapView.enableTileSelector(
    boolean: Boolean = false
) {
    tileSelectorView = MapView.inflate(context, R.layout.layout_tile_selector, null)
    tilesRecycler = tileSelectorView?.findViewById<RecyclerView>(R.id.recyclerView)
    ivTile = tileSelectorView?.findViewById<ImageView>(R.id.ivTile)!!
    ivArrow = tileSelectorView?.findViewById<ImageView>(R.id.imageView)!!
    tilesRecycler?.adapter = TilesAdapter(context, TileEnum.values(), this)
    tileSelectorLayoutParams =
        FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
    tileSelectorView?.layoutParams = tileSelectorLayoutParams
    if (boolean) {
        addView(tileSelectorView)
    } else {
        if (contains(tileSelectorView!!))
            removeView(tileSelectorView)
        loadStyle(TileEnum.TERRAIN)
    }

    ivTile.setOnClickListener {
        if (isVisibleTiles) {
            tilesRecycler?.visibility = MapView.GONE
            ivArrow.visibility = MapView.GONE
        } else {
            tilesRecycler?.visibility = MapView.VISIBLE
            ivArrow.visibility = MapView.VISIBLE
        }
        isVisibleTiles = !isVisibleTiles
    }

}

fun UnlMapView.setTileSelectorGravity(gravity: Int) {
    if (tileSelectorView != null) {
        tileSelectorLayoutParams.gravity = gravity
        tileSelectorView?.layoutParams = tileSelectorLayoutParams
    }
}