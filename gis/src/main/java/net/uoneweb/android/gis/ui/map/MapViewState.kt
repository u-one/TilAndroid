package net.uoneweb.android.gis.ui.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import net.uoneweb.android.gis.ui.location.Location
import org.maplibre.geojson.Feature

class MapViewState(
    initialStyle: Style = Style.Default,
    initialLocation: Location = Location.Default,
    initialZoom: Double = 15.0,
) {
    var style: Style by mutableStateOf(initialStyle)
        private set
    var requestLocation by mutableStateOf(initialLocation)
        private set
    var mapLocation: Location by mutableStateOf(initialLocation)
        private set
    var zoom: Double by mutableStateOf(initialZoom)
        private set

    var features by mutableStateOf<List<Feature>>(emptyList())
        private set

    fun requestLocation(newLocation: Location) {
        requestLocation = newLocation
    }

    fun updateLocation(newLocation: Location) {
        mapLocation = newLocation
    }

    fun updateStyle(newStyle: Style) {
        style = newStyle
    }

    fun updateFeatures(newFeatures: List<Feature>) {
        for (feature in newFeatures) {
            Log.i("MapViewState", "Feature: ${feature}")
        }
        features = newFeatures
    }
}