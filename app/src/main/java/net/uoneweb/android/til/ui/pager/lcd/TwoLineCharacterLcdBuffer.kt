package net.uoneweb.android.til.ui.pager.lcd

import androidx.compose.ui.unit.IntSize

data class TwoLineCharacterLcdBuffer(
    val displayWidth: Int = 11,
    val displayHeight: Int = 15,
    val charWidth: Int = 5,
    val charHeight: Int = 7,
    val charMargin: Int = 1,
) {
    private val bufferSize = displayWidth * displayHeight
    val buffer = IntArray(bufferSize) { 0 }
    private val maxCharCountInLine: Int = (displayWidth + charMargin) / (charWidth + charMargin)
    private var currentPos = 0

    fun draw(bitmap: LcdBitmap) {
        val orgHeight =
            if (bitmap.size.height > displayHeight) displayHeight else bitmap.size.height
        val orgWidth = if (bitmap.size.width > displayWidth) displayWidth else bitmap.size.width
        if (!canDraw(bitmap)) return

        for (y in 0 until orgHeight) {
            val orgIndex = orgWidth * y
            val orgEndIndex = orgIndex + orgWidth
            val dstOffset = displayWidth * y + currentOffset()
            bitmap.data.copyInto(
                buffer,
                dstOffset,
                orgIndex,
                orgEndIndex,
            )
        }
        currentPos++
    }

    fun reset() {
        currentPos = 0
        buffer.fill(0)
    }

    private fun canDraw(bitmap: LcdBitmap): Boolean {
        if (bitmap.size == IntSize.Zero) return false
        val orgHeight =
            if (bitmap.size.height > displayHeight) displayHeight else bitmap.size.height
        val orgWidth = if (bitmap.size.width > displayWidth) displayWidth else bitmap.size.width

        for (y in 0 until orgHeight) {
            val dstOffset = displayWidth * y + currentOffset()
            if (dstOffset > bufferSize || dstOffset + orgWidth > bufferSize) return false
        }
        return true
    }

    private fun currentOffset(): Int {
        return if (needNewLine()) {
            val secondLineOffset = (displayWidth) * (charHeight + charMargin)
            secondLineOffset + (charWidth + charMargin) * (currentPos - maxCharCountInLine)
        } else {
            0 + (charWidth + charMargin) * currentPos
        }
    }

    private fun needNewLine(): Boolean {
        return currentPos > (maxCharCountInLine - 1)
    }
}
