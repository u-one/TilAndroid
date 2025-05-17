package net.uoneweb.android.til.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.gis.ui.location.CurrentLocationButton


@Composable
fun MapScreen() {
    var currentLocation by remember { mutableStateOf<net.uoneweb.android.gis.ui.location.Location?>(null) }
    var mapLocation by remember { mutableStateOf<net.uoneweb.android.gis.ui.location.Location?>(null) }
    Box(modifier = Modifier.fillMaxSize()) {
        MapComponent(
            location = currentLocation ?: net.uoneweb.android.gis.ui.location.Location.Default,
            onLocationChanged = {
                mapLocation = it
            },
        )
        CurrentLocationButton(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.BottomEnd),
            onLocation = { currentLocation = it },
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
            )
        }
        Canvas(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.Center),
        ) {
            val stroke = 20f
            drawLine(
                color = Color.Black,
                strokeWidth = 4f,
                start = center.copy(x = center.x - stroke),
                end = center.copy(x = center.x + stroke),
            )
            drawLine(
                color = Color.Black,
                strokeWidth = 4f,
                start = center.copy(y = center.y - stroke),
                end = center.copy(y = center.y + stroke),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun MapScreenPreview() {
    MapComponent()
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
}