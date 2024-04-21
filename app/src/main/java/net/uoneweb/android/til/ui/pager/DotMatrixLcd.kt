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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

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

class DotMatrixLcdStateImpl(initialData: IntArray = intArrayOf()) : DotMatrixLcdState {
    override val dotWidth: Int = 80
    override val dotHeight: Int = 15
    override var data = mutableStateOf(
        initialData.copyInto(IntArray(dotWidth * dotHeight) { 0 }, 0, 0, initialData.size)
    )

    override fun update(chars: List<PagerChar>) {
        val data = IntArray(dotWidth * dotHeight) { 0 }
        val charMarginWidth = 1

        // 一番上のドット行ごとに文字を描画
        // TODO 2次元的にコピーしてからシリアライズしたほうがやりやすそう
        for (y in 0 until dotHeight) {
            var destinationOffset = dotWidth * y
            chars.forEachIndexed() { index, ch ->
                if (y >= ch.size.height) return@forEachIndexed

                val startIndex = ch.size.width * y
                val endIndex = startIndex + ch.size.width
                ch.charData.copyInto(data, destinationOffset, startIndex, endIndex)
                destinationOffset += ch.size.width + charMarginWidth
            }
        }
        update(data)
    }
}

@Composable
fun DotMatrixLcd(
    state: DotMatrixLcdState,
    modifier: Modifier = Modifier
) {

    val density = LocalDensity.current
    val dotSize = with(density) {
        //Size(12f, 12f)
        Size(6.dp.toPx(), 6.dp.toPx())
    }
    val dotMargin = with(density) {
        // 2f
        0.5.dp.toPx()
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF446644))
    ) {
        val offset = Offset(10f, 10f)
        (0 until state.dotHeight).forEach { y ->
            (0 until state.dotWidth).forEach { x ->
                val index = x + state.dotWidth * y
                val color = if (state.data.value[index] != 0) {
                    Color(0xFF222244)
                } else {
                    Color(0x44222244)
                }

                val topLeft =
                    offset + Offset(
                        x * (dotSize.width + dotMargin),
                        y * (dotSize.height + dotMargin)
                    )
                val rect = Rect(topLeft, dotSize)
                if (size.toRect().contains(rect)) {
                    drawRect(
                        topLeft = topLeft,
                        size = dotSize,
                        color = color
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

private class LcdBitmap(val data: IntArray, val size: IntSize)

private class PreviewStateImpl : DotMatrixLcdState {
    override val dotWidth: Int = 11
    override val dotHeight: Int = 15
    val charWidth = 5
    val charHeight = 7
    val charMargin = 1
    val maxCharCountInLine = 2

    override var data = mutableStateOf(IntArray(dotWidth * dotHeight) { 0 })

    private var currentPos = 0

    override fun update(chars: List<PagerChar>) {
        val buffer = IntArray(dotWidth * dotHeight) { 0 }
        chars.forEach { char ->
            val bitmap = LcdBitmap(char.charData, char.size)
            transfer(buffer, bitmap)
        }
        update(buffer)
    }

    private fun transfer(buffer: IntArray, initialData: LcdBitmap): IntArray {
        for (y in 0 until initialData.size.height) {
            val orgIndex = initialData.size.width * y
            val dstOffset = dotWidth * y + currentOffset(0, y)
            initialData.data.copyInto(
                buffer,
                dstOffset,
                orgIndex,
                orgIndex + initialData.size.width
            )
        }
        currentPos++
        return buffer
    }

    private fun currentOffset(x: Int, y: Int): Int {
        return if (currentPos < maxCharCountInLine) {
            0 + (charWidth + charMargin) * currentPos
        } else {
            val secondLineOffset = (dotWidth) * (charHeight + charMargin)
            secondLineOffset + (charWidth + charMargin) * (currentPos - maxCharCountInLine)
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
private fun DotMatrixLcdPreview() {
    val chA = PagerChar.Alpha(
        PagerCode("16"), "A",
        """
       01110
       10001
       10001
       11111
       10001
       10001
       10001
    """
    )
    val state = PreviewStateImpl()
    state.update(listOf(chA, chA, chA, chA))
    DotMatrixLcd(state, Modifier.fillMaxSize())
}

