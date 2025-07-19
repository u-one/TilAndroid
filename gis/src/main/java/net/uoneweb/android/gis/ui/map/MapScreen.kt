package net.uoneweb.android.gis.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
    var showDropDown by remember { mutableStateOf(false) }
    val mapViewState = remember { MapViewState() }
    var userMessage by remember { mutableStateOf("") }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
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
                Button(modifier = Modifier.align(Alignment.TopEnd), onClick = { showDropDown = true }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Icon")
                }
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
                StyleSelectorDialog(
                    expanded = showDropDown,
                    onDismissRequest = {
                        showDropDown = false
                    },
                    onStyleSelected = {
                        mapViewState.updateStyle(it)
                    },
                )
            }
            FeatureList(mapViewState, modifier = Modifier.weight(1f))
            AIChatUI(
                userMessage = userMessage,
                onUserMessageChange = { userMessage = it },
            )
        }

    }
}

@Composable
fun Cross(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier,
    ) {
        val stroke = size.width / 2
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


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun MapScreenPreview() {
    MapScreen()
}
