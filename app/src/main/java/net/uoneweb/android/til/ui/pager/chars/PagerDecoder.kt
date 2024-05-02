package net.uoneweb.android.til.ui.pager.chars

class PagerDecoder {
    val ctrlBegin =
        PagerChar.Control(
            PagerCode("*2*2"),
            "*2*2",
        )
    private val ctrlEnd = PagerChar.Control(PagerCode("#"), "<END>")

    private val charset = mutableSetOf<PagerChar>(ctrlBegin, ctrlEnd)

    init {
        charset.addAll(KanaPagerChars.set)
        charset.addAll(AlphabetPagerChars.set)
        charset.addAll(SpecialPagerChars.set)
        charset.addAll(LargeChars.set)
        charset.addAll(NumberPagerChars.set)
    }

    fun decode(code: String): List<PagerChar> {
        var startIndex = 0
        val chars = mutableListOf<PagerChar>()
        while (startIndex < code.length) {
            val pagerChar =
                charset.find {
                    code.startsWith(it.code.value, startIndex, ignoreCase = true)
                } ?: NumberPagerChars.set.find {
                    it.char ==
                        code.substring(
                            startIndex,
                            startIndex + 1,
                        )
                }
                    ?: PagerChar.Control(PagerCode(code.substring(startIndex, startIndex + 1)), "?")
            if (pagerChar == ctrlBegin) {
                chars.clear()
            } else if (pagerChar == ctrlEnd) {
                break
            } else {
                chars.add(pagerChar)
            }
            startIndex += pagerChar.code.value.length
        }
        return chars
    }

    fun fromText(text: String): List<PagerChar> {
        return stringToChars(text)
    }

    private fun stringToChars(text: String): List<PagerChar> {
        return text.map {
            charset.find { ch -> ch.char == it.toString() }
        }.filterNotNull()
    }
}
