package com.unl.map

import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.unl.map.sdk.data.*
import com.unl.map.sdk.helpers.grid_controls.getCell
import com.unl.map.sdk.helpers.grid_controls.locationIdToBoundsCoordinates
import com.unl.map.sdk.helpers.grid_controls.locationIdToLngLat
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Helper Functions Unit Test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HelperFunctionsUnitTest {
    /**
     * [locationIdLatLng_isCorrect] method is a Test Case to Test [locationIdToLngLat] method.
     *  And expected return data is [LatLng] according to LocationId.
     */
    @Test
    fun locationIdLatLng_isCorrect() {
        var expectedData = LatLng(47.17500002, 21.73100011)
        var locationId = "u2rkqebn312h"
        assertEquals(expectedData, locationIdToLngLat(locationId))
    }

    /**
     * [getGridCell_isCorrect] method is a Test Case to Test [getCell] method.
     *  And expected return data is [GridCell] according to LocationId and Cell Size.
     */
    @Test
    fun getGridCell_isCorrect() {
        var latLn = LatLng(47.17500002, 21.73100011)
        var locationId = "u2rkqebn3"
        var cellSize = "4.8m x 4.8m"
        var expectedData = GridCell(locationId, cellSize)
        assertEquals(expectedData, getCell(latLn, CellPrecision.GEOHASH_LENGTH_9))
    }

    /**
     * [locationIdToBoundsCoordinates_isCorrect] method is a Test Case to Test [locationIdToBoundsCoordinates] method.
     *  And expected return data is ArrayList<List<Point>> for Selected LocationId.
     */
    @Test
    fun locationIdToBoundsCoordinates_isCorrect() {
        val expectedData: ArrayList<List<Point>> = ArrayList()
        val points: ArrayList<Point> = ArrayList()
        var locationId = "u2rkqebn312h"
        points.add(Point.fromLngLat(21.73099994659424, 47.175000105053186))
        points.add(Point.fromLngLat(21.73099994659424, 47.17499993741512))
        points.add(Point.fromLngLat(21.731000281870365, 47.17499993741512))
        points.add(Point.fromLngLat(21.731000281870365, 47.175000105053186))
        points.add(Point.fromLngLat(21.73099994659424, 47.175000105053186))
        expectedData.add(points)
        assertEquals(expectedData, locationIdToBoundsCoordinates(locationId))
    }

    /**
     * [getFormattedCellDimensions_isCorrect] method is a Test Case to Test [getFormattedCellDimensions] method.
     *  And expected return data is size of Selected GridCell
     */
    @Test
    fun getFormattedCellDimensions_isCorrect() {
        var expectedData = "4.8m x 4.8m"
        assertEquals(expectedData, getFormattedCellDimensions(CellPrecision.GEOHASH_LENGTH_9))
    }

    /**
     * [getMinGridZoom_isCorrect] method is a Test Case to Test [getMinGridZoom] method.
     *  And expected return data is [ZoomLevel] enum for Selected [CellPrecision]
     */
    @Test
    fun getMinGridZoom_isCorrect() {
        var expectedData = ZoomLevel.MIN_GRID_ZOOM_GEOHASH_LENGTH_9
        assertEquals(expectedData, getMinGridZoom(CellPrecision.GEOHASH_LENGTH_9))
    }
}