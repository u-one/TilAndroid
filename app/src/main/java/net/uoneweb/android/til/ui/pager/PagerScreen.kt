package net.uoneweb.android.til.ui.pager

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PagerScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PagerLcd(Modifier.height(160.dp))
    }
}

@Stable
class PagerLcdState(val dotMatrixLcdState: DotMatrixLcdState) {
    val emSmile = PagerChar.Emoji(PagerCode("1*2"))
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

    val chE = PagerChar.Alpha(
        PagerCode("10"), "E", """
       11111
       10000
       10000
       11110
       10000
       10000
       11111
    """
    )
    val chG = PagerChar.Alpha(
        PagerCode("27"), "G", """
       01110
       10001
       10000
       10111
       10001
       10001
       01111
    """
    )
    val chK = PagerChar.Alpha(
        PagerCode("36"), "K", """
       10001
       10010
       10100
       11000
       10100
       10010
       10001
    """
    )
    val chL = PagerChar.Alpha(
        PagerCode("37"), "L", """
       00000
       00000
       00000
       00000
       00000
       00000
       00000
    """
    )
    val chM = PagerChar.Alpha(
        PagerCode("38"), "M", """
       00000
       00000
       00000
       00000
       00000
       00000
       00000
    """
    )
    val chO = PagerChar.Alpha(
        PagerCode("30"), "O", """
       01110
       10001
       10001
       10001
       10001
       10001
       01110
    """
    )
    val chT = PagerChar.Alpha(
        PagerCode("40"), "T", """
       11111
       00100
       00100
       00100
       00100
       00100
       00100
    """
    )
    val chY = PagerChar.Alpha(
        PagerCode("50"), "Y", """
       10001
       10001
       01010
       00100
       00100
       00100
       00100
    """
    )
    private val charset = setOf(emSmile, chA, chE, chG, chK, chL, chM, chO, chT, chY)

    fun update(text: String) {
        dotMatrixLcdState.update(stringToChars(text))
    }

    private fun stringToChars(text: String): List<PagerChar> {
        return text.map {
            charset.find { ch -> ch.char == it.toString() }
        }.filterNotNull()
    }
}

@Composable
private fun rememberPagerLcdState(): PagerLcdState {
    val lcdState = remember { DotMatrixLcdStateImpl() }
    return remember { PagerLcdState(lcdState) }
}

@Composable
fun PagerLcd(modifier: Modifier = Modifier) {
    val state = rememberPagerLcdState()
    val str1 = "TOKYO"
    val str2 = "TELEMESSAGE"

    LaunchedEffect(Unit) {
        state.update(str1)
    }
    DotMatrixLcd(state.dotMatrixLcdState, modifier)
}

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

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
fun PagerLcdPreview() {
    PagerLcd()
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
fun DotMatrixLcdPreview() {
    DotMatrixLcd(DotMatrixLcdStateImpl(intArrayOf(0, 1, 0, 1, 1)), Modifier.fillMaxSize())
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
fun PagerScreenPreview() {
    PagerScreen()
}