package com.app.unl_map_sdk.helpers.grid_controls

import android.content.Context
import android.graphics.Color
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.app.unl_map_sdk.R
import com.app.unl_map_sdk.data.*
import com.app.unl_map_sdk.views.PrecisionDialog
import com.app.unl_map_sdk.views.UnlMapView
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import unl.core.Bounds
import unl.core.UnlCore

fun MapboxMap?.loadGrids(
    isVisibleGrids: Boolean,
    context: Context,
    unlMapView: UnlMapView,
    cellPrecision: CellPrecision,
) {
    var latLngBounds = this?.projection?.visibleRegion?.latLngBounds
    var mapBoundsWidth = latLngBounds?.northEast?.longitude!! - latLngBounds?.southWest?.longitude!!
    var mapBoundsHeight = latLngBounds?.northEast?.latitude!! - latLngBounds?.southWest?.latitude!!

    var bounds = Bounds(latLngBounds?.latNorth!! + mapBoundsWidth,
        latLngBounds.lonEast + mapBoundsHeight,
        latLngBounds.latSouth - mapBoundsWidth,
        latLngBounds.lonWest - mapBoundsHeight)
    var zoomLevel = this?.cameraPosition?.zoom!!
    var minZoom = getZoomLevels()[getMinGridZoom(cellPrecision)]
    if (zoomLevel >= minZoom!! && isVisibleGrids) {
        var lines = UnlCore.gridLines(bounds, getCellPrecisions()[cellPrecision]!!)
        LoadGeoJson(unlMapView, context, lines).execute(lines);
    } else {
        this?.getStyle { style ->
            if (style.layers.size > 0) {
                style.removeLayer(LayerIDs.GRID_LAYER_ID.name)
                style.removeSource(SourceIDs.GRID_SOURCE_ID.name)
            }
        }
    }
}

fun MapboxMap?.drawLines(featureCollection: FeatureCollection) {
    if (this != null) {
        this?.getStyle { style ->
            if (featureCollection.features() != null) {
                if (featureCollection.features()!!.size > 0) {
                        var src = style.getSource(SourceIDs.GRID_SOURCE_ID.name)
                    if (src != null) {
                        (src as GeoJsonSource).setGeoJson(featureCollection)
                    } else {
                        style.addSource(GeoJsonSource(SourceIDs.GRID_SOURCE_ID.name,
                            featureCollection))
                        style.addLayer(LineLayer(LayerIDs.GRID_LAYER_ID.name,
                            SourceIDs.GRID_SOURCE_ID.name)
                            .withProperties(
                                PropertyFactory.lineWidth(1f),
                                PropertyFactory.lineColor(Color.parseColor("#C0C0C0"))))
                    }

                }
            }
        }
    }
}

fun UnlMapView.setGridControls(
    context: Context,
    showGrid: Boolean = false
) {
    if(showGrid) {
        var imageView = MapView.inflate(context, R.layout.item_grid_selector, null)
        var imageViewParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        imageViewParams.setMargins(10, 10, 0, 0)
        imageView?.layoutParams = imageViewParams
        this.addView(imageView)
        imageView.setOnClickListener {

            var frag = PrecisionDialog(this)
            fm.let { frag.show(it!!, "TAG") }
        }
    }
}
