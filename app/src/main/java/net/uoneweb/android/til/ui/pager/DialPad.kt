package net.uoneweb.android.til.ui.pager

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

interface DialPadStateFactory {
    fun create(context: Context?): DialPadState
}

class DialPadStateFactoryImpl(private val tonePlayer: TonePlayer) : DialPadStateFactory {
    override fun create(context: Context?): DialPadState {
        return DialPadStateImpl(tonePlayer, context)
    }
}

@Stable
interface DialPadState {
    val labels: List<Char>
    var playTone: Boolean
    var enableHapticFeedback: Boolean

    fun onButtonPress(char: Char)

    fun onButtonRelease()
}

@Stable
class DialPadStateImpl(private val tonePlayer: TonePlayer = DummyTonePlayer(), private val context: Context? = null) : DialPadState {
    override var playTone by mutableStateOf(true)
    override var enableHapticFeedback by mutableStateOf(true)

    override val labels = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '0', '#')

    override fun onButtonPress(char: Char) {
        if (playTone) {
            tonePlayer.startTone(char)
        }
        if (enableHapticFeedback) {
            hapticFeedback()
        }
    }

    override fun onButtonRelease() {
        if (playTone) {
            tonePlayer.stopTone()
        }
    }

    private fun hapticFeedback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager?
            val vibrator = vibratorManager?.defaultVibrator
            vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        }
    }
}

@Composable
fun DialPad(
    modifier: Modifier = Modifier,
    onButtonPress: (Char) -> Unit,
    state: DialPadState = DialPadStateImpl(),
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
                                    val char = state.labels[index]
                                    state.onButtonPress(char)
                                    onButtonPress(char)
                                    tryAwaitRelease()
                                    state.onButtonRelease()
                                },
                            )
                        },
            ) {
                Card(modifier = Modifier.fillMaxSize(), elevation = 10.dp) {
                    Text(
                        text = state.labels[index].toString(),
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
        state = DialPadStateImpl(),
    )
}
