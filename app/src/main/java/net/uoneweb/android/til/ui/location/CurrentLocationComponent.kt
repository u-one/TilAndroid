package net.uoneweb.android.til.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun CurrentLocationComponent(location: Location?, onLocation: (Location?) -> Unit) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var permissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
        },
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column {
        if (permissionGranted) {
            Button(
                onClick = {
                    getCurrentLocation(fusedLocationClient) { loc ->
                        onLocation(loc)
                    }
                },
            ) {
                Text("Get Current Location")
            }
            location?.let {
                Text("Latitude: ${it.latitude}, Longitude: ${it.longitude}")
            }
        } else {
            Text("Location permission is required.")
        }
    }
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location?) -> Unit,
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        onLocationReceived(location)
    }.addOnFailureListener {
        onLocationReceived(null)
    }
}

@Preview
@Composable
fun PreviewCurrentLocationComponent() {
    CurrentLocationComponent(location = null) {}
}