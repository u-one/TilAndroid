package net.uoneweb.android.til.ui.pager

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.pager.chars.PagerChar
import net.uoneweb.android.til.ui.pager.chars.PagerCode

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
            if (chars.find { it is PagerChar.LargeChar || it is PagerChar.Emoji } != null) {
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
    val density = LocalDensity.current
    val dotSize =
        with(density) {
            Size(5.dp.toPx(), 5.dp.toPx())
        }
    val dotMargin =
        with(density) {
            0.4.dp.toPx()
        }
    val offset =
        with(density) {
            Offset(10.dp.toPx(), 10.dp.toPx())
        }

    Canvas(
        modifier = modifier.fillMaxSize(),
    ) {
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
            if (chars.find { it is PagerChar.LargeChar || it is PagerChar.Emoji } != null) {
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
    val chA =
        PagerChar.Alpha(
            PagerCode("16"),
            "A",
            """
       01110
       10001
       10001
       11111
       10001
       10001
       10001
    """,
        )
    val state = PreviewStateImpl()
    state.update(listOf(chA, chA, chA, chA))
    DotMatrixLcd(
        state,
        Modifier
            .fillMaxSize()
            .background(Color(0xFF446644)),
    )
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
private fun DotMatrixLcdLargePreview() {
    val ch =
        PagerChar.Emoji(
            PagerCode("1*2"),
            "Smile",
            """
            00011111000
            00100000100
            01000000010
            10000000001
            10010001001
            10101010101
            10000000001
            10000000001
            10111111101
            10100000101
            10100000101
            10010001001
            01001110010
            00100000100
            00011111000
            """,
        )
    val state = PreviewStateImpl()
    state.update(listOf(ch, ch, ch, ch))
    DotMatrixLcd(
        state,
        Modifier
            .fillMaxSize()
            .background(Color(0xFF446644)),
    )
}
