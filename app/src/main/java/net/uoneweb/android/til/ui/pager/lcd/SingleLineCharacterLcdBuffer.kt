package net.uoneweb.android.til.ui.pager.lcd

data class SingleLineCharacterLcdBuffer(
    val displayWidth: Int = 23,
    val displayHeight: Int = 15,
    val charMargin: Int = 1,
) {
    val buffer = IntArray(displayWidth * displayHeight) { 0 }
    private var currentXOffset = 0

    fun draw(bitmap: LcdBitmap) {
        if (currentXOffset + bitmap.size.width > displayWidth) return

        for (y in 0 until bitmap.size.height) {
            val orgIndex = bitmap.size.width * y
            val dstOffset = displayWidth * y + currentXOffset
            bitmap.data.copyInto(
                buffer,
                dstOffset,
                orgIndex,
                orgIndex + bitmap.size.width,
            )
        }
        currentXOffset += (bitmap.size.width + charMargin)
    }

    fun reset() {
        currentXOffset = 0
        buffer.fill(0)
    }
}
