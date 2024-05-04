package net.uoneweb.android.til.ui.pager.chars

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PagerDecoderTest {
    @Test
    fun decodeFreeWord() {
        val sut = PagerDecoder()
        val code = "*2*21112131415#"

        val chars = sut.decode(code)

        assertThat(chars).isEqualTo(
            listOf(
                PagerChars.CtrlBeginFreeWord,
                PagerChars.KanaA,
                PagerChars.KanaI,
                PagerChars.KanaU,
                PagerChars.KanaE,
                PagerChars.KanaO,
                PagerChars.CtrlEnd,
            ),
        )
    }

    @Test
    fun decode() {
        val sut = PagerDecoder()
        val code = "1112131415#"

        val chars = sut.decode(code)

        assertThat(chars).isEqualTo(
            listOf(
                PagerChars.Num1,
                PagerChars.Num1,
                PagerChars.Num1,
                PagerChars.Num2,
                PagerChars.Num1,
                PagerChars.Num3,
                PagerChars.Num1,
                PagerChars.Num4,
                PagerChars.Num1,
                PagerChars.Num5,
                PagerChars.CtrlEnd,
            ),
        )
    }

    @Test
    fun decodeFreeWordWithUnknownCode() {
        val sut = PagerDecoder()
        val code = "*2*21180#"

        val chars = sut.decode(code)

        assertThat(chars).isEqualTo(
            listOf(
                PagerChars.CtrlBeginFreeWord,
                PagerChars.KanaA,
                PagerChars.Num8,
                PagerChars.Num0,
                PagerChars.CtrlEnd,
            ),
        )
    }
}
