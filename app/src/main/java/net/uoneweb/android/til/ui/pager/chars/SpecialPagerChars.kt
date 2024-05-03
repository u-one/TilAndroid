package net.uoneweb.android.til.ui.pager.chars

import net.uoneweb.android.til.ui.pager.TlmPagerCodes

object SpecialPagerChars {
    val SpQuestion = PagerChar.Special(TlmPagerCodes.SpQuestion, "?", LcdCharData.chQuestionMark)
    val SpExclamation = PagerChar.Special(TlmPagerCodes.SpExclamation, "!", LcdCharData.chExclamationMark)
    val SpHyphen = PagerChar.Special(TlmPagerCodes.SpHyphen, "-", LcdCharData.chHyphen)
    val SpSlash = PagerChar.Special(TlmPagerCodes.SpSlash, "/", LcdCharData.chSlash)
    val SpYen = PagerChar.Special(TlmPagerCodes.SpYen, "¥", LcdCharData.chYen)
    val SpAnd = PagerChar.Special(TlmPagerCodes.SpAmpersand, "&", LcdCharData.chAmpersand)
    val SpClock = PagerChar.Special(TlmPagerCodes.SpClock, "時計", LcdCharData.chClock)
    val SpTel = PagerChar.Special(TlmPagerCodes.SpTel, "電話", LcdCharData.chTelephone)
    val SpCup = PagerChar.Special(TlmPagerCodes.SpCup, "カップ", LcdCharData.chCup)
    val SpLeftParen = PagerChar.Special(TlmPagerCodes.SpParenthesesLeft, "(", LcdCharData.chOpenParenthesis)
    val SpRightParen = PagerChar.Special(TlmPagerCodes.SpParenthesesRight, ")", LcdCharData.chCloseParenthesis)
    val SpAsterisk = PagerChar.Special(TlmPagerCodes.SpAsterisk, "*", LcdCharData.chAsterisk)
    val SpHash = PagerChar.Special(TlmPagerCodes.SpSharp, "#", LcdCharData.chHash)
    val SpSpace = PagerChar.Special(TlmPagerCodes.SpSpace, " ", LcdCharData.chSpace)
    val SpHeart = PagerChar.Special(TlmPagerCodes.SpHeart, "♥", LcdCharData.chHeart)
    val SpDakuten = PagerChar.Special(TlmPagerCodes.KanaDaku, "゛", LcdCharData.chDakuten)
    val SpHandakuten = PagerChar.Special(TlmPagerCodes.KanaHanDaku, "゜", LcdCharData.chHandakuten)

    val set =
        mutableSetOf<PagerChar>(
            SpQuestion,
            SpExclamation,
            SpHyphen,
            SpSlash,
            SpYen,
            SpAnd,
            SpClock,
            SpTel,
            SpCup,
            SpLeftParen,
            SpRightParen,
            SpAsterisk,
            SpHash,
            SpSpace,
            SpHeart,
            SpDakuten,
            SpHandakuten,
        )
}
