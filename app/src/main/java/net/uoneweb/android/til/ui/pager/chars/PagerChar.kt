package net.uoneweb.android.til.ui.pager.chars

import androidx.compose.ui.unit.IntSize
import net.uoneweb.android.til.ui.pager.lcd.LcdBitmap

private fun (String).toIntArray(): IntArray {
    return this.trim().split("\n").flatMap { line ->
        line.trim().map { if (it == '1') 1 else 0 }
    }.toIntArray()
}

sealed class PagerChar(
    val code: PagerCode,
    val char: String,
    val bitmap: LcdBitmap,
) {
    val size: IntSize = bitmap.size
    val charData: IntArray = bitmap.data

    constructor(code: PagerCode, char: String, size: IntSize, charData: String) : this(
        code,
        char,
        LcdBitmap(charData.toIntArray(), size),
    )

    @OptIn(ExperimentalStdlibApi::class)
    override fun toString(): String {
        return "PagerChar(code=$code char=$char addr=${this.hashCode().toHexString()})"
    }

    class Alpha(code: PagerCode, char: String, bitmap: LcdBitmap) :
        PagerChar(code, char, bitmap)

    class Kana(code: PagerCode, char: String, bitmap: LcdBitmap) : PagerChar(
        code,
        char,
        bitmap,
    )

    class Number(code: PagerCode, char: String, bitmap: LcdBitmap) : PagerChar(
        code,
        char,
        bitmap,
    )

    class Special(code: PagerCode, char: String, bitmap: LcdBitmap) :
        PagerChar(code, char, bitmap)

    class LargeChar(code: PagerCode, char: String, bitmap: LcdBitmap) : PagerChar(
        code,
        char,
        bitmap,
    )

    class Control(code: PagerCode, char: String) :
        PagerChar(code, char, LcdBitmap(intArrayOf(), IntSize.Zero))
}
