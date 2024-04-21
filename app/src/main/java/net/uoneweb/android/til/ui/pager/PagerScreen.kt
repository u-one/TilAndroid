package net.uoneweb.android.til.ui.pager

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PagerScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PagerLcd(Modifier.height(160.dp))
    }
}


@Composable
fun PagerLcd(modifier: Modifier = Modifier) {
    val state = rememberPagerLcdState()
    val str1 = "TOKYO"
    val str2 = "TELEMESSAGE"
    var inputText by remember { mutableStateOf("") }
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_DTMF, ToneGenerator.MAX_VOLUME) }
    LaunchedEffect(Unit) {
        state.updateWithText(str1 + "        " + str2)
    }
    LaunchedEffect(inputText) {
        state.update(inputText)
    }
    Column {
        PagerLcdHeader()
        Row {
            PagerLcdLeft()
            DotMatrixLcd(state.dotMatrixLcdState, modifier.background(Color(0xFF446644)))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = inputText,
            style = MaterialTheme.typography.subtitle1,
            color = Color.Black,
            modifier = Modifier.padding(10.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth()
        ) {
            val labels = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
            val dtmfs = listOf(
                ToneGenerator.TONE_DTMF_1,
                ToneGenerator.TONE_DTMF_2,
                ToneGenerator.TONE_DTMF_3,
                ToneGenerator.TONE_DTMF_4,
                ToneGenerator.TONE_DTMF_5,
                ToneGenerator.TONE_DTMF_6,
                ToneGenerator.TONE_DTMF_7,
                ToneGenerator.TONE_DTMF_8,
                ToneGenerator.TONE_DTMF_9,
                ToneGenerator.TONE_DTMF_S,
                ToneGenerator.TONE_DTMF_0,
                ToneGenerator.TONE_DTMF_P
            )
            items(12) { key ->
                Box(modifier =
                Modifier.padding(10.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                inputText += labels[key]
                                toneGenerator.startTone(dtmfs[key])
                                //playDTMFTone(dtmfs[key])
                                tryAwaitRelease()
                                toneGenerator.stopTone()
                            }
                        )
                    }) {
                    Card{
                        Text(
                            text = labels[key],
                            modifier.size(100.dp).align(
                                alignment = Alignment.Center
                            ),
                            style = MaterialTheme.typography.h3,
                            color = Color.Gray
                        )
                    }

                }
            }
        }
    }
}

private fun playDTMFTone(dtmfTone: Int) {
    val toneGenerator = ToneGenerator(AudioManager.STREAM_DTMF, ToneGenerator.MAX_VOLUME)
    toneGenerator.startTone(dtmfTone)
    toneGenerator.stopTone()
    toneGenerator.release()
}

@Composable
fun PagerLcdHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(Color(0xFF446644))
    )
}

@Composable
fun PagerLcdLeft() {
    Box(
        modifier = Modifier
            .height(160.dp)
            .width(20.dp)
            .background(Color(0xFF446644))
    )
}


@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
fun PagerLcdPreview() {
    PagerLcd()
}

@Composable
@Preview(showBackground = true)
fun PagerScreenPreview() {
    PagerScreen()
}