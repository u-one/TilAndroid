package net.uoneweb.android.til.ui.pager.chars

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PagerCharTest {
    @Test
    fun testPagerChar() {
        val chA =
            PagerChar.Alpha(
                PagerCode("16"),
                "A",
                """
       01110
       10001
       10001
       11111
       10001
       10001
       10001
    """,
            )

        assertThat(chA.charData).isEqualTo(
            intArrayOf(
                0, 1, 1, 1, 0,
                1, 0, 0, 0, 1,
                1, 0, 0, 0, 1,
                1, 1, 1, 1, 1,
                1, 0, 0, 0, 1,
                1, 0, 0, 0, 1,
                1, 0, 0, 0, 1,
            ),
        )
    }
}
