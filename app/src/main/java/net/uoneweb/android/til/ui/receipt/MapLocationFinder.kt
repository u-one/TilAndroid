package net.uoneweb.android.til.ui.receipt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.uoneweb.android.gis.ui.location.CurrentLocationButton
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.gis.ui.map.Cross
import net.uoneweb.android.gis.ui.map.MapComponent
import net.uoneweb.android.gis.ui.map.MapViewState

@Composable
fun MapLocationFinder(location: Location, onLocationSelected: (Location) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Button(
        onClick = {
            expanded = true
        },
    ) {
        Text("Find Loction")
    }
    if (expanded) {
        MapLocationDialog(
            location,
            onLocationSelected = {
                onLocationSelected(it)
                expanded = false
            },
            onDismissRequest = {
                expanded = false
            },
        )
    }
}

@Composable
fun MapLocationDialog(location: Location = Location.Default, onLocationSelected: (Location) -> Unit = {}, onDismissRequest: () -> Unit = {}) {
    val mapViewState = remember { MapViewState(initialLocation = location) }
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(this@BoxWithConstraints.maxHeight / 2),
                    ) {
                        MapComponent(
                            modifier = Modifier.fillMaxSize(),
                            mapViewState = mapViewState,
                        )
                        Cross(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.Center),
                        )
                        Text(mapViewState.mapLocation.latitude.toString() + ", " + mapViewState.mapLocation.longitude.toString())

                        CurrentLocationButton(
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.BottomEnd),
                            onLocation = { it?.let { mapViewState.requestLocation(it) } },
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location Icon",
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxSize()) {
                        Button(
                            onClick = {
                                onDismissRequest()
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                onLocationSelected(mapViewState.mapLocation)
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("OK")
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapLocationDialogPreview() {
    MapLocationDialog()
}