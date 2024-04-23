package net.uoneweb.android.til.ui.pager

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun rememberDialPadState(outputTone: Boolean = false): DialPadState {
    return remember(outputTone) {
        if (outputTone) {
            DialPadStateImpl()
        } else {
            DefaultDialPadState()
        }
    }
}

@Stable
interface DialPadState {
    val labels: List<String>

    fun onButtonPress(index: Int)

    fun onButtonRelease()
}

@Stable
class DialPadStateImpl() : DialPadState {
    override val labels = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_DTMF, ToneGenerator.MAX_VOLUME)
    private val dtmfs =
        listOf(
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
            ToneGenerator.TONE_DTMF_P,
        )

    override fun onButtonPress(index: Int) {
        toneGenerator.startTone(dtmfs[index])
    }

    override fun onButtonRelease() {
        toneGenerator.stopTone()
    }
}

@Stable
class DefaultDialPadState : DialPadState {
    override val labels = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")

    override fun onButtonPress(index: Int) {}

    override fun onButtonRelease() {}
}

@Composable
fun DialPad(
    modifier: Modifier = Modifier,
    onButtonPress: (String) -> Unit,
    state: DialPadState = rememberDialPadState(),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxWidth(),
    ) {
        items(12) { index ->
            Box(
                modifier =
                    Modifier
                        .padding(10.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    state.onButtonPress(index)
                                    onButtonPress(state.labels[index])
                                    tryAwaitRelease()
                                    state.onButtonRelease()
                                },
                            )
                        },
            ) {
                Card(modifier = Modifier.size(100.dp), elevation = 10.dp) {
                    Text(
                        text = state.labels[index],
                        modifier.align(
                            alignment = Alignment.Center,
                        ),
                        style = MaterialTheme.typography.h3,
                        color = Color.Gray,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DialPadPreview() {
    DialPad(
        onButtonPress = { _ ->
        },
        state =
            object : DialPadState {
                override val labels = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")

                override fun onButtonPress(index: Int) {}

                override fun onButtonRelease() {}
            },
    )
}
