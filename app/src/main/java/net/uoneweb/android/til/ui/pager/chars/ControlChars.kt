package net.uoneweb.android.til.ui.pager.chars

import net.uoneweb.android.til.ui.pager.TlmPagerCodes

object ControlChars {
    val CtrlBeginFreeWord =
        PagerChar.Control(
            TlmPagerCodes.ctrlBeginFreeWord,
            "<BeginFreeWord>",
        )
    val CtrlEnd = PagerChar.Control(TlmPagerCodes.ctrlEnd, "<End>")

    val set = mutableSetOf<PagerChar>(CtrlBeginFreeWord, CtrlEnd)

    fun findByChar(char: Char): PagerChar? {
        return KanaPagerChars.set.find { it.char == char.toString() }
    }
}
