package net.uoneweb.android.til.ui.pager

import android.util.Log
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
    val chSp = PagerChar.Special(
        PagerCode("88"), " ", """
         00000
         00000
         00000
         00000
         00000
         00000
         00000
    """
    )
    val ctrlBegin = PagerChar.Control(
        PagerCode("*2*2"), "*2*2"
    )

    private val numbers = mutableSetOf<PagerChar>()

    private val charset =
        mutableSetOf(emSmile, chA, chE, chG, chK, chL, chM, chO, chS, chT, chY, chSp, ctrlBegin)

    init {
        numbers.add(
            PagerChar.Number(
                PagerCode("0"), "0", """
         01110
         10001
         10001
         10101
         10001
         10001
         01110
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("1"), "1", """
         01100
         00100
         00100
         00100
         00100
         00100
         11111
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("2"), "2", """
         01110
         10001
         00010
         00100
         01000
         10000
         11111
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("3"), "3", """
         01110
         10001
         00001
         00110
         00001
         10001
         01110
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("4"), "4", """
         00010
         00110
         01010
         10010
         11111
         00010
         00010
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("5"), "5", """
         11111
         10000
         10000
         11110
         00001
         00001
         11110
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("6"), "6", """
         01110
         10000
         10000
         11110
         10001
         10001
         01110
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("7"), "7", """
         11111
         10001
         00010
         00100
         00100
         00100
         00100
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("8"), "8", """
         01110
         10001
         10001
         01110
         10001
         10001
         01110
            """
            )
        )
        numbers.add(
            PagerChar.Number(
                PagerCode("9"), "9", """
         01110
         10001
         10001
         01110
         00001
         00001
         01110
            """
            )
        )
    }

    fun updateWithText(text: String) {
        dotMatrixLcdState.update(stringToChars(text))
    }


    fun update(text: String) {
        //dotMatrixLcdState.update(stringToChars(text))
        dotMatrixLcdState.update(decode(text))

    }

    private fun stringToChars(text: String): List<PagerChar> {
        return text.map {
            charset.find { ch -> ch.char == it.toString() }
        }.filterNotNull()
    }

    private fun decode(code: String): List<PagerChar> {
        var startIndex = 0
        val chars = mutableListOf<PagerChar>()
        while (startIndex < code.length) {
            val pagerChar = charset.find {
                code.startsWith(it.code.value, startIndex, ignoreCase = true)
            } ?: numbers.find { it.char == code.substring(startIndex, startIndex + 1) }
            ?: PagerChar.Control(PagerCode(code.substring(startIndex, startIndex + 1)), "")
            Log.d("PagerLcdState", "decode: ${pagerChar.code}")
            if (pagerChar == ctrlBegin) {
                chars.clear()
            } else {
                chars.add(pagerChar)
            }
            startIndex += pagerChar.code.value.length
        }
        return chars
    }
}