package net.uoneweb.android.til.ui.pager

import android.media.AudioManager
import android.media.ToneGenerator

interface TonePlayer {
    fun startTone(char: Char)

    fun stopTone()
}

object TonePlayerImpl : TonePlayer {
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_DTMF, ToneGenerator.MAX_VOLUME)
    private val dtmfs =
        mapOf(
            '1' to ToneGenerator.TONE_DTMF_1,
            '2' to ToneGenerator.TONE_DTMF_2,
            '3' to ToneGenerator.TONE_DTMF_3,
            '4' to ToneGenerator.TONE_DTMF_4,
            '5' to ToneGenerator.TONE_DTMF_5,
            '6' to ToneGenerator.TONE_DTMF_6,
            '7' to ToneGenerator.TONE_DTMF_7,
            '8' to ToneGenerator.TONE_DTMF_8,
            '9' to ToneGenerator.TONE_DTMF_9,
            '*' to ToneGenerator.TONE_DTMF_S,
            '0' to ToneGenerator.TONE_DTMF_0,
            '#' to ToneGenerator.TONE_DTMF_P,
        )

    override fun startTone(char: Char) {
        dtmfs[char]?.let {
            toneGenerator.startTone(it)
        }
    }

    override fun stopTone() {
        toneGenerator.stopTone()
    }
}

class DummyTonePlayer : TonePlayer {
    override fun startTone(char: Char) {}

    override fun stopTone() {}
}
