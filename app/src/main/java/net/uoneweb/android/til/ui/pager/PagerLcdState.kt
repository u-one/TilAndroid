package net.uoneweb.android.til.ui.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import net.uoneweb.android.til.ui.pager.chars.AlphabetPagerChars
import net.uoneweb.android.til.ui.pager.chars.KanaPagerChars
import net.uoneweb.android.til.ui.pager.chars.LargeChars
import net.uoneweb.android.til.ui.pager.chars.NumberPagerChars
import net.uoneweb.android.til.ui.pager.chars.PagerChar
import net.uoneweb.android.til.ui.pager.chars.PagerCode
import net.uoneweb.android.til.ui.pager.chars.SpecialPagerChars

@Composable
fun rememberPagerLcdState(): PagerLcdState {
    val lcdState = remember { DotMatrixLcdStateImpl() }
    return remember { PagerLcdState(lcdState) }
}

@Stable
class PagerLcdState(val dotMatrixLcdState: DotMatrixLcdState) {
    val ctrlBegin =
        PagerChar.Control(
            PagerCode("*2*2"),
            "*2*2",
        )

    private val charset = mutableSetOf<PagerChar>(ctrlBegin)

    init {
        charset.addAll(KanaPagerChars.set)
        charset.addAll(AlphabetPagerChars.set)
        charset.addAll(SpecialPagerChars.set)
        charset.addAll(LargeChars.set)
        charset.addAll(NumberPagerChars.set)
    }

    fun updateWithText(text: String) {
        dotMatrixLcdState.update(stringToChars(text))
    }

    fun update(text: String) {
        // dotMatrixLcdState.update(stringToChars(text))
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
            val pagerChar =
                charset.find {
                    code.startsWith(it.code.value, startIndex, ignoreCase = true)
                } ?: NumberPagerChars.set.find {
                    it.char ==
                        code.substring(
                            startIndex,
                            startIndex + 1,
                        )
                }
                    ?: PagerChar.Control(PagerCode(code.substring(startIndex, startIndex + 1)), "?")
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
