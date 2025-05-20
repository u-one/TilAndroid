package net.uoneweb.android.til.ui.pager

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
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
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val boxWidth = maxWidth / 3
        val boxHeight = minOf(maxHeight / 4, boxWidth)
        val padding = minOf(maxHeight / 20, 16.dp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.fillMaxSize(),
        ) {
            items(12) { index ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            // .heightIn(boxHeight, boxWidth)
                            .height(boxHeight)
                            .padding(padding)
                            .background(Color.LightGray, CircleShape)
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
                    contentAlignment = Alignment.Center,
                ) {
                    val fontSize = with(LocalDensity.current) { (boxHeight / 3).toSp() }
                    Text(
                        text = state.labels[index].toString(),
                        style =
                            MaterialTheme.typography.headlineSmall.merge(
                                TextStyle(
                                    fontSize = fontSize,
                                    lineHeightStyle =
                                        LineHeightStyle(
                                            alignment = LineHeightStyle.Alignment.Center,
                                            trim = LineHeightStyle.Trim.Both,
                                        ),
                                ),
                            ),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 600)
@Preview(showBackground = true, heightDp = 500)
@Preview(showBackground = true, heightDp = 300)
@Preview(showBackground = true, heightDp = 200)
@Composable
fun DialPadPreview() {
    DialPad(
        modifier = Modifier.fillMaxSize(),
        onButtonPress = { _ ->
        },
        state = DialPadStateImpl(),
    )
}
