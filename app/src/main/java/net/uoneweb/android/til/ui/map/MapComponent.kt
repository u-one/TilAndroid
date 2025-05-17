package net.uoneweb.android.til.ui.map

import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import net.uoneweb.android.til.ui.location.Location
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.geojson.Feature

@Composable
fun MapComponent(
    location: Location = Location.Default,
    initialZoom: Double = 15.0,
    onLocationChanged: (Location) -> Unit = {},
) {
    if (LocalInspectionMode.current) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Blue) {
        }
        return
    }

    val mapView = rememberMapView()

    LaunchedEffect(location) {
        mapView.getMapAsync { map ->
            if (map.cameraPosition.target?.latitude != location.latitude || map.cameraPosition.target?.longitude != location.longitude) {
                map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
            }
        }

    }

    val featureList = remember { mutableStateListOf<Feature>() }

    LaunchedEffect(featureList) {
        if (featureList.isNotEmpty()) {
            for (feature in featureList) {
                Log.i("MapComponent", "Feature: ${feature}")
            }
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray),
        factory = {

            mapView.getMapAsync { map ->
                map.setStyle("https://api.maptiler.com/maps/jp-mierune-streets/style.json?key=397cVdOrWVJxbRXCtkW1")
                map.cameraPosition = CameraPosition
                    .Builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(initialZoom)
                    .build()
                map.addOnMapClickListener { latlng ->
                    Log.i("MapComponent", "Map clicked at: ${latlng.latitude}, ${latlng.longitude}")
                    val point = PointF(latlng.longitude.toFloat(), latlng.latitude.toFloat())
                    val list = map.queryRenderedFeatures(point)
                    featureList.clear()
                    for (feature in list) {
                        featureList.add(feature)
                    }
                    true
                }
                map.addOnCameraMoveListener {
                    val center = map.cameraPosition.target
                    Log.i("MapComponent", "Camera moved to: ${center?.latitude}, ${center?.longitude}")
                    if (center?.latitude != null && center?.longitude != null) {
                        onLocationChanged(Location(center.latitude, center.longitude))
                    }
                }

            }
            mapView
        },
    )
}


@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    MapLibre.getInstance(context)
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle, mapView) {
        val lifecycleObserver = getMapViewLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose { lifecycle.removeObserver(lifecycleObserver) }
    }
    return mapView
}

fun getMapViewLifecycleObserver(mapView: MapView): LifecycleObserver {
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException("Unsupported lifecycle event")
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun MapComponentPreview() {
    MapComponent()
}