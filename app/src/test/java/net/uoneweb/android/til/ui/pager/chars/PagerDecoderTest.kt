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
                ControlChars.ctrlBeginFreeWord,
                KanaPagerChars.findByChar('ア'),
                KanaPagerChars.findByChar('イ'),
                KanaPagerChars.findByChar('ウ'),
                KanaPagerChars.findByChar('エ'),
                KanaPagerChars.findByChar('オ'),
                ControlChars.ctrlEnd,
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
                NumberPagerChars.findByChar('1'),
                NumberPagerChars.findByChar('1'),
                NumberPagerChars.findByChar('1'),
                NumberPagerChars.findByChar('2'),
                NumberPagerChars.findByChar('1'),
                NumberPagerChars.findByChar('3'),
                NumberPagerChars.findByChar('1'),
                NumberPagerChars.findByChar('4'),
                NumberPagerChars.findByChar('1'),
                NumberPagerChars.findByChar('5'),
                ControlChars.ctrlEnd,
            ),
        )
    }
}
