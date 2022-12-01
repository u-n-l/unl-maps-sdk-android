package com.app.unl_map_sdk.helpers.tile_controls

import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.contains
import com.app.unl_map_sdk.R
import com.app.unl_map_sdk.adapters.TilesAdapter
import com.app.unl_map_sdk.data.TILE_ERROR
import com.app.unl_map_sdk.data.TileEnum
import com.app.unl_map_sdk.views.UnlMapView
import com.mapbox.mapboxsdk.maps.MapView

/**
 * [enableTileSelector] method is an Extension method for [UnlMapView] to enable/disable TileSelector Controls.
 *
 * @param boolean this boolean param describes the visibility of TileSelector controls and
 * the default value for is false, that means we need to pass the true value from Application side if wants to display SDK's Tile Controls
 */
fun UnlMapView.enableTileSelector(
    boolean: Boolean = false,
) {
    tileSelectorView = MapView.inflate(context, R.layout.layout_tile_selector, null)
    tilesRecycler = tileSelectorView?.findViewById(R.id.recyclerView)
    ivTile = tileSelectorView?.findViewById(R.id.ivTile)!!
    ivArrow = tileSelectorView?.findViewById(R.id.imageView)!!
    tilesRecycler?.adapter = TilesAdapter(TileEnum.values(), this)
    tileSelectorLayoutParams =
        FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
    tileSelectorView?.layoutParams = tileSelectorLayoutParams

    /**
     * Here the purpose of condition is to check whether we want to show SDKs tile controls or not
     */
    if (boolean) {
        addView(tileSelectorView)
    } else {
        if (contains(tileSelectorView!!))
            removeView(tileSelectorView)
        loadStyle(TileEnum.TERRAIN)
    }
    /**
     * This is click Event Listener for Tile selector.
     *
     * This Event will be called when we try to change the Tile Style
     */
    ivTile.setOnClickListener {
        /**
         * Here the purpose of If condition is to check whether the List of Tiles are already Visible to User or not.
         *
         *If Visible then will Hide and vice versa.
         */
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

/**
 * [setTileSelectorGravity] method is used to set The Position of Tile Selector.
 *
 * @param gravity  gravity param defines the position of TileSelector.
 */
fun UnlMapView.setTileSelectorGravity(gravity: Int) {
    /**
     * Here the purpose of If condition is to check whether Tile Selector View is created or not.
     */
    if (tileSelectorView != null) {
        tileSelectorLayoutParams.gravity = gravity
        tileSelectorView?.layoutParams = tileSelectorLayoutParams
    } else {
        Log.e(TILE_ERROR, "Tile Selector View is not created Yet!")
    }
}