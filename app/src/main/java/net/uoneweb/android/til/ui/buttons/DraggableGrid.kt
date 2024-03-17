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
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize

@Composable
fun DraggableGrid() {
    val list = listOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
    )


    var draggingIndex by remember { mutableStateOf(-1) }
    var offset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val lazyGridState = rememberLazyGridState()
    val draggingItem = remember(lazyGridState, draggingIndex) {
        lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            info.index == draggingIndex
        }
    }

    val currentItemUnderDragIndex = rememberCurrentItemOnOffset(
        lazyGridState = lazyGridState,
        offset = offset,
        draggingIndex = draggingIndex,
        draggingItem = draggingItem
    )

    val temp = rememberCurrentItemsUnderOffset(
        lazyGridState = lazyGridState,
        offset = offset,
        draggingIndex = draggingIndex,
        draggingItem = draggingItem
    )


    Column {

        LazyVerticalGrid(columns = GridCells.Adaptive(64.dp), state = lazyGridState) {
            itemsIndexed(list) { index, item ->
                ItemButton(
                    item,
                    Modifier
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { draggingIndex = index },
                                onDragEnd = {
                                    draggingIndex = -1
                                    offset = Offset(0f, 0f)
                                },
                                onDragCancel = {
                                    draggingIndex = -1
                                    offset = Offset(0f, 0f)
                                },
                                onDrag = { change, dragAmount ->
                                    offset += dragAmount
                                    change.consume()
                                }
                            )
                        },
                    isDragging = (draggingIndex == index),
                    offset = offset,
                    onClickItem = {
                        Log.d(
                            "Button",
                            "totalItemsCount: ${lazyGridState.layoutInfo.totalItemsCount} " +
                                    "afterContentPadding: ${lazyGridState.layoutInfo.afterContentPadding} " +
                                    "beforeContentPadding: ${lazyGridState.layoutInfo.beforeContentPadding} "
                        )

                        lazyGridState.layoutInfo.visibleItemsInfo.forEach { info ->
                            //infoのすべてのプロパティをログに出力
                            Log.d(
                                "Button",
                                "offset: ${info.offset} size: ${info.size} index:${info.index} row:${info.row} column:${info.column} ke:${info.key}"
                            )
                        }
                    }
                )
            }
        }
        Text(
            style = MaterialTheme.typography.body1, text =
            "currentItemUnderDrag: $currentItemUnderDragIndex"
        )
        Text(
            style = MaterialTheme.typography.body1, text =
            "currentItemsUnderDrag: $temp"
        )
    }
}

@Composable
fun rememberCurrentItemOnOffset(
    lazyGridState: LazyGridState,
    offset: Offset,
    draggingIndex: Int,
    draggingItem: LazyGridItemInfo?
): Int {
    return remember(lazyGridState, offset, draggingIndex) {
        val center = draggingItem?.size?.center ?: IntOffset.Zero
        val offsetCenter = Offset(offset.x + center.x.toFloat(), offset.y + center.y.toFloat())
        val currentItem = lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            val rect = Rect(info.offset.toOffset(), info.size.toSize())
            rect.contains(offsetCenter)
        }
        currentItem?.index ?: -1
    }
}

@Composable
fun rememberCurrentItemsUnderOffset(
    lazyGridState: LazyGridState,
    offset: Offset,
    draggingIndex: Int,
    draggingItem: LazyGridItemInfo?
): Pair<Int, Int>? {
    return remember(lazyGridState, offset, draggingIndex) {
        val center = draggingItem?.size?.center ?: IntOffset.Zero
        val offsetCenter = Offset(offset.x + center.x.toFloat(), offset.y + center.y.toFloat())
        val currentItem = lazyGridState.layoutInfo.visibleItemsInfo.find { info ->
            val rect = Rect(info.offset.toOffset(), info.size.toSize())
            rect.contains(offsetCenter)
        }
        currentItem?.let {
            val currentItemCenter =
                Rect(currentItem.offset.toOffset(), currentItem.size.toSize()).center
            if (offsetCenter.x > currentItemCenter.x) {
                Pair(currentItem.index, currentItem.index + 1)
            } else {
                Pair(currentItem.index - 1, currentItem.index)
            }
        }
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
