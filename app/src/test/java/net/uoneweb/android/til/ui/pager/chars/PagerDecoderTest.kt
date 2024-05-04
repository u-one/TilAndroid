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
    fun decodeFreeWordWithUnknownCode() {
        val sut = PagerDecoder()
        val code = "*2*21180#"

        val chars = sut.decode(code)

        assertThat(chars).isEqualTo(
            listOf(
                PagerChars.CtrlBeginFreeWord,
                PagerChars.KanaA,
                PagerChars.SingleNum8,
                PagerChars.SingleNum0,
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
                PagerChars.SingleNum1,
                PagerChars.SingleNum1,
                PagerChars.SingleNum1,
                PagerChars.SingleNum2,
                PagerChars.SingleNum1,
                PagerChars.SingleNum3,
                PagerChars.SingleNum1,
                PagerChars.SingleNum4,
                PagerChars.SingleNum1,
                PagerChars.SingleNum5,
                PagerChars.CtrlEnd,
            ),
        )
    }
}
