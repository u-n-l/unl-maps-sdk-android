package com.app.unl_map_sdk.helpers.grid_controls

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.annotation.Nullable
import com.app.unl_map_sdk.views.UnlMapView
import com.app.unl_map_sdk.data.MyFeature
import com.app.unl_map_sdk.data.Props
import com.google.gson.Gson
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*

class LoadGeoJson(
    map: UnlMapView,
    activity: Context?,
    linesData: MutableList<Array<DoubleArray>>,
) :
    AsyncTask<MutableList<Array<DoubleArray>>?, Void?, FeatureCollection?>() {
    private val weakReference: WeakReference<Context>
    private val mapRef: WeakReference<UnlMapView>
    private val dataRef: WeakReference<MutableList<Array<DoubleArray>>>

    override fun onPostExecute(@Nullable featureCollection: FeatureCollection?) {
        super.onPostExecute(featureCollection)
        val activity: Context? = weakReference.get()
        val data: MutableList<Array<DoubleArray>>? = dataRef.get()
        val map: UnlMapView? = mapRef.get()
        if (activity != null && featureCollection != null) {
            map?.mapbox.drawLines(featureCollection)
        }
    }

    companion object {
        fun convertStreamToString(`is`: InputStream?): String {
            val scanner: Scanner = Scanner(`is`).useDelimiter("\\A")
            return if (scanner.hasNext()) scanner.next() else ""
        }
    }

    init {
        weakReference = WeakReference(activity)
        mapRef = WeakReference(map)
        dataRef = WeakReference(linesData)
    }

    override fun doInBackground(vararg p0: MutableList<Array<DoubleArray>>?): FeatureCollection? {
        try {
            val activity: Context? = weakReference.get()
            val lines = p0[0]
            if (activity != null) {
                val inputStream: InputStream = activity.assets.open("test.geojson")

                var features = ArrayList<Feature>()
                lines?.distinctBy {
                    var props = Props(name = "Grid Lines")
                    var geometry =
                        com.app.unl_map_sdk.data.Geometry(type = "LineString", coordinates = it)
                    var feature =
                        MyFeature(type = "Feature", properties = props, geometry = geometry)
                    var dt = Gson().toJson(feature)
                    features.add(Feature.fromJson(dt))
                }
                Log.e("TEST", Gson().toJson(features))
                var fc = FeatureCollection.fromJson(convertStreamToString(inputStream))
                fc.features()?.clear()
                fc.features()?.addAll(features)
//                    fc.features()?.add(Feature("Feature", null, null, geometry as Geometry,null))
                return fc
            }
        } catch (exception: Exception) {
            Log.e("Exception", exception.toString())
        }
        return null
    }
}