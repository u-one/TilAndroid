package net.uoneweb.android.til.ui.buttons

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.plus
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import net.uoneweb.android.til.collection.MovableList
import java.security.MessageDigest

@Stable
class DraggableGridState(
    val lazyGridState: LazyGridState
) {
    var draggingIndex: Int by mutableStateOf(-1)
        private set
    var dragOffset: Offset by mutableStateOf(Offset.Zero)
        private set

    fun draggingItem(): LazyGridItemInfo? {
        return findItemInfo(draggingIndex)
    }

    private fun findItemInfo(index: Int): LazyGridItemInfo? {
        return lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            info.index == index
        }
    }

    fun itemIndexUnderDrag(): Int {
        return itemUnderDrag()?.index ?: -1
    }

    private fun itemUnderDrag(): LazyGridItemInfo? {
        val draggingCenter = draggingCenter()
        if (draggingCenter == Offset.Zero) return null
        val currentItem = lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            val rect = Rect(info.offset.toOffset(), info.size.toSize())
            rect.contains(draggingCenter)
        }
        return currentItem
    }

    /**
     * Absolute position of the center of dragging item.
     */
    internal fun draggingCenter(): Offset {
        val draggingItem = draggingItem() ?: return Offset.Zero
        val center = draggingItem.size.center
        return dragOffset + draggingItem.offset + center.toOffset()
    }

    fun onDragStart(offset: Offset) {
        val itemDragging = lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            val rect = Rect(info.offset.toOffset(), info.size.toSize())
            rect.contains(offset)
        }
        draggingIndex = itemDragging?.index ?: -1
    }

    fun onDragIndexChange(index: Int) {
        val prevItemInfo = findItemInfo(draggingIndex)
        val curItemInfo = findItemInfo(index)

        val prevOffset = prevItemInfo?.offset ?: IntOffset.Zero
        val curOffset = curItemInfo?.offset ?: return

        draggingIndex = index
        dragOffset += (prevOffset - curOffset).toOffset()
    }

    fun resetIndex() {
        draggingIndex = -1
        dragOffset = Offset.Zero
    }

    fun onDrag(dragAmount: Offset) {
        dragOffset += dragAmount
    }

}

@Composable
fun rememberDraggableGridState(): DraggableGridState {
    val lazyGridState = rememberLazyGridState()
    return remember {
        DraggableGridState(
            lazyGridState = lazyGridState
        )
    }
}

@Composable
fun DraggableGrid() {
    // A list of characters from A to Z
    val list = ('A'..'Z').map { it.toString() }

    val draggableGridState = rememberDraggableGridState()

    val tempList = remember {
        MovableList(list.toMutableList())
    }

    LaunchedEffect(draggableGridState.itemIndexUnderDrag()) {
        val target = draggableGridState.itemIndexUnderDrag()
        if (target == -1) return@LaunchedEffect
        if (target == draggableGridState.draggingIndex) {
            return@LaunchedEffect
        }
        val index = tempList.move(draggableGridState.draggingIndex, target)
        draggableGridState.onDragIndexChange(index)
    }

    Column {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(64.dp),
            state = draggableGridState.lazyGridState,
            userScrollEnabled = false, // needed to start dragging vertically
            modifier = Modifier.pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        draggableGridState.onDragStart(it)
                    },
                    onDragEnd = {
                        draggableGridState.resetIndex()
                    },
                    onDragCancel = {
                        draggableGridState.resetIndex()
                    },
                    onDrag = { change, dragAmount ->
                        draggableGridState.onDrag(dragAmount)
                        change.consume()
                    }
                )
            }
        ) {
            itemsIndexed(tempList) { index, item ->
                ItemButton(
                    item,
                    Modifier
                        .background(bgColor(draggableGridState, index)),
                    index,
                    isDragging = (draggableGridState.draggingIndex == index),
                    offset = draggableGridState.dragOffset,
                    onClickItem = {
                        val itemsInfo = draggableGridState.lazyGridState.layoutInfo.visibleItemsInfo
                        itemsInfo.forEach {
                            // 内容をすべてログ出力
                            Log.d(
                                "DraggableGrid",
                                "index: ${it.index}, offset: ${it.offset}, size: ${it.size}"
                            )
                        }
                    }
                )
            }
        }
        DebugInfo(draggableGridState)
    }
    DebugOverlay(draggableGridState)
}

@Composable
private fun DebugInfo(draggableGridState: DraggableGridState) {
    Text(
        style = MaterialTheme.typography.body2, text =
        "draggingIndex: ${draggableGridState.draggingIndex}"
    )
    Text(
        style = MaterialTheme.typography.body2, text =
        "draggingCenter: ${draggableGridState.draggingCenter()}"
    )
    Text(
        style = MaterialTheme.typography.body2, text =
        "indexUnderDrag: ${draggableGridState.itemIndexUnderDrag()}"
    )
    draggableGridState.draggingItem()?.let {
        LazyGridItemInfo(it)
    }
}

@Composable
private fun LazyGridItemInfo(info: LazyGridItemInfo) {
    Column {
        Text(
            style = MaterialTheme.typography.body1, text =
            "draggingItem:"
        )
        Text(
            style = MaterialTheme.typography.body2, text =
            "index: ${info.index}"
        )
        Text(
            style = MaterialTheme.typography.body2, text =
            "offset: ${info.offset}"
        )
        Text(
            style = MaterialTheme.typography.body2, text =
            "size: ${info.size}"
        )
    }
}

@Composable
private fun DebugOverlay(draggableGridState: DraggableGridState) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(color = Color.Red, radius = 16f, center = draggableGridState.draggingCenter())
    }
}

private fun bgColor(draggableGridState: DraggableGridState, index: Int): Color {
    val target = draggableGridState.itemIndexUnderDrag()
    return if (draggableGridState.draggingIndex == index) {
        Color.Green
    } else if (target != -1 && target == index) {
        Color.Yellow
    } else {
        Color.White
    }
}


@Composable
private fun ItemButton(
    item: String,
    modifier: Modifier = Modifier,
    index: Int = 0,
    onClickItem: (item: String) -> Unit = {},
    isDragging: Boolean = false,
    offset: Offset = Offset(0f, 0f)
) {
    val itemColor = remember(item) {
        val hash = item.toSHA256ToInt()
        Color(hash or 0xFF000000.toInt())
    }
    val applyItemColor = true

    Box(
        modifier = modifier
            .size(64.dp)
            .padding(8.dp)
    ) {
        if (!isDragging) {
            Button(
                modifier = Modifier.size(56.dp),
                colors = if (applyItemColor) {
                    ButtonDefaults.buttonColors(
                        backgroundColor = itemColor,
                    )
                } else {
                    ButtonDefaults.buttonColors()
                },
                onClick = { onClickItem(item) })
            {
                Text(item)
            }
        } else {
            val density = LocalDensity.current
            val offsetX = with(density) { offset.x.toDp() }
            val offsetY = with(density) { offset.y.toDp() }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .offset(offsetX, offsetY)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(item)
            }
        }
        Text(index.toString())

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
private fun ItemButtonPreview() {
    MaterialTheme {
        Column {
            ItemButton("Text")
            ItemButton("Text", isDragging = true)
            ItemButton("Text", isDragging = true, offset = Offset(10f, 10f))
        }

    }
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
