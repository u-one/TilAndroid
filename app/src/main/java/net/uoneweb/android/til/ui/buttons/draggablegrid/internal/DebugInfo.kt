package net.uoneweb.android.til.ui.buttons.draggablegrid.internal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import net.uoneweb.android.til.ui.buttons.draggablegrid.DraggableGridState

@Composable
internal fun DebugInfo(draggableGridState: DraggableGridState) {
    Text(
        style = MaterialTheme.typography.body2,
        text =
            "draggingIndex: ${draggableGridState.draggingIndex}",
    )
    Text(
        style = MaterialTheme.typography.body2,
        text =
            "draggingCenter: ${draggableGridState.draggingCenter()}",
    )
    Text(
        style = MaterialTheme.typography.body2,
        text =
            "indexUnderDrag: ${draggableGridState.itemIndexUnderDrag()}",
    )
}

@Composable
private fun LazyGridItemInfo(info: LazyGridItemInfo) {
    Column {
        Text(
            style = MaterialTheme.typography.body1,
            text =
                "draggingItem:",
        )
        Text(
            style = MaterialTheme.typography.body2,
            text =
                "index: ${info.index}",
        )
        Text(
            style = MaterialTheme.typography.body2,
            text =
                "offset: ${info.offset}",
        )
        Text(
            style = MaterialTheme.typography.body2,
            text =
                "size: ${info.size}",
        )
    }
}

@Composable
internal fun DebugOverlay(draggableGridState: DraggableGridState) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(color = Color.Red, radius = 16f, center = draggableGridState.draggingCenter())
    }
}
