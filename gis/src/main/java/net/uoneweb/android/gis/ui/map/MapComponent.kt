package net.uoneweb.android.gis.ui.map

import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch
import net.uoneweb.android.gis.ui.location.Location
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
    mapViewState: MapViewState = MapViewState(),
) {
    if (LocalInspectionMode.current) {
        Box(
            modifier = modifier
                .background(Color.Green),
        ) {
            Text(modifier = Modifier.align(Alignment.Center), text = "MapView")
        }
    } else {
        MapView(mapViewState, modifier.background(Color.Gray))
    }
}

@Composable
fun MapView(
    mapViewState: MapViewState,
    modifier: Modifier,
) {
    val mapView = rememberMapView()
    val scope = rememberCoroutineScope()

    LaunchedEffect(mapViewState.requestLocation) {
        mapView.getMapAsync { map ->
            if (
                map.cameraPosition.target?.latitude != mapViewState.requestLocation.latitude.value
                || map.cameraPosition.target?.longitude != mapViewState.requestLocation.longitude.value
            ) {
                map.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            mapViewState.requestLocation.latitude.value,
                            mapViewState.requestLocation.longitude.value,
                        ),
                    ),
                )
            }
        }

    }

    LaunchedEffect(mapViewState.style) {
        mapView.getMapAsync { map ->
            Log.i("MapComponent", "try to set style: ${mapViewState.style}")
            map.setStyle(mapViewState.style.url) { it ->
                Log.i("MapComponent", "Style loaded: ${it.uri}")
                mapViewState.updateStyle(Styles.fromUri(it.uri))
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.getMapAsync { map ->
                Log.i("MapComponent", "Map ready")

                map.cameraPosition = CameraPosition
                    .Builder()
                    .target(LatLng(mapViewState.requestLocation.latitude.value, mapViewState.requestLocation.longitude.value))
                    .zoom(mapViewState.zoom)
                    .build()
                map.addOnMapClickListener { latlng ->
                    Log.i("MapComponent", "Map clicked at: ${latlng.latitude}, ${latlng.longitude}")

                    val point = PointF(latlng.longitude.toFloat(), latlng.latitude.toFloat())
                    val rect = RectF(0f, 0f, map.width, map.height)
                    scope.launch {
                        val list = map.queryRenderedFeatures(rect)
                        mapViewState.updateFeatures(list)
                    }

                    true
                }
                map.addOnCameraMoveListener {
                    val center = map.cameraPosition.target
                    Log.i("MapComponent", "Camera moved to: ${center?.latitude}, ${center?.longitude}")
                    if (center?.latitude != null && center?.longitude != null) {
                        mapViewState.updateLocation(
                            Location(
                                center.latitude,
                                center.longitude,
                            ),
                        )
                    }
                }
                map.addOnCameraIdleListener {
                    Log.i("MapComponent", "Camera idle")
                }
                map.addOnCameraMoveCancelListener {
                    Log.i("MapComponent", "Camera move canceled")
                }
                map.addOnCameraMoveStartedListener {
                    Log.i("MapComponent", "Camera move started")
                }

            }
            mapView
        },
        update = { it ->
            Log.i("MapComponent", "update. ")

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