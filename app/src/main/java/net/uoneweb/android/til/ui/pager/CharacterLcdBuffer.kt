package net.uoneweb.android.til.ui.pager

data class CharacterLcdBuffer(
    val displayWidth: Int = 11,
    val displayHeight: Int = 15,
    val charWidth: Int = 5,
    val charHeight: Int = 7,
    val charMargin: Int = 1,
) {
    val buffer = IntArray(displayWidth * displayHeight) { 0 }
    private val maxCharCountInLine: Int = (displayWidth + charMargin) / (charWidth + charMargin)
    private var currentPos = 0

    fun draw(bitmap: LcdBitmap) {
        for (y in 0 until bitmap.size.height) {
            val orgIndex = bitmap.size.width * y
            val dstOffset = displayWidth * y + currentOffset()
            bitmap.data.copyInto(
                buffer,
                dstOffset,
                orgIndex,
                orgIndex + bitmap.size.width
            )
        }
        currentPos++
    }

    fun reset() {
        currentPos = 0
        buffer.fill(0)
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