package net.uoneweb.android.til.ui.buttons.draggablegrid

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
import androidx.compose.material.ButtonDefaults
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
import net.uoneweb.android.til.collection.MovableList
import net.uoneweb.android.til.ui.buttons.draggablegrid.internal.DebugInfo
import net.uoneweb.android.til.ui.buttons.draggablegrid.internal.DebugOverlay

@Stable
class DraggableGridState(
    list: List<Any>,
    val lazyGridState: LazyGridState,
    private val onListChanged: (List<Any>) -> Unit = {}
) {
    val tempList = MovableList(list.toMutableList())

    var draggingIndex: Int by mutableStateOf(-1)
        private set
    var dragOffset: Offset by mutableStateOf(Offset.Zero)
        private set

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
        val draggingItem = findItemInfo(draggingIndex) ?: return Offset.Zero
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

    private fun resetIndex() {
        draggingIndex = -1
        dragOffset = Offset.Zero
    }

    fun onDrag(dragAmount: Offset) {
        dragOffset += dragAmount

        val target = itemIndexUnderDrag()
        if (target == -1) return
        if (target != draggingIndex) {
            val index = tempList.move(draggingIndex, target)
            onListChanged(tempList.toList())
            onDragIndexChange(index)
        }
    }

    private fun onDragIndexChange(index: Int) {
        val prevItemInfo = findItemInfo(draggingIndex)
        val curItemInfo = findItemInfo(index)

        val prevOffset = prevItemInfo?.offset ?: IntOffset.Zero
        val curOffset = curItemInfo?.offset ?: return

        draggingIndex = index
        dragOffset += (prevOffset - curOffset).toOffset()
    }

    fun onDragEnd() {
        resetIndex()
    }

    fun onDragCancel() {
        resetIndex()
    }
}

@Composable
fun rememberDraggableGridState(
    list: List<Any>,
    onListChanged: (List<Any>) -> Unit
): DraggableGridState {
    val lazyGridState = rememberLazyGridState()
    return remember {
        DraggableGridState(
            list = list,
            lazyGridState = lazyGridState,
            onListChanged = onListChanged,
        )
    }
}

@Composable
fun DraggableGrid(
    list: List<Any>,
    onListChanged: (List<Any>) -> Unit = {},
    enableDebug: Boolean = false,
    itemContent: @Composable (index: Int, item: Any, dragging: Boolean, dragOffset: Offset) -> Unit
) {
    val draggableGridState = rememberDraggableGridState(list, onListChanged)

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
                        draggableGridState.onDragEnd()
                    },
                    onDragCancel = {
                        draggableGridState.onDragCancel()
                    },
                    onDrag = { change, dragAmount ->
                        draggableGridState.onDrag(dragAmount)
                        change.consume()
                    }
                )
            },
        ) {
            itemsIndexed(draggableGridState.tempList) { index, item ->
                val dragging = draggableGridState.draggingIndex == index
                val offset = if (dragging) {
                    draggableGridState.dragOffset
                } else {
                    Offset.Zero
                }
                itemContent(
                    index,
                    item,
                    (draggableGridState.draggingIndex == index),
                    offset
                )
            }
        }
        if (enableDebug) DebugInfo(draggableGridState)
    }
    if (enableDebug) DebugOverlay(draggableGridState)
}

@Preview(
    showBackground = true,
)
@Composable
private fun DraggableGridPreview() {
    val list = ('A'..'Z').map { it.toString() }
    MaterialTheme {
        DraggableGrid(list) { index: Int, item: Any, dragging: Boolean, dragOffset: Offset ->
            if (!dragging) {
                Box(modifier = Modifier.padding(4.dp)) {
                    Button(
                        modifier = Modifier.size(56.dp),
                        onClick = {})
                    {
                        Text(item as String)
                    }
                    Text(index.toString())
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Yellow)
                ) {
                    val density = LocalDensity.current
                    val offsetX = with(density) { dragOffset.x.toDp() }
                    val offsetY = with(density) { dragOffset.y.toDp() }
                    Button(
                        modifier = Modifier.size(56.dp).offset(offsetX, offsetY),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                        ),
                        onClick = {})
                    {
                        Text(item as String)
                    }
                }
            }
        }
    }
}
