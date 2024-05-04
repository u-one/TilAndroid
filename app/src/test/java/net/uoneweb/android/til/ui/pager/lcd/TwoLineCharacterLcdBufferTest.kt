package net.uoneweb.android.til.ui.pager.lcd

import androidx.compose.ui.unit.IntSize
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TwoLineCharacterLcdBufferTest {
    @Test
    fun initialState() {
        val sut = TwoLineCharacterLcdBuffer(5, 7, 2, 3, 1)

        assertThat(sut.buffer).isEqualTo(
            intArrayOf(
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
            ),
        )
    }

    @Test
    fun drawFirstChar() {
        val sut = TwoLineCharacterLcdBuffer(5, 7, 2, 3, 1)

        val size = IntSize(2, 3)
        val data = IntArray(size.width * size.height) { 1 }
        val bitmap = LcdBitmap(data = data, size = size)

        sut.draw(bitmap)

        assertThat(sut.buffer).isEqualTo(
            intArrayOf(
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
            ),
        )
    }

    @Test
    fun drawLastOnFirstLine() {
        val sut = TwoLineCharacterLcdBuffer(5, 7, 2, 3, 1)

        val size = IntSize(2, 3)
        val data = IntArray(size.width * size.height) { 1 }
        val bitmap = LcdBitmap(data = data, size = size)
        sut.draw(bitmap)

        sut.draw(bitmap)

        assertThat(sut.buffer).isEqualTo(
            intArrayOf(
                1, 1, 0, 1, 1,
                1, 1, 0, 1, 1,
                1, 1, 0, 1, 1,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
            ),
        )
    }

    @Test
    fun drawFirstOnSecondLine() {
        val sut = TwoLineCharacterLcdBuffer(5, 7, 2, 3, 1)

        val size = IntSize(2, 3)
        val data = IntArray(size.width * size.height) { 1 }
        val bitmap = LcdBitmap(data = data, size = size)
        sut.draw(bitmap)
        sut.draw(bitmap)

        sut.draw(bitmap)

        assertThat(sut.buffer).isEqualTo(
            intArrayOf(
                1, 1, 0, 1, 1,
                1, 1, 0, 1, 1,
                1, 1, 0, 1, 1,
                0, 0, 0, 0, 0,
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
            ),
        )
    }

    @Test
    fun drawLastOnSecondLine() {
        val sut = TwoLineCharacterLcdBuffer(5, 7, 2, 3, 1)

        val size = IntSize(2, 3)
        val data = IntArray(size.width * size.height) { 1 }
        val bitmap = LcdBitmap(data = data, size = size)
        sut.draw(bitmap)
        sut.draw(bitmap)
        sut.draw(bitmap)

        sut.draw(bitmap)

        assertThat(sut.buffer).isEqualTo(
            intArrayOf(
                1, 1, 0, 1, 1,
                1, 1, 0, 1, 1,
                1, 1, 0, 1, 1,
                0, 0, 0, 0, 0,
                1, 1, 0, 1, 1,
                1, 1, 0, 1, 1,
                1, 1, 0, 1, 1,
            ),
        )
    }

    @Test
    fun drawingOutOfBufferWillBeSkipped() {
        val sut = TwoLineCharacterLcdBuffer(5, 7, 2, 3, 1)

        val size = IntSize(2, 3)
        val data0 = IntArray(size.width * size.height) { 0 }
        val bitmap0 = LcdBitmap(data = data0, size = size)
        val data1 = IntArray(size.width * size.height) { 1 }
        val bitmap1 = LcdBitmap(data = data1, size = size)
        sut.draw(bitmap0)
        sut.draw(bitmap0)
        sut.draw(bitmap0)
        sut.draw(bitmap0)

        sut.draw(bitmap1)

        assertThat(sut.buffer).isEqualTo(
            intArrayOf(
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
            ),
        )
    }

    @Test
    fun bitmapOverDisplayWidthWillBeCropped() {
        val sut = TwoLineCharacterLcdBuffer(5, 7, 2, 3, 1)

        val size1 = IntSize(6, 3)
        val data1 = IntArray(size1.width * size1.height) { 1 }
        val bitmap1 = LcdBitmap(data = data1, size = size1)

        sut.draw(bitmap1)

        assertThat(sut.buffer).isEqualTo(
            intArrayOf(
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
            ),
        )
    }

    @Test
    fun bitmapOverDisplayHeightWillBeCropped() {
        val sut = TwoLineCharacterLcdBuffer(5, 7, 2, 3, 1)

        val size1 = IntSize(2, 8)
        val data1 = IntArray(size1.width * size1.height) { 1 }
        val bitmap1 = LcdBitmap(data = data1, size = size1)

        sut.draw(bitmap1)

        assertThat(sut.buffer).isEqualTo(
            intArrayOf(
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
                1, 1, 0, 0, 0,
            ),
        )
    }
}
