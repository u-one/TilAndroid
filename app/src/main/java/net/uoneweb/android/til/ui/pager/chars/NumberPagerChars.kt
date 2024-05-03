package net.uoneweb.android.til.ui.pager.chars

import net.uoneweb.android.til.ui.pager.TlmPagerCodes

object NumberPagerChars {
    val pagerCharNum1 = PagerChar.Number(TlmPagerCodes.Num1, "1", LcdCharData.num1)
    val pagerCharNum2 = PagerChar.Number(TlmPagerCodes.Num2, "2", LcdCharData.num2)
    val pagerCharNum3 = PagerChar.Number(TlmPagerCodes.Num3, "3", LcdCharData.num3)
    val pagerCharNum4 = PagerChar.Number(TlmPagerCodes.Num4, "4", LcdCharData.num4)
    val pagerCharNum5 = PagerChar.Number(TlmPagerCodes.Num5, "5", LcdCharData.num5)
    val pagerCharNum6 = PagerChar.Number(TlmPagerCodes.Num6, "6", LcdCharData.num6)
    val pagerCharNum7 = PagerChar.Number(TlmPagerCodes.Num7, "7", LcdCharData.num7)
    val pagerCharNum8 = PagerChar.Number(TlmPagerCodes.Num8, "8", LcdCharData.num8)
    val pagerCharNum9 = PagerChar.Number(TlmPagerCodes.Num9, "9", LcdCharData.num9)
    val pagerCharNum0 = PagerChar.Number(TlmPagerCodes.Num0, "0", LcdCharData.num0)

    val set =
        mutableSetOf<PagerChar>(
            pagerCharNum1,
            pagerCharNum2,
            pagerCharNum3,
            pagerCharNum4,
            pagerCharNum5,
            pagerCharNum6,
            pagerCharNum7,
            pagerCharNum8,
            pagerCharNum9,
            pagerCharNum0,
        )

    fun findByChar(char: Char): PagerChar? {
        return set.find { it.char == char.toString() }
    }
}
