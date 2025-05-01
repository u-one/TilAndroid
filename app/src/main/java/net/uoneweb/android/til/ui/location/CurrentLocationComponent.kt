package net.uoneweb.android.til.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            Row {
                var loading by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        loading = true
                        getCurrentLocation(fusedLocationClient) { loc ->
                            loading = false
                            onLocation(loc)
                        }
                    },
                ) {
                    Text("Get Current Location")
                }
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically),
                    )
                }
                if (location != null) {
                    Text("Latitude: ${location.latitude}, Longitude: ${location.longitude}",
                        modifier = Modifier.align(Alignment.CenterVertically))
                }
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
    fusedLocationClient.lastLocation.addOnSuccessListener { androidLocation ->
        val location = androidLocation?.let {
            Location(it.latitude, it.longitude)
        }
        onLocationReceived(location)
    }.addOnFailureListener {
        onLocationReceived(null)
    }
}

@Preview
@Composable
fun PreviewCurrentLocationComponent() {
    CurrentLocationComponent(location = Location(1.0, 2.0)) {}
}