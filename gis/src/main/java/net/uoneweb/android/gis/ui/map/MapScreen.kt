package net.uoneweb.android.gis.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.window.Dialog
import net.uoneweb.android.gis.ui.location.CurrentLocationButton
import net.uoneweb.android.gis.ui.location.Location


@Composable
fun MapScreen() {
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var mapLocation by remember { mutableStateOf<Location?>(null) }
    var showDropDown by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        MapComponent(
            location = currentLocation ?: Location.Default,
            onLocationChanged = {
                mapLocation = it
            },
        )
        Cross(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center),
        )
        if (mapLocation != null) {
            Text(mapLocation?.latitude.toString() + ", " + mapLocation?.longitude.toString())
        }
        Button(modifier = Modifier.align(Alignment.TopEnd), onClick = { showDropDown = true }) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Icon")
        }
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
        StyleSelectorDialog(modifier = Modifier.align(Alignment.Center), expanded = showDropDown) {
            showDropDown = false
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

@Composable
fun StyleSelectorDialog(modifier: Modifier = Modifier, expanded: Boolean = false, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun MapScreenPreview() {
    MapScreen()
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
}