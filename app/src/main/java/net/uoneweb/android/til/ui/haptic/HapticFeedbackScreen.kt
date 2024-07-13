package net.uoneweb.android.til.ui.haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

@Composable
fun HapticFeedbackScreen() {
    LazyColumn {
        item {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PredefinedVibration("Click", VibrationEffect.EFFECT_CLICK)
                PredefinedVibration("Double Click", VibrationEffect.EFFECT_DOUBLE_CLICK)
                PredefinedVibration("Heavy Click", VibrationEffect.EFFECT_HEAVY_CLICK)
                PredefinedVibration("Tick", VibrationEffect.EFFECT_TICK)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                HapticFeedbackConstantsButton(label = "NO_HAPTICS", effect = HapticFeedbackConstants.NO_HAPTICS)
                HapticFeedbackConstantsButton(label = "LONG_PRESS", effect = HapticFeedbackConstants.LONG_PRESS)
                HapticFeedbackConstantsButton(label = "VIRTUAL_KEY", effect = HapticFeedbackConstants.VIRTUAL_KEY)
                HapticFeedbackConstantsButton(label = "KEYBOARD_TAP", effect = HapticFeedbackConstants.KEYBOARD_TAP)
                HapticFeedbackConstantsButton(label = "CLOCK_TICK", effect = HapticFeedbackConstants.CLOCK_TICK)
                HapticFeedbackConstantsButton(label = "CONTEXT_CLICK", effect = HapticFeedbackConstants.CONTEXT_CLICK)
                HapticFeedbackConstantsButton(label = "KEYBOARD_PRESS", effect = HapticFeedbackConstants.KEYBOARD_PRESS)
                HapticFeedbackConstantsButton(label = "KEYBOARD_RELEASE", effect = HapticFeedbackConstants.KEYBOARD_RELEASE)
                HapticFeedbackConstantsButton(label = "VIRTUAL_KEY_RELEASE", effect = HapticFeedbackConstants.VIRTUAL_KEY_RELEASE)
                HapticFeedbackConstantsButton(label = "TEXT_HANDLE_MOVE", effect = HapticFeedbackConstants.TEXT_HANDLE_MOVE)
                HapticFeedbackConstantsButton(label = "GESTURE_START", effect = HapticFeedbackConstants.GESTURE_START)
                HapticFeedbackConstantsButton(label = "GESTURE_END", effect = HapticFeedbackConstants.GESTURE_END)
                HapticFeedbackConstantsButton(label = "CONFIRM", effect = HapticFeedbackConstants.CONFIRM)
                HapticFeedbackConstantsButton(label = "REJECT", effect = HapticFeedbackConstants.REJECT)
                HapticFeedbackConstantsButton(label = "TOGGLE_ON", effect = HapticFeedbackConstants.TOGGLE_ON)
                HapticFeedbackConstantsButton(label = "TOGGLE_OFF", effect = HapticFeedbackConstants.TOGGLE_OFF)
                HapticFeedbackConstantsButton(label = "GESTURE_THRESHOLD_ACTIVATE", effect = HapticFeedbackConstants.GESTURE_THRESHOLD_ACTIVATE)
                HapticFeedbackConstantsButton(label = "GESTURE_THRESHOLD_DEACTIVATE", effect = HapticFeedbackConstants.GESTURE_THRESHOLD_DEACTIVATE)
                HapticFeedbackConstantsButton(label = "DRAG_START", effect = HapticFeedbackConstants.DRAG_START)
                HapticFeedbackConstantsButton(label = "SEGMENT_TICK", effect = HapticFeedbackConstants.SEGMENT_TICK)
                HapticFeedbackConstantsButton(label = "SEGMENT_FREQUENT_TICK", effect = HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                CompositionPrimitivesButton(
                    label = "PRIMITIVE_CLICK",
                    effect = VibrationEffect.Composition.PRIMITIVE_CLICK,
                )
                CompositionPrimitivesButton(
                    label = "PRIMITIVE_THUD",
                    effect = VibrationEffect.Composition.PRIMITIVE_THUD,
                )
                CompositionPrimitivesButton(
                    label = "PRIMITIVE_SPIN",
                    effect = VibrationEffect.Composition.PRIMITIVE_SPIN,
                )
                CompositionPrimitivesButton(
                    label = "PRIMITIVE_SLOW_RISE",
                    effect = VibrationEffect.Composition.PRIMITIVE_SLOW_RISE,
                )
                CompositionPrimitivesButton(
                    label = "PRIMITIVE_QUICK_FALL",
                    effect = VibrationEffect.Composition.PRIMITIVE_QUICK_FALL,
                )
                CompositionPrimitivesButton(
                    label = "PRIMITIVE_TICK",
                    effect = VibrationEffect.Composition.PRIMITIVE_TICK,
                )
                CompositionPrimitivesButton(
                    label = "PRIMITIVE_LOW_TICK",
                    effect = VibrationEffect.Composition.PRIMITIVE_LOW_TICK,
                )
            }
        }
    }
}

@Composable
private fun PredefinedVibration(
    text: String,
    effectId: Int,
) {
    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Text(
            text = text,
            style = MaterialTheme.typography.h1,
            modifier =
            Modifier.clickable {

                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createPredefined(effectId))
            },
        )
    } else {
        Text("Not supported on this device.")
    }
}

@Composable
private fun HapticFeedbackConstantsButton(label: String, effect: Int) {
    val view = LocalView.current
    TextButton(
        onClick = {
            view.performHapticFeedback(effect)
        },
    ) {
        Text(label)
    }
}

@Composable
private fun CompositionPrimitivesButton(
    label: String,
    effect: Int,
) {
    val application = LocalContext.current.applicationContext
    val vibrator = ContextCompat.getSystemService(application, Vibrator::class.java)!!
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        TextButton(
            onClick = {
                vibrator.vibrate(
                    VibrationEffect.startComposition()
                        .addPrimitive(effect)
                        .compose(),
                )
            },
        ) {
            Text(label)
        }
    } else {
        Text("Not supported on this device.")
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(
    showBackground = true,
)
@Composable
private fun PredefinedVibrationPreview() {
    MaterialTheme {
        PredefinedVibration("Click", VibrationEffect.EFFECT_CLICK)
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun HapticFeedbackScreenPreview() {
    MaterialTheme {
        HapticFeedbackScreen()
    }
}

