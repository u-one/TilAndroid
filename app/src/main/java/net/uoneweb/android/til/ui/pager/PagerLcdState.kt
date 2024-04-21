package net.uoneweb.android.til.ui.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Composable
fun rememberPagerLcdState(): PagerLcdState {
    val lcdState = remember { DotMatrixLcdStateImpl() }
    return remember { PagerLcdState(lcdState) }
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
       10000
       10000
       10000
       10000
       10000
       10000
       11111
    """
    )
    val chM = PagerChar.Alpha(
        PagerCode("38"), "M", """
       10001
       11011
       10101
       10101
       10001
       10001
       10001
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
    val chS = PagerChar.Alpha(
        PagerCode("30"), "S", """
       01110
       10001
       10000
       01110
       00001
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
    private val charset = setOf(emSmile, chA, chE, chG, chK, chL, chM, chO, chS, chT, chY)

    fun update(text: String) {
        dotMatrixLcdState.update(stringToChars(text))
    }

    private fun stringToChars(text: String): List<PagerChar> {
        return text.map {
            charset.find { ch -> ch.char == it.toString() }
        }.filterNotNull()
    }
}