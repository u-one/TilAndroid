package net.uoneweb.android.til.ui.buttons

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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize

@Stable
data class DraggableGridState(
    var draggingIndex: Int = -1,
    var offset: Offset = Offset.Zero,
    val lazyGridState: LazyGridState
) {
    fun draggingItem(): LazyGridItemInfo? {
        return lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            info.index == draggingIndex
        }
    }

    fun currentItemUnderOffset(): Int {
        val center = draggingItem()?.size?.center ?: IntOffset.Zero
        val offsetCenter = Offset(offset.x + center.x.toFloat(), offset.y + center.y.toFloat())
        val currentItem = lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            val rect = Rect(info.offset.toOffset(), info.size.toSize())
            rect.contains(offsetCenter)
        }
        return currentItem?.index ?: -1
    }

    fun currentItemsUnderOffset(): Pair<Int, Int>? {
        val center = draggingItem()?.size?.center ?: IntOffset.Zero
        val offsetCenter = Offset(offset.x + center.x.toFloat(), offset.y + center.y.toFloat())
        val currentItem = lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            val rect = Rect(info.offset.toOffset(), info.size.toSize())
            rect.contains(offsetCenter)
        }
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
        offset = Offset.Zero
    }

    fun onDrag(dragAmount: Offset) {
        offset += dragAmount
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
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { draggableGridState.onDraggingIndexChange(index) },
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
                    offset = draggableGridState.offset,
                    onClickItem = {
                    }
                )
            }
        }
        Text(
            style = MaterialTheme.typography.body1, text =
            "currentItemUnderDrag: ${draggableGridState.currentItemUnderOffset()}"
        )
        Text(
            style = MaterialTheme.typography.body1, text =
            "currentItemsUnderDrag: ${draggableGridState.currentItemsUnderOffset()}"
        )
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
