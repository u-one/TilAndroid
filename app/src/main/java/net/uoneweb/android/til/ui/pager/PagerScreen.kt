package net.uoneweb.android.til.ui.pager

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PagerScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PagerLcd()
    }
}

@Stable
class PagerLcdState(val dotMatrixLcdState: State<DotMatrixLcdState>) {
    val ch = PagerChar.Emoji(PagerCode("1*2"))
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

    /*
    val chE = PagerChar.Alpha(PagerCode("10"), "E")
    val chG = PagerChar.Alpha(PagerCode("27"), "G")
    val chK = PagerChar.Alpha(PagerCode("36"), "K")
    val chL = PagerChar.Alpha(PagerCode("37"), "L")
    val chM = PagerChar.Alpha(PagerCode("38"), "M")
    val chO = PagerChar.Alpha(PagerCode("30"), "O")
    val chT = PagerChar.Alpha(PagerCode("40"), "T")
    val chY = PagerChar.Alpha(PagerCode("50"), "Y")
     */
    fun update(text: String) {
        dotMatrixLcdState.value.update(listOf(chA))
    }
}

@Composable
private fun rememberPagerLcdState(): State<PagerLcdState> {
    val lcdState = remember { mutableStateOf(DotMatrixLcdStateImpl()) }
    return remember { mutableStateOf(PagerLcdState(lcdState)) }
}

@Composable
fun PagerLcd() {
    val state = rememberPagerLcdState()
    val str1 = "TOKYO"
    val str2 = "TELEMESSAGE"

    LaunchedEffect(Unit) {
        state.value.update(str1)
    }
    DotMatrixLcd(state.value.dotMatrixLcdState.value)
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
        chars.forEach {
            val ch = chars[0]
            for (y in 0 until ch.size.height) {
                val startIndex = ch.size.width * y
                ch.charData.copyInto(data, dotWidth * y, startIndex, startIndex + ch.size.width)
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
                val topLeft = offset + Offset(x * 10f, y * 10f)
                val dotSize = Size(8f, 8f)
                val rect = Rect(topLeft, dotSize)
                if (size.toRect().contains(rect)) {
                    drawRect(
                        topLeft = topLeft,
                        size = Size(8f, 8f),
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