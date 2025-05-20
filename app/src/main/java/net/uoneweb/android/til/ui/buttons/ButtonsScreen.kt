package net.uoneweb.android.til.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.buttons.draggablegrid.DraggableGrid
import java.security.MessageDigest

@Composable
fun ButtonsScreen() {
    // A list of characters from A to Z
    val list = ('A'..'Z').map { it.toString() }
    DraggableGrid(
        list = list,
        onListChanged = {},
    ) { index: Int, item: Any, dragging: Boolean, dragOffset: Offset ->
        GridItem(
            item = item as String,
            modifier = Modifier.size(64.dp),
            index = index,
            isDragging = dragging,
            dragOffset = dragOffset,
            onClickItem = {
            },
        )
    }
}

@Composable
private fun GridItem(
    item: String,
    modifier: Modifier = Modifier,
    index: Int = 0,
    onClickItem: (item: String) -> Unit = {},
    isDragging: Boolean = false,
    dragOffset: Offset = Offset.Zero,
) {
    val enableItemColor = true
    Box(
        modifier =
            modifier
                .background(if (isDragging) Color.Green else Color.Transparent)
                .padding(8.dp),
    ) {
        if (!isDragging) {
            ItemButton(item, onClickItem, enableItemColor)
        } else {
            DraggingItem(item, dragOffset)
        }
        Text(index.toString())
    }
}

@Composable
private fun ItemButton(
    item: String,
    onClickItem: (String) -> Unit = {},
    enableItemColor: Boolean = false,
) {
    val itemColor =
        remember(item) {
            val hash = item.toSHA256ToInt()
            Color(hash or 0xFF000000.toInt())
        }
    Button(
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(4.dp),
        colors =
            if (enableItemColor) {
                ButtonDefaults.buttonColors(
                    containerColor = itemColor,
                )
            } else {
                ButtonDefaults.buttonColors()
            },
        onClick = { onClickItem(item) },
    ) {
        Text(item)
    }
}

@Composable
private fun DraggingItem(
    item: String,
    dragOffset: Offset = Offset.Zero,
) {
    val density = LocalDensity.current
    val offsetX = with(density) { dragOffset.x.toDp() }
    val offsetY = with(density) { dragOffset.y.toDp() }
    Box(
        modifier =
            Modifier
                .size(56.dp)
                .offset(x = offsetX, y = offsetY)
                .background(Color.Gray),
        contentAlignment = Alignment.Center,
    ) {
        Text(item)
    }
}

private fun String.toSHA256ToInt(): Int {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(this.toByteArray())
    // translate byte array to int (use first 4 bytes)
    return digest.slice(0..3).fold(0) { acc, byte -> (acc shl 8) or (byte.toInt() and 0xFF) }
}

@Preview(
    showBackground = true,
)
@Composable
private fun ButtonsScreenPreview() {
    MaterialTheme {
        ButtonsScreen()
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun GridItemPreview() {
    MaterialTheme {
        Column {
            GridItem("Text")
            GridItem("Text", isDragging = true)
            GridItem("Text", isDragging = true)
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun ItemButtonPreview() {
    MaterialTheme {
        Column {
            ItemButton("Text")
            ItemButton("Text", enableItemColor = true)
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun DraggingItemPreview() {
    MaterialTheme {
        Column {
            DraggingItem("Text")
            DraggingItem("Text", dragOffset = Offset(20f, 20f))
        }
    }
}
