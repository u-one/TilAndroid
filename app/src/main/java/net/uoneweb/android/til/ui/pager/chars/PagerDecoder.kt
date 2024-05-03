package net.uoneweb.android.til.ui.pager.chars

import net.uoneweb.android.til.ui.pager.PagerCode

class PagerDecoder {
    private val simpleNumberCharset = mutableSetOf<PagerChar>()
    private val freeWordCharset = mutableSetOf<PagerChar>()

    init {
        simpleNumberCharset.add(ControlChars.CtrlEnd)

        freeWordCharset.addAll(KanaPagerChars.set)
        freeWordCharset.addAll(AlphabetPagerChars.set)
        freeWordCharset.addAll(SpecialPagerChars.set)
        freeWordCharset.addAll(LargeChars.set)
        freeWordCharset.addAll(NumberPagerChars.set)
        freeWordCharset.addAll(ControlChars.set)
    }

    fun decode(code: String): List<PagerChar> {
        if (code.startsWith(ControlChars.CtrlBeginFreeWord.code.value)) {
            return decodeFreeWord(code)
        }
        return decodeNumber(code)
    }

    private fun decodeNumber(code: String): List<PagerChar> {
        var startIndex = 0
        val chars = mutableListOf<PagerChar>()
        while (startIndex < code.length) {
            val pagerChar = findNextPagerCharNumber(code.substring(startIndex))
            chars.add(pagerChar)
            if (pagerChar == ControlChars.CtrlEnd) {
                break
            }
            startIndex += 1
        }
        return chars
    }

    private fun findNextPagerCharNumber(codes: String): PagerChar {
        val nextCode = codes.take(1)

        simpleNumberCharset.find {
            it.code.value == nextCode
        }?.let { return it }

        // TODO: Create SingleNumberPagerChars
        NumberPagerChars.set.find {
            it.char == nextCode
        }?.let { return it }

        return unknownCode(nextCode)
    }

    private fun decodeFreeWord(code: String): List<PagerChar> {
        var startIndex = 0
        val chars = mutableListOf<PagerChar>()
        while (startIndex < code.length) {
            val pagerChar = findNextPagerChar(code.substring(startIndex))
            chars.add(pagerChar)
            if (pagerChar == ControlChars.CtrlEnd) {
                break
            }
            startIndex += pagerChar.code.value.length
        }
        return chars
    }

    private fun findNextPagerChar(codes: String): PagerChar {
        freeWordCharset.find {
            codes.startsWith(it.code.value, ignoreCase = true)
        }?.let { return it }

        val nextCode = codes.take(1)

        // TODO: Create SingleNumberPagerChars
        NumberPagerChars.set.find {
            it.char == nextCode
        }?.let { return it }

        return unknownCode(nextCode)
    }

    private fun unknownCode(code: String): PagerChar = PagerChar.Control(PagerCode(code), "?")

    fun fromText(text: String): List<PagerChar> {
        return stringToChars(text)
    }

    private fun stringToChars(text: String): List<PagerChar> {
        return text.map {
            freeWordCharset.find { ch -> ch.char == it.toString() }
        }.filterNotNull()
    }
}
