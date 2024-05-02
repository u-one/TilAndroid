package net.uoneweb.android.til.ui.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import net.uoneweb.android.til.ui.pager.chars.PagerChar
import net.uoneweb.android.til.ui.pager.chars.PagerDecoder

@Composable
fun rememberPagerLcdState(): PagerLcdState {
    val lcdState = remember { DotMatrixLcdStateImpl() }
    return remember { PagerLcdState(lcdState) }
}

@Stable
class PagerLcdState(val dotMatrixLcdState: DotMatrixLcdState) {
    private val decoder = PagerDecoder()

    fun updateWithText(text: String) {
        dotMatrixLcdState.update(decoder.fromText(text))
    }

    fun update(text: String) {
        dotMatrixLcdState.update(decode(text))
    }

    private fun decode(code: String): List<PagerChar> {
        return decoder.decode(code)
    }
}
