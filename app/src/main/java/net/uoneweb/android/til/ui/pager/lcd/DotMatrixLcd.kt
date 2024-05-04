package net.uoneweb.android.til.ui.pager.lcd

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.pager.chars.PagerChar
import net.uoneweb.android.til.ui.pager.chars.PagerChars
import kotlin.math.min

@Stable
interface DotMatrixLcdState {
    val dotWidth: Int
    val dotHeight: Int
    var data: MutableState<IntArray>

    fun update(chars: List<PagerChar>) {}

    fun update(input: IntArray) {
        data.value = input.copyInto(IntArray(dotWidth * dotHeight) { 0 }, 0, 0, input.size)
    }
}

class DotMatrixLcdStateImpl : DotMatrixLcdState {
    override val dotWidth: Int = 80
    override val dotHeight: Int = 15
    override var data = mutableStateOf(IntArray(dotWidth * dotHeight) { 0 })
    private val singleLineCharacterLcdBuffer =
        SingleLineCharacterLcdBuffer(
            displayWidth = dotWidth,
            displayHeight = dotHeight,
            charMargin = 1,
        )
    private val twoLineCharacterLcdBuffer =
        TwoLineCharacterLcdBuffer(
            displayWidth = dotWidth,
            displayHeight = dotHeight,
            charWidth = 5,
            charHeight = 7,
            charMargin = 1,
        )

    override fun update(chars: List<PagerChar>) {
        val data =
            if (chars.find { it is PagerChar.LargeChar } != null) {
                singleLineCharacterLcdBuffer.reset()
                chars.forEach {
                    singleLineCharacterLcdBuffer.draw(LcdBitmap(data = it.charData, size = it.size))
                }
                singleLineCharacterLcdBuffer.buffer
            } else {
                twoLineCharacterLcdBuffer.reset()
                chars.forEach {
                    twoLineCharacterLcdBuffer.draw(LcdBitmap(data = it.charData, size = it.size))
                }
                twoLineCharacterLcdBuffer.buffer
            }
        update(data)
    }
}

@Composable
fun DotMatrixLcd(
    state: DotMatrixLcdState,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier =
            modifier
                .layout { measurables, constraints ->
                    val dotMargin = 0.4.dp.toPx().toInt()
                    val placeable =
                        if (constraints.maxWidth < constraints.maxHeight) {
                            val pixelSizeX = constraints.maxWidth / (state.dotWidth + 5 + 5)
                            val height = pixelSizeX * (state.dotHeight + 5) + dotMargin * (state.dotHeight - 1)
                            measurables.measure(
                                constraints.copy(
                                    minHeight = height,
                                    maxHeight = height,
                                ),
                            )
                        } else {
                            val pixelSizeY = constraints.maxHeight / (state.dotHeight + 5)
                            val width = pixelSizeY * (state.dotWidth + 5 + 5) + dotMargin * (state.dotWidth - 1)
                            measurables.measure(
                                constraints.copy(
                                    minWidth = width,
                                    maxWidth = width,
                                ),
                            )
                        }

                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                },
    ) {
        val pixelSizeX = size.width / (state.dotWidth + 5 + 5)
        val pixelSizeY = size.height / (state.dotHeight + 5)
        val pixelSize = min(pixelSizeX, pixelSizeY)
        val dotMargin = 0.4.dp.toPx().toInt()
        val dotSize = Size(pixelSize - dotMargin, pixelSize - dotMargin)

        val offset = Offset(pixelSize * 1, pixelSize * 1)

        (0 until state.dotHeight).forEach { y ->
            (0 until state.dotWidth).forEach { x ->
                val index = x + state.dotWidth * y
                val color =
                    if (state.data.value[index] != 0) {
                        Color(0xFF222244)
                    } else {
                        Color(0x44222244)
                    }

                val topLeft =
                    offset +
                        Offset(
                            x * (dotSize.width + dotMargin),
                            y * (dotSize.height + dotMargin),
                        )
                val rect = Rect(topLeft, dotSize)
                if (size.toRect().contains(rect)) {
                    drawRect(
                        topLeft = topLeft,
                        size = dotSize,
                        color = color,
                    )
                }
            }
        }
    }
}

private fun Rect.contains(other: Rect): Boolean {
    return this.left <= other.left &&
        this.top <= other.top &&
        this.right >= other.right &&
        this.bottom >= other.bottom
}

private class PreviewStateImpl : DotMatrixLcdState {
    override val dotWidth: Int = 11
    override val dotHeight: Int = 15

    private val singleLineCharacterLcdBuffer =
        SingleLineCharacterLcdBuffer(
            displayWidth = dotWidth,
            displayHeight = dotHeight,
            charMargin = 1,
        )

    val twoLineCharacterLcdBuffer =
        TwoLineCharacterLcdBuffer(
            displayWidth = dotWidth,
            displayHeight = dotHeight,
            charWidth = 5,
            charHeight = 7,
            charMargin = 1,
        )

    override var data = mutableStateOf(twoLineCharacterLcdBuffer.buffer)

    override fun update(chars: List<PagerChar>) {
        val data =
            if (chars.find { it is PagerChar.LargeChar } != null) {
                singleLineCharacterLcdBuffer.reset()
                chars.forEach {
                    singleLineCharacterLcdBuffer.draw(LcdBitmap(data = it.charData, size = it.size))
                }
                singleLineCharacterLcdBuffer.buffer
            } else {
                twoLineCharacterLcdBuffer.reset()
                chars.forEach {
                    twoLineCharacterLcdBuffer.draw(LcdBitmap(data = it.charData, size = it.size))
                }
                twoLineCharacterLcdBuffer.buffer
            }
        update(data)
    }
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
private fun DotMatrixLcdPreview() {
    val state = PreviewStateImpl()
    state.update(listOf(PagerChars.AlphaA, PagerChars.AlphaA, PagerChars.AlphaA, PagerChars.AlphaA))
    DotMatrixLcd(
        state,
        Modifier
            .background(Color(0xFF446644)),
    )
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
private fun DotMatrixLcdWithControlCharPreview() {
    val state = PreviewStateImpl()
    state.update(listOf(PagerChars.CtrlBeginFreeWord, PagerChars.AlphaA, PagerChars.AlphaA, PagerChars.AlphaA))
    DotMatrixLcd(
        state,
        Modifier
            .background(Color(0xFF446644)),
    )
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
private fun DotMatrixLcdLargePreview() {
    val state = PreviewStateImpl()
    state.update(listOf(PagerChars.IconHappy, PagerChars.IconHappy, PagerChars.IconHappy, PagerChars.IconHappy))
    DotMatrixLcd(
        state,
        Modifier
            .fillMaxSize()
            .background(Color(0xFF446644)),
    )
}
