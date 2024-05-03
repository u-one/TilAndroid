package net.uoneweb.android.til.ui.pager.chars

object ControlChars {
    val set = mutableSetOf<PagerChar>()

    val ctrlBeginFreeWord =
        PagerChar.Control(
            PagerCode("*2*2"),
            "<BeginFreeWord>",
        )
    val ctrlEnd = PagerChar.Control(PagerCode("#"), "<End>")

    fun findByChar(char: Char): PagerChar? {
        return KanaPagerChars.set.find { it.char == char.toString() }
    }

    init {
        set.add(ctrlBeginFreeWord)
        set.add(ctrlEnd)
    }
}
