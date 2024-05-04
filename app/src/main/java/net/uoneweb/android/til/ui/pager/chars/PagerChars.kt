package net.uoneweb.android.til.ui.pager.chars

import net.uoneweb.android.til.ui.pager.TlmPagerCodes

object PagerChars {
    val CtrlBeginFreeWord = PagerChar.Control(TlmPagerCodes.CtrlBeginFreeWord, "<BeginFreeWord>")
    val CtrlBeginFixedWord = PagerChar.Control(TlmPagerCodes.CtrlBeginFixedWord, "<BeginFixedWord>")
    val CtrlEnd = PagerChar.Control(TlmPagerCodes.CtrlEnd, "<End>")

    val ctrls = mutableSetOf<PagerChar>(CtrlBeginFreeWord, CtrlBeginFixedWord, CtrlEnd)

    val Num1 = PagerChar.Number(TlmPagerCodes.Num1, "1", LcdCharData.num1)
    val Num2 = PagerChar.Number(TlmPagerCodes.Num2, "2", LcdCharData.num2)
    val Num3 = PagerChar.Number(TlmPagerCodes.Num3, "3", LcdCharData.num3)
    val Num4 = PagerChar.Number(TlmPagerCodes.Num4, "4", LcdCharData.num4)
    val Num5 = PagerChar.Number(TlmPagerCodes.Num5, "5", LcdCharData.num5)
    val Num6 = PagerChar.Number(TlmPagerCodes.Num6, "6", LcdCharData.num6)
    val Num7 = PagerChar.Number(TlmPagerCodes.Num7, "7", LcdCharData.num7)
    val Num8 = PagerChar.Number(TlmPagerCodes.Num8, "8", LcdCharData.num8)
    val Num9 = PagerChar.Number(TlmPagerCodes.Num9, "9", LcdCharData.num9)
    val Num0 = PagerChar.Number(TlmPagerCodes.Num0, "0", LcdCharData.num0)

    val numbers =
        mutableSetOf<PagerChar>(
            Num1,
            Num2,
            Num3,
            Num4,
            Num5,
            Num6,
            Num7,
            Num8,
            Num9,
            Num0,
        )

    val AlphaA = PagerChar.Alpha(TlmPagerCodes.AlphaA, "A", LcdCharData.chAlphaA)
    val AlphaB = PagerChar.Alpha(TlmPagerCodes.AlphaB, "B", LcdCharData.chAlphaB)
    val AlphaC = PagerChar.Alpha(TlmPagerCodes.AlphaC, "C", LcdCharData.chAlphaC)
    val AlphaD = PagerChar.Alpha(TlmPagerCodes.AlphaD, "D", LcdCharData.chAlphaD)
    val AlphaE = PagerChar.Alpha(TlmPagerCodes.AlphaE, "E", LcdCharData.chAlphaE)
    val AlphaF = PagerChar.Alpha(TlmPagerCodes.AlphaF, "F", LcdCharData.chAlphaF)
    val AlphaG = PagerChar.Alpha(TlmPagerCodes.AlphaG, "G", LcdCharData.chAlphaG)
    val AlphaH = PagerChar.Alpha(TlmPagerCodes.AlphaH, "H", LcdCharData.chAlphaH)
    val AlphaI = PagerChar.Alpha(TlmPagerCodes.AlphaI, "I", LcdCharData.chAlphaI)
    val AlphaJ = PagerChar.Alpha(TlmPagerCodes.AlphaJ, "J", LcdCharData.chAlphaJ)
    val AlphaK = PagerChar.Alpha(TlmPagerCodes.AlphaK, "K", LcdCharData.chAlphaK)
    val AlphaL = PagerChar.Alpha(TlmPagerCodes.AlphaL, "L", LcdCharData.chAlphaL)
    val AlphaM = PagerChar.Alpha(TlmPagerCodes.AlphaM, "M", LcdCharData.chAlphaM)
    val AlphaN = PagerChar.Alpha(TlmPagerCodes.AlphaN, "N", LcdCharData.chAlphaN)
    val AlphaO = PagerChar.Alpha(TlmPagerCodes.AlphaO, "O", LcdCharData.chAlphaO)
    val AlphaP = PagerChar.Alpha(TlmPagerCodes.AlphaP, "P", LcdCharData.chAlphaP)
    val AlphaQ = PagerChar.Alpha(TlmPagerCodes.AlphaQ, "Q", LcdCharData.chAlphaQ)
    val AlphaR = PagerChar.Alpha(TlmPagerCodes.AlphaR, "R", LcdCharData.chAlphaR)
    val AlphaS = PagerChar.Alpha(TlmPagerCodes.AlphaS, "S", LcdCharData.chAlphaS)
    val AlphaT = PagerChar.Alpha(TlmPagerCodes.AlphaT, "T", LcdCharData.chAlphaT)
    val AlphaU = PagerChar.Alpha(TlmPagerCodes.AlphaU, "U", LcdCharData.chAlphaU)
    val AlphaV = PagerChar.Alpha(TlmPagerCodes.AlphaV, "V", LcdCharData.chAlphaV)
    val AlphaW = PagerChar.Alpha(TlmPagerCodes.AlphaW, "W", LcdCharData.chAlphaW)
    val AlphaX = PagerChar.Alpha(TlmPagerCodes.AlphaX, "X", LcdCharData.chAlphaX)
    val AlphaY = PagerChar.Alpha(TlmPagerCodes.AlphaY, "Y", LcdCharData.chAlphaY)
    val AlphaZ = PagerChar.Alpha(TlmPagerCodes.AlphaZ, "Z", LcdCharData.chAlphaZ)

    val alphas =
        mutableSetOf<PagerChar>(
            AlphaA,
            AlphaB,
            AlphaC,
            AlphaD,
            AlphaE,
            AlphaF,
            AlphaG,
            AlphaH,
            AlphaI,
            AlphaJ,
            AlphaK,
            AlphaL,
            AlphaM,
            AlphaN,
            AlphaO,
            AlphaP,
            AlphaQ,
            AlphaR,
            AlphaS,
            AlphaT,
            AlphaU,
            AlphaV,
            AlphaW,
            AlphaX,
            AlphaY,
            AlphaZ,
        )

    val KanaA = PagerChar.Kana(TlmPagerCodes.KanaA, "ア", LcdCharData.chKanaA)
    val KanaI = PagerChar.Kana(TlmPagerCodes.KanaI, "イ", LcdCharData.chKanaI)
    val KanaU = PagerChar.Kana(TlmPagerCodes.KanaU, "ウ", LcdCharData.chKanaU)
    val KanaE = PagerChar.Kana(TlmPagerCodes.KanaE, "エ", LcdCharData.chKanaE)
    val KanaO = PagerChar.Kana(TlmPagerCodes.KanaO, "オ", LcdCharData.chKanaO)
    val KanaKa = PagerChar.Kana(TlmPagerCodes.KanaKa, "カ", LcdCharData.chKanaKa)
    val KanaKi = PagerChar.Kana(TlmPagerCodes.KanaKi, "キ", LcdCharData.chKanaKi)
    val KanaKu = PagerChar.Kana(TlmPagerCodes.KanaKu, "ク", LcdCharData.chKanaKu)
    val KanaKe = PagerChar.Kana(TlmPagerCodes.KanaKe, "ケ", LcdCharData.chKanaKe)
    val KanaKo = PagerChar.Kana(TlmPagerCodes.KanaKo, "コ", LcdCharData.chKanaKo)
    val KanaSa = PagerChar.Kana(TlmPagerCodes.KanaSa, "サ", LcdCharData.chKanaSa)
    val KanaSi = PagerChar.Kana(TlmPagerCodes.KanaSi, "シ", LcdCharData.chKanaSi)
    val KanaSu = PagerChar.Kana(TlmPagerCodes.KanaSu, "ス", LcdCharData.chKanaSu)
    val KanaSe = PagerChar.Kana(TlmPagerCodes.KanaSe, "セ", LcdCharData.chKanaSe)
    val KanaSo = PagerChar.Kana(TlmPagerCodes.KanaSo, "ソ", LcdCharData.chKanaSo)
    val KanaTa = PagerChar.Kana(TlmPagerCodes.KanaTa, "タ", LcdCharData.chKanaTa)
    val KanaTi = PagerChar.Kana(TlmPagerCodes.KanaTi, "チ", LcdCharData.chKanaTi)
    val KanaTu = PagerChar.Kana(TlmPagerCodes.KanaTu, "ツ", LcdCharData.chKanaTu)
    val KanaTe = PagerChar.Kana(TlmPagerCodes.KanaTe, "テ", LcdCharData.chKanaTe)
    val KanaTo = PagerChar.Kana(TlmPagerCodes.KanaTo, "ト", LcdCharData.chKanaTo)
    val KanaNa = PagerChar.Kana(TlmPagerCodes.KanaNa, "ナ", LcdCharData.chKanaNa)
    val KanaNi = PagerChar.Kana(TlmPagerCodes.KanaNi, "ニ", LcdCharData.chKanaNi)
    val KanaNu = PagerChar.Kana(TlmPagerCodes.KanaNu, "ヌ", LcdCharData.chKanaNu)
    val KanaNe = PagerChar.Kana(TlmPagerCodes.KanaNe, "ネ", LcdCharData.chKanaNe)
    val KanaNo = PagerChar.Kana(TlmPagerCodes.KanaNo, "ノ", LcdCharData.chKanaNo)
    val KanaHa = PagerChar.Kana(TlmPagerCodes.KanaHa, "ハ", LcdCharData.chKanaHa)
    val KanaHi = PagerChar.Kana(TlmPagerCodes.KanaHi, "ヒ", LcdCharData.chKanaHi)
    val KanaHu = PagerChar.Kana(TlmPagerCodes.KanaHu, "フ", LcdCharData.chKanaHu)
    val KanaHe = PagerChar.Kana(TlmPagerCodes.KanaHe, "ヘ", LcdCharData.chKanaHe)
    val KanaHo = PagerChar.Kana(TlmPagerCodes.KanaHo, "ホ", LcdCharData.chKanaHo)
    val KanaMa = PagerChar.Kana(TlmPagerCodes.KanaMa, "マ", LcdCharData.chKanaMa)
    val KanaMi = PagerChar.Kana(TlmPagerCodes.KanaMi, "ミ", LcdCharData.chKanaMi)
    val KanaMu = PagerChar.Kana(TlmPagerCodes.KanaMu, "ム", LcdCharData.chKanaMu)
    val KanaMe = PagerChar.Kana(TlmPagerCodes.KanaMe, "メ", LcdCharData.chKanaMe)
    val KanaMo = PagerChar.Kana(TlmPagerCodes.KanaMo, "モ", LcdCharData.chKanaMo)
    val KanaYa = PagerChar.Kana(TlmPagerCodes.KanaYa, "ヤ", LcdCharData.chKanaYa)
    val KanaYu = PagerChar.Kana(TlmPagerCodes.KanaYu, "ユ", LcdCharData.chKanaYu)
    val KanaYo = PagerChar.Kana(TlmPagerCodes.KanaYo, "ヨ", LcdCharData.chKanaYo)
    val KanaRa = PagerChar.Kana(TlmPagerCodes.KanaRa, "ラ", LcdCharData.chKanaRa)
    val KanaRi = PagerChar.Kana(TlmPagerCodes.KanaRi, "リ", LcdCharData.chKanaRi)
    val KanaRu = PagerChar.Kana(TlmPagerCodes.KanaRu, "ル", LcdCharData.chKanaRu)
    val KanaRe = PagerChar.Kana(TlmPagerCodes.KanaRe, "レ", LcdCharData.chKanaRe)
    val KanaRo = PagerChar.Kana(TlmPagerCodes.KanaRo, "ロ", LcdCharData.chKanaRo)
    val KanaWa = PagerChar.Kana(TlmPagerCodes.KanaWa, "ワ", LcdCharData.chKanaWa)
    val KanaWo = PagerChar.Kana(TlmPagerCodes.KanaWo, "ヲ", LcdCharData.chKanaWo)
    val KanaN = PagerChar.Kana(TlmPagerCodes.KanaN, "ン", LcdCharData.chKanaN)

    val kanas =
        mutableSetOf<PagerChar>(
            KanaA,
            KanaI,
            KanaU,
            KanaE,
            KanaO,
            KanaKa,
            KanaKi,
            KanaKu,
            KanaKe,
            KanaKo,
            KanaSa,
            KanaSi,
            KanaSu,
            KanaSe,
            KanaSo,
            KanaTa,
            KanaTi,
            KanaTu,
            KanaTe,
            KanaTo,
            KanaNa,
            KanaNi,
            KanaNu,
            KanaNe,
            KanaNo,
            KanaHa,
            KanaHi,
            KanaHu,
            KanaHe,
            KanaHo,
            KanaMa,
            KanaMi,
            KanaMu,
            KanaMe,
            KanaMo,
            KanaYa,
            KanaYu,
            KanaYo,
            KanaRa,
            KanaRi,
            KanaRu,
            KanaRe,
            KanaRo,
            KanaWa,
            KanaWo,
            KanaN,
        )

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

    val specials =
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

    val IconHappy = PagerChar.Emoji(TlmPagerCodes.IconHappy, "Happy", LcdFullCharData.chFullHappy)
    val IconSmile = PagerChar.Emoji(TlmPagerCodes.IconSmile, "Smile", LcdFullCharData.chFullSmile)
    val IconCry = PagerChar.Emoji(TlmPagerCodes.IconCry, "Cry", LcdFullCharData.chFullCry)
    val IconSad = PagerChar.Emoji(TlmPagerCodes.IconSad, "Sad", LcdFullCharData.chFullSad)
    val IconScream = PagerChar.Emoji(TlmPagerCodes.IconScream, "Scream", LcdFullCharData.chFullScream)
    val IconWink = PagerChar.Emoji(TlmPagerCodes.IconWink, "Wink", LcdFullCharData.chFullWink)
    val IconAngry = PagerChar.Emoji(TlmPagerCodes.IconAngry, "Angry", LcdFullCharData.chFullAngry)
    val IconUpset = PagerChar.Emoji(TlmPagerCodes.IconUpset, "Upset", LcdFullCharData.chFullUpset)
    val IconGood = PagerChar.Emoji(TlmPagerCodes.IconGood, "Good", LcdFullCharData.chFullGood)
    val IconHandScissors = PagerChar.Emoji(TlmPagerCodes.IconHandScissors, "HandScissors", LcdFullCharData.chFullHandScissors)
    val IconHandPaper = PagerChar.Emoji(TlmPagerCodes.IconHandPaper, "HandPaper", LcdFullCharData.chFullHandPaper)
    val IconHandRock = PagerChar.Emoji(TlmPagerCodes.IconHandRock, "HandRock", LcdFullCharData.chFullHandRock)
    val IconSunny = PagerChar.Emoji(TlmPagerCodes.IconSunny, "Sunny", LcdFullCharData.chFullSunny)
    val IconCloudy = PagerChar.Emoji(TlmPagerCodes.IconCloudy, "Cloudy", LcdFullCharData.chFullCloudy)
    val IconRainy = PagerChar.Emoji(TlmPagerCodes.IconRainy, "Rainy", LcdFullCharData.chFullRainy)
    val IconSnowy = PagerChar.Emoji(TlmPagerCodes.IconSnowy, "Snowy", LcdFullCharData.chFullSnowy)
    val IconHeart = PagerChar.Emoji(TlmPagerCodes.IconHeart, "Heart", LcdFullCharData.chFullHeart)
    val IconBrokenHeart = PagerChar.Emoji(TlmPagerCodes.IconBrokenHeart, "BrokenHeart", LcdFullCharData.chFullBrokenHeart)
    val IconSuper = PagerChar.Emoji(TlmPagerCodes.IconSuper, "Super", LcdFullCharData.chFullSuper)
    val IconMuscle = PagerChar.Emoji(TlmPagerCodes.IconMuscle, "Muscle", LcdFullCharData.chFullMuscle)
    val IconTrain = PagerChar.Emoji(TlmPagerCodes.IconTrain, "Train", LcdFullCharData.chFullTrain)
    val IconCar = PagerChar.Emoji(TlmPagerCodes.IconCar, "Car", LcdFullCharData.chFullCar)
    val IconHome = PagerChar.Emoji(TlmPagerCodes.IconHome, "Home", LcdFullCharData.chFullHome)
    val IconPhone = PagerChar.Emoji(TlmPagerCodes.IconPhone, "Phone", LcdFullCharData.chFullPhone)
    val IconBeer = PagerChar.Emoji(TlmPagerCodes.IconBeer, "Beer", LcdFullCharData.chFullBeer)
    val IconCutlery = PagerChar.Emoji(TlmPagerCodes.IconCutlery, "Cutlery", LcdFullCharData.chFullCutlery)
    val IconCake = PagerChar.Emoji(TlmPagerCodes.IconCake, "Cake", LcdFullCharData.chFullCake)
    val IconPager = PagerChar.Emoji(TlmPagerCodes.IconPager, "Pager", LcdFullCharData.chFullPager)
    val IconKaraoke = PagerChar.Emoji(TlmPagerCodes.IconKaraoke, "Karaoke", LcdFullCharData.chFullKaraoke)
    val IconTv = PagerChar.Emoji(TlmPagerCodes.IconTv, "Tv", LcdFullCharData.chFullTv)
    val IconFlower = PagerChar.Emoji(TlmPagerCodes.IconFlower, "Flower", LcdFullCharData.chFullFlower)
    val IconPoop = PagerChar.Emoji(TlmPagerCodes.IconPoop, "Poop", LcdFullCharData.chFullPoop)

    val icons =
        mutableSetOf<PagerChar>(
            IconHappy,
            IconSmile,
            IconCry,
            IconSad,
            IconScream,
            IconWink,
            IconAngry,
            IconUpset,
            IconGood,
            IconHandScissors,
            IconHandPaper,
            IconHandRock,
            IconSunny,
            IconCloudy,
            IconRainy,
            IconSnowy,
            IconHeart,
            IconBrokenHeart,
            IconSuper,
            IconMuscle,
            IconTrain,
            IconCar,
            IconHome,
            IconPhone,
            IconBeer,
            IconCutlery,
            IconCake,
            IconPager,
            IconKaraoke,
            IconTv,
            IconFlower,
            IconPoop,
        )

    val all = mutableSetOf<PagerChar>()

    init {
        all.addAll(alphas)
        all.addAll(kanas)
        all.addAll(icons)
    }

    fun findByChar(char: Char): PagerChar? {
        return all.find { it.char == char.toString() }
    }

    fun (Set<PagerChar>).findByChar(char: Char): PagerChar? {
        return this.find { it.char == char.toString() }
    }
}
