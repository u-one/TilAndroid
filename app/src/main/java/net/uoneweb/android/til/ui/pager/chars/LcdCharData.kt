package net.uoneweb.android.til.ui.pager.chars

import androidx.compose.ui.unit.IntSize
import net.uoneweb.android.til.ui.pager.LcdBitmap

object LcdCharData {
    val num1 =
        """
         01100
         00100
         00100
         00100
         00100
         00100
         11111
    """.toLcdBitmap()

    val num2 =
        """
            01110
            10001
            00010
            00100
            01000
            10000
            11111
    """.toLcdBitmap()

    val num3 =
        """
            01110
            10001
            00001
            00110
            00001
            10001
            01110
    """.toLcdBitmap()

    val num4 =
        """
            00010
            00110
            01010
            10010
            11111
            00010
            00010
    """.toLcdBitmap()

    val num5 =
        """
            11111
            10000
            10000
            11110
            00001
            10001
            01110
    """.toLcdBitmap()

    val num6 =
        """
            01110
            10001
            10000
            11110
            10001
            10001
            01110
    """.toLcdBitmap()

    val num7 =
        """
            11111
            00001
            00010
            00100
            01000
            01000
            01000
    """.toLcdBitmap()

    val num8 =
        """
            01110
            10001
            10001
            01110
            10001
            10001
            01110
    """.toLcdBitmap()

    val num9 =
        """
            01110
            10001
            10001
            01111
            00001
            10001
            01110
    """.toLcdBitmap()

    val num0 =
        """
            01110
            10001
            10001
            10001
            10001
            10001
            01110
    """.toLcdBitmap()

    val chAlphaA =
        """
       01110
       10001
       10001
       11111
       10001
       10001
       10001
    """.toLcdBitmap()

    val chAlphaB =
        """
       11110
       10001
       10001
       11110
       10001
       10001
       11110
    """.toLcdBitmap()

    val chAlphaC =
        """
       01110
       10000
       10000
       10000
       10000
       10000
       01110
    """.toLcdBitmap()

    val chAlphaD =
        """
       11110
       10001
       10001
       10001
       10001
       10001
       11110
    """.toLcdBitmap()

    val chAlphaE =
        """
       11111
       10000
       10000
       11110
       10000
       10000
       11111
    """.toLcdBitmap()

    val chAlphaF =
        """
       11111
       10000
       10000
       11110
       10000
       10000
       10000
    """.toLcdBitmap()

    val chAlphaG =
        """
       01110
       10001
       10000
       10111
       10001
       10001
       01111
    """.toLcdBitmap()

    val chAlphaH =
        """
       10001
       10001
       10001
       11111
       10001
       10001
       10001
    """.toLcdBitmap()

    val chAlphaI =
        """
       00100
       00100
       00100
       00100
       00100
       00100
       00100
    """.toLcdBitmap()

    val chAlphaJ =
        """
       00001
       00001
       00001
       00001
       00001
       10001
       01110
    """.toLcdBitmap()

    val chAlphaK =
        """
       10001
       10010
       10100
       11000
       10100
       10010
       10001
    """.toLcdBitmap()

    val chAlphaL =
        """
       10000
       10000
       10000
       10000
       10000
       10000
       11111
    """.toLcdBitmap()

    val chAlphaM =
        """
       10001
       11011
       10101
       10101
       10001
       10001
       10001
    """.toLcdBitmap()

    val chAlphaN =
        """
       10001
       11001
       10101
       10011
       10001
       10001
       10001
    """.toLcdBitmap()

    val chAlphaO =
        """
       01110
       10001
       10001
       10001
       10001
       10001
       01110
    """.toLcdBitmap()

    val chAlphaP =
        """
       11110
       10001
       10001
       11110
       10000
       10000
       10000
    """.toLcdBitmap()

    val chAlphaQ =
        """
       01110
       10001
       10001
       10001
       10101
       10011
       01111
    """.toLcdBitmap()

    val chAlphaR =
        """
       11110
       10001
       10001
       11110
       10100
       10010
       10001
    """.toLcdBitmap()

    val chAlphaS =
        """
       01110
       10001
       10000
       01110
       00001
       10001
       01110
    """.toLcdBitmap()

    val chAlphaT =
        """
       11111
       00100
       00100
       00100
       00100
       00100
       00100
    """.toLcdBitmap()

    val chAlphaU =
        """
       10001
       10001
       10001
       10001
       10001
       10001
       01110
    """.toLcdBitmap()

    val chAlphaV =
        """
       10001
       10001
       10001
       01010
       01010
       00100
       00100
    """.toLcdBitmap()

    val chAlphaW =
        """
       10001
       10001
       10101
       10101
       10101
       11011
       10001
    """.toLcdBitmap()

    val chAlphaX =
        """
       10001
       10001
       01010
       00100
       01010
       10001
       10001
    """.toLcdBitmap()

    val chAlphaY =
        """
       10001
       10001
       01010
       00100
       00100
       00100
       00100
    """.toLcdBitmap()

    val chAlphaZ =
        """
       11111
       00001
       00010
       00100
       01000
       10000
       11111
    """.toLcdBitmap()

    /*
     * カナ文字
     * 全てオリジナル
     */

    val chKanaA =
        """
     11111
     00001
     00101
     00110
     00100
     01000
     10000
    """.toLcdBitmap()

    val chKanaI =
        """
     00001
     00010
     00100
     11100
     00100
     00100
     00100
    """.toLcdBitmap()

    val chKanaU =
        """
     00100
     11111
     10001
     00001
     00001
     00010
     11100
    """.toLcdBitmap()

    val chKanaE =
        """
     01111
     00100
     00100
     00100
     00100
     00100
     11111
    """.toLcdBitmap()

    val chKanaO =
        """
     00010
     11111
     00110
     01010
     10010
     00010
     00010
    """.toLcdBitmap()

    val chKanaKa =
        """
     00100
     11111
     00101
     00101
     01001
     10001
     00001
    """.toLcdBitmap()

    val chKanaKi =
        """
     01000
     11111
     00100
     11111
     00100
     00010
     00010
    """.toLcdBitmap()

    val chKanaKu =
        """
     01111
     01001
     10001
     00001
     00010
     00100
     01000
    """.toLcdBitmap()

    val chKanaKe =
        """
     01000
     11111
     10100
     00100
     00100
     00100
     01000
    """.toLcdBitmap()

    val chKanaKo =
        """
     11111
     00001
     00001
     00001
     00001
     00001
     11111
    """.toLcdBitmap()

    val chKanaSa =
        """
     01010
     11111
     01010
     00010
     00010
     00100
     01000
    """.toLcdBitmap()

    val chKanaSi =
        """
     00001
     11001
     00001
     11001
     00010
     00100
     11000
    """.toLcdBitmap()

    val chKanaSu =
        """
     11111
     00001
     00001
     00010
     00100
     01010
     10001
    """.toLcdBitmap()

    val chKanaSe =
        """
     01000
     11111
     01001
     01000
     01000
     01000
     00111
    """.toLcdBitmap()

    val chKanaSo =
        """
     10001
     10001
     01001
     00001
     00010
     00100
     11000
    """.toLcdBitmap()

    val chKanaTa =
        """
     01111
     01001
     10111
     00001
     00010
     00100
     01000
    """.toLcdBitmap()

    val chKanaTi =
        """
     00011
     11100
     00100
     11111
     00100
     01000
     10000
    """.toLcdBitmap()

    val chKanaTu =
        """
     10101
     10101
     10101
     00001
     00010
     00100
     11000
    """.toLcdBitmap()

    val chKanaTe =
        """
     11111
     00000
     11111
     00100
     00100
     01000
     10000
    """.toLcdBitmap()

    val chKanaTo =
        """
     10000
     10000
     11100
     10010
     10001
     10000
     10000
    """.toLcdBitmap()

    val chKanaNa =
        """
     00100
     11111
     00100
     00100
     00100
     01000
     10000
    """.toLcdBitmap()

    val chKanaNi =
        """
     00000
     01111
     00000
     00000
     00000
     00000
     11111
    """.toLcdBitmap()

    val chKanaNu =
        """
     11111
     00001
     00010
     01010
     00100
     01010
     10000
    """.toLcdBitmap()

    val chKanaNe =
        """
     00100
     11111
     00010
     00110
     01101
     10100
     00100
    """.toLcdBitmap()

    val chKanaNo =
        """
     00001
     00001
     00010
     00010
     00100
     01000
     10000
    """.toLcdBitmap()

    val chKanaHa =
        """
     10010
     10010
     10001
     10001
     10001
     10001
     10001
    """.toLcdBitmap()

    val chKanaHi =
        """
     10000
     11111
     10000
     10000
     10000
     10000
     01111
    """.toLcdBitmap()

    val chKanaHu =
        """
     11111
     00001
     00001
     00010
     00100
     01000
     10000
    """.toLcdBitmap()

    val chKanaHe =
        """
     01000
     10100
     10010
     00010
     00001
     00001
     00001
    """.toLcdBitmap()

    val chKanaHo =
        """
     00100
     11111
     00100
     00100
     10101
     10101
     00100
    """.toLcdBitmap()

    val chKanaMa =
        """
     11111
     00001
     00010
     10100
     01000
     00100
     00010
    """.toLcdBitmap()

    val chKanaMi =
        """
     11100
     00011
     11100
     00011
     00000
     11100
     00011
    """.toLcdBitmap()

    val chKanaMu =
        """
     00100
     00100
     01000
     01000
     10100
     10010
     11101
    """.toLcdBitmap()

    val chKanaMe =
        """
     00001
     00001
     10010
     01010
     00100
     01010
     10001
    """.toLcdBitmap()

    val chKanaMo =
        """
     11111
     00100
     00100
     11111
     00100
     00100
     00111
    """.toLcdBitmap()

    val chKanaYa =
        """
     01000
     11111
     01001
     01010
     00100
     00100
     00100
    """.toLcdBitmap()

    val chKanaYu =
        """
     11110
     00010
     00010
     00010
     00010
     00010
     11111
    """.toLcdBitmap()

    val chKanaYo =
        """
     11111
     00001
     00001
     11111
     00001
     00001
     11111
    """.toLcdBitmap()

    val chKanaRa =
        """
     11111
     00000
     11111
     00001
     00010
     00100
     11000
    """.toLcdBitmap()

    val chKanaRi =
        """
     10001
     10001
     10001
     10001
     00010
     00010
     00100
    """.toLcdBitmap()

    val chKanaRu =
        """
     01010
     01010
     01010
     01010
     01010
     10010
     10011
    """.toLcdBitmap()

    val chKanaRe =
        """
     10000
     10000
     10000
     10001
     10010
     10100
     11000
    """.toLcdBitmap()

    val chKanaRo =
        """
     11111
     10001
     10001
     10001
     10001
     10001
     11111
    """.toLcdBitmap()

    val chKanaWa =
        """
     11111
     10001
     00001
     00001
     00010
     00100
     11000
    """.toLcdBitmap()

    val chKanaWo =
        """
     11111
     00001
     11111
     00001
     00010
     00100
     11000
    """.toLcdBitmap()

    val chKanaN =
        """
     00001
     11001
     00001
     00001
     00010
     00100
     11000
    """.toLcdBitmap()

    val chQuestionMark =
        """
         01110
         10001
         00001
         00010
         00100
         00000
         00100
    """.toLcdBitmap()

    val chExclamationMark =
        """
         00100
         00100
         00100
         00100
         00100
         00000
         00100
    """.toLcdBitmap()

    val chHyphen =
        """
         00000
         00000
         00000
         11111
         00000
         00000
         00000
    """.toLcdBitmap()

    val chSlash =
        """
         00001
         00010
         00010
         00100
         01000
         01000
         10000
    """.toLcdBitmap()

    val chYen =
        """
         10001
         01010
         00100
         11111
         00100
         11111
         00100
    """.toLcdBitmap()

    val chAmpersand =
        """
         01110
         10001
         01000
         10101
         10010
         10010
         01101
    """.toLcdBitmap()

    val chClock =
        """
         01110
         10101
         10101
         10111
         10001
         10001
         01110
    """.toLcdBitmap()

    val chTelephone =
        """
         00000
         00000
         00000
         00000
         00000
         00000
         00000
    """.toLcdBitmap()

    val chCup =
        """
         11100
         11110
         11101
         11101
         11101
         11110
         11100
    """.toLcdBitmap()

    val chOpenParenthesis =
        """
         00001
         00010
         00100
         00100
         00100
         00010
         00001
    """.toLcdBitmap()

    val chCloseParenthesis =
        """
         10000
         01000
         00100
         00100
         00100
         01000
         10000
    """.toLcdBitmap()

    val chAsterisk =
        """
         00100
         10101
         01110
         00100
         01110
         10101
         00100
    """.toLcdBitmap()

    val chHash =
        """
         01010
         01010
         11111
         01010
         11111
         01010
         01010
    """.toLcdBitmap()

    val chSpace =
        """
         00000
         00000
         00000
         00000
         00000
         00000
         00000
    """.toLcdBitmap()

    val chHeart =
        """
         01010
         11111
         11111
         11111
         11111
         01110
         00100
    """.toLcdBitmap()

    val chDakuten =
        """
         10100
         01010
         00000
         00000
         00000
         00000
         00000
    """.toLcdBitmap()

    val chHandakuten =
        """
         01000
         10100
         01000
         00000
         00000
         00000
         00000
    """.toLcdBitmap()

    private fun (String).toLcdBitmap(): LcdBitmap {
        return LcdBitmap(
            this.toIntArray(),
            IntSize(5, 7),
        )
    }

    private fun (String).toIntArray(): IntArray {
        return this.trim().split("\n")
            .flatMap { line ->
                line.trim().map {
                    if (it == '1') 1 else 0
                }
            }.toIntArray()
    }
}
