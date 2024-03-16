package net.uoneweb.android.til.ui.haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HapticFeedbackScreen() {
    LazyColumn {
        item {
            PredefinedVibration("Click", VibrationEffect.EFFECT_CLICK)
            PredefinedVibration("Double Click", VibrationEffect.EFFECT_DOUBLE_CLICK)
            PredefinedVibration("Heavy Click", VibrationEffect.EFFECT_HEAVY_CLICK)
            PredefinedVibration("Tick", VibrationEffect.EFFECT_TICK)
        }
    }

}

@Composable
private fun PredefinedVibration(text: String, effectId: Int) {
    val context = LocalContext.current
    Text(text = text,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.clickable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createPredefined(effectId))
            }
        }
    )
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
