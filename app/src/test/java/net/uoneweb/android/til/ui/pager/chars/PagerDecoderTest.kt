package net.uoneweb.android.til.ui.pager.chars

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PagerDecoderTest {
    @Test
    fun decode() {
        val sut = PagerDecoder()
        val code = "*2*21112131415#"

        val chars = sut.decode(code)

        assertThat(chars).isEqualTo(
            listOf(
                KanaPagerChars.findByChar('ア'),
                KanaPagerChars.findByChar('イ'),
                KanaPagerChars.findByChar('ウ'),
                KanaPagerChars.findByChar('エ'),
                KanaPagerChars.findByChar('オ'),
            ),
        )
    }
}
