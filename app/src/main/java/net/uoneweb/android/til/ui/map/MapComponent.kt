package net.uoneweb.android.til.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import net.uoneweb.android.til.ui.location.Location
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView


@Composable
fun MapComponent(initialLocation: Location = Location.Default, initialZoom: Double = 15.0) {
    val mapView = rememberMapView()

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray),
        factory = {
            mapView.getMapAsync { map ->
                map.setStyle("https://api.maptiler.com/maps/jp-mierune-streets/style.json?key=397cVdOrWVJxbRXCtkW1")
                map.cameraPosition = CameraPosition
                    .Builder()
                    .target(LatLng(initialLocation.latitude, initialLocation.longitude))
                    .zoom(initialZoom)
                    .build()
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