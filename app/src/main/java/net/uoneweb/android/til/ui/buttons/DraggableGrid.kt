package net.uoneweb.android.til.ui.buttons

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@Stable
class DraggableGridState(
    val lazyGridState: LazyGridState
) {
    var draggingIndex: Int by mutableStateOf(-1)
        private set
    var dragOffset: Offset by mutableStateOf(Offset.Zero)
        private set

    fun draggingItem(): LazyGridItemInfo? {
        return lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            info.index == draggingIndex
        }
    }

    fun itemIndexUnderDrag(): Int {
        return itemUnderDrag()?.index ?: -1
    }

    private fun itemUnderDrag(): LazyGridItemInfo? {
        val currentItem = lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            val rect = Rect(info.offset.toOffset(), info.size.toSize())
            rect.contains(offsetCenter())
        }
        return currentItem
    }

    /**
     * Absolute position of the center of dragging item.
     */
    fun offsetCenter(): Offset {
        val draggingItem = draggingItem()
        val center = draggingItem?.size?.center ?: IntOffset.Zero
        val centerOffset =
            Offset(dragOffset.x + center.x.toFloat(), dragOffset.y + center.y.toFloat())
        return draggingItem?.let { it.offset + centerOffset } ?: Offset.Zero
    }

    fun currentItemsUnderOffset(): Pair<Int, Int>? {
        val currentItem = itemUnderDrag()
        val offsetCenter = offsetCenter()
        return currentItem?.let {
            val currentItemCenter =
                Rect(currentItem.offset.toOffset(), currentItem.size.toSize()).center
            if (offsetCenter.x > currentItemCenter.x) {
                Pair(currentItem.index, currentItem.index + 1)
            } else {
                Pair(currentItem.index - 1, currentItem.index)
            }
        }
    }

    fun onDraggingIndexChange(index: Int) {
        draggingIndex = index
    }

    fun resetIndex() {
        draggingIndex = -1
        dragOffset = Offset.Zero
    }

    fun onDrag(dragAmount: Offset) {
        dragOffset = dragOffset + dragAmount
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

    Column {

        LazyVerticalGrid(
            columns = GridCells.Adaptive(64.dp),
            state = draggableGridState.lazyGridState
        ) {
            itemsIndexed(list) { index, item ->
                ItemButton(
                    item,
                    Modifier
                        .background(bgColor(draggableGridState, index))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    draggableGridState.onDraggingIndexChange(index)
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
                        },
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
}

@Composable
private fun DebugInfo(draggableGridState: DraggableGridState) {
    Text(
        style = MaterialTheme.typography.body2, text =
        "draggingIndex: ${draggableGridState.draggingIndex}"
    )
    Text(
        style = MaterialTheme.typography.body2, text =
        "offsetCenter: ${draggableGridState.offsetCenter()}"
    )
    Text(
        style = MaterialTheme.typography.body2, text =
        "indexUnderDrag: ${draggableGridState.itemIndexUnderDrag()}"
    )
    Text(
        style = MaterialTheme.typography.body2, text =
        "currentItemsUnderOffset: ${draggableGridState.currentItemsUnderOffset()}"
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

private fun bgColor(draggableGridState: DraggableGridState, index: Int): Color {
    val indice = draggableGridState.currentItemsUnderOffset()
    return if (draggableGridState.draggingIndex == index) {
        Color.Green
    } else if (indice?.first == index || indice?.second == index) {
        Color.Yellow
    } else {
        Color.White
    }
}


@Composable
private fun ItemButton(
    item: String,
    modifier: Modifier = Modifier,
    onClickItem: (item: String) -> Unit = {},
    isDragging: Boolean = false,
    offset: Offset = Offset(0f, 0f)
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .padding(8.dp)
    ) {
        if (!isDragging) {
            Button(modifier = Modifier.size(56.dp), onClick = { onClickItem(item) }) {
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
