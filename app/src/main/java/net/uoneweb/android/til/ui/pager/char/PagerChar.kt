package net.uoneweb.android.til.ui.pager.char

import androidx.compose.ui.unit.IntSize

@JvmInline
value class PagerCode(val value: String)

sealed class PagerChar(val code: PagerCode, val char: String, val size: IntSize, charData: String) {
    val charData: IntArray =
        charData.trim().split("\n").flatMap { line ->
            line.trim().map { if (it == '1') 1 else 0 }
        }.toIntArray()

    class Alpha(code: PagerCode, char: String, charData: String) :
        PagerChar(code, char, IntSize(5, 7), charData)

    class Kana(code: PagerCode, char: String, charData: String) : PagerChar(
        code,
        char,
        IntSize(5, 7),
        charData,
    )

    class Number(code: PagerCode, char: String, charData: String) : PagerChar(
        code,
        char,
        IntSize(5, 7),
        charData,
    )

    class Special(code: PagerCode, char: String, charData: String) :
        PagerChar(code, char, IntSize(5, 7), charData)

    class Emoji(code: PagerCode) : PagerChar(
        code,
        "",
        IntSize(11, 15),
        """
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
         00000000000
    """,
    )

    class Control(code: PagerCode, char: String) :
        PagerChar(code, char, IntSize.Zero, "")
}
